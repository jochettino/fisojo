# How to configure
Here there are some guidelines for configuring Fisojo

## Fisheye
Fisheye is the source of data for reading.

### feauth
Getting the feauth is easy just run this curl to your Fisheye server (`<fe-server>`):

```bash
$ curl -X POST -H 'Content-type: application/x-www-form-urlencoded' 'https://<fe-server>/rest-service-fecru/auth/login?userName=<username>&password=<passowrd>'

{"token":"username:12345:blablablablablablabla"}
```

## Slack
Slack is the destination of data for writing. 

### webhook
The slack webwook only can be created by me for now. So ask me for it :)
