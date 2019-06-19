# How to configure
Here there are some guidelines for configuring Fisojo

## Fisheye
Fisheye is the source of data for reading.
### FEAUTH
Getting the feauth is easy just run this curl:
`$ curl -X POST -H 'Content-type: application/x-www-form-urlencoded' 'https://<fe-server>/rest-service-fecru/auth/login?userName=<username>&password=<passowrd>'
{"token":"username:12345:blablablablablablabla"}`


## Slack
Slack is the destination of data for writing. 

