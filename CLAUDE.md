# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Fisojo is a long-running Kotlin daemon (Maven, JDK 8, Kotlin 1.3) that polls an Atlassian Fisheye/Crucible server for newly created code reviews and posts them to Slack via an incoming webhook.

## Commands

- Build + tests: `mvn verify -B` (what Travis CI runs)
- Tests only: `mvn test`; single test class: `mvn test -Dtest=ConfigReaderImplTest`
- Package: `mvn package` produces `target/fisojo-<version>-jar-with-dependencies.jar` (the runnable fat jar; main class `com.github.jochettino.fisojo.RunKt`)
- Run with a config file: `java -jar target/fisojo-*-jar-with-dependencies.jar --file=config.props` (see `config.props.example`)
- Run with env vars: source a copy of `setenv.sh.example`, then run the jar with no `--file`
- Flags: `--debug`/`-d`, `--file=<path>`/`-f=<path>`, `--help`/`-h`
- Docker: the `Dockerfile` builds the jar and runs it with `--debug`; config is passed as `SLACK_WEBHOOK_URL`, `FISHEYE_FEAUTH`, `FISHEYE_BASE_SERVER_URL`, `FISHEYE_PROJECT_ID`, `FISHEYE_POLLING_FREQUENCY`. The jar name in the `Dockerfile` hardcodes the version from `pom.xml`, so update both together when bumping the version.

## Architecture

Everything is driven by the loop in `Run.kt`:

1. `main` picks a `ConfigHandler` implementation: `FileConfigHandlerImpl` if `--file` is given, otherwise `EnvironmentConfigHandlerImpl`. Both expose `getFisheyeConfig()` and `getSlackConfig()`.
2. Each cycle, `FisheyeHandler.getReviewsData()` calls the Fisheye REST filter endpoint (`/rest-service/reviews-v1/filter`, with `states=Review`, the project ID, and `FEAUTH` as a query param) and parses the JSON with Gson into the DTOs in `dto/Fisheye.kt`.
3. If there are new reviews, `SlackHandler.sendMessageToSlack()` posts them to the webhook.
4. The loop sleeps `pollingFrequency` seconds. On an exception from either handler it logs the error plus the handler's `lastHttpCall`, sleeps 4× the polling frequency, and retries. The loop never exits.

Non-obvious behavior:
- **De-duplication is in-memory state.** `FisheyeHandler` keeps `config.lastCrTime` (a mutable field on `FisheyeConfig`), and a review counts as "new" only if its `createDate` is after that time. Each query also uses `fromDate = now - 1 minute`. Restarting the process resets the state, and a polling interval longer than that window can miss reviews.
- Both handlers expose `lastHttpCall`, which `Run.kt` uses purely for error logging.
- Logging goes through `LoggerProvider` (log4j2), and `--debug` raises the default level at runtime.

## Docs

`docs/how-to-configure.md` explains how to obtain the Fisheye `FEAUTH` token (POST to `/rest-service-fecru/auth/login`). The Slack webhook is created by the maintainer.
