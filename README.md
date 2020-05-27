# Fisojo [![Build Status](https://travis-ci.org/jochettino/fisojo.svg?branch=master)](https://travis-ci.org/jochettino/fisojo)
Fisojo is a Fisheye code review notifier for Slack


## Setting up Fisojo
First of all you need some configuration about where Fisojo should read and write. Please, read [how to configure](docs/how-to-configure.md).

## Running Fisojo
- You can run it locally with a config file like [this](config.props.example) or using enviroment variables.
- You can run it into a container using enviroment variables executing something like
```bash
$ docker run --env SLACK_WEBHOOK_URL=<URL> --env FISHEYE_FEAUTH=<feauth> --env FISHEYE_BASE_SERVER_URL=<febase> --env FISHEYE_PROJECT_ID=<project> --env FISHEYE_POLLING_FREQUENCY=<freq> fisojo:latest
```
