# Paperless Pen and Paper - gruppe5

## Authentication
Authentication is implemtentet with [Auth0](https://auth0.com/), an OAuth2 login system.

These parameters are used for the login system.

```
auth0_client_id=fO2ATpY1mfFrutuO9HLpiiF80qQRSJiH
auth0_domain=dev-paperlesspenandpaper.eu.auth0.com
auth0_scheme=android
audience=https://dev-paperlesspenandpaper.eu.auth0.com/api/v2/
```

 RESTful request, which requires a logged-in user, must have a bearer token added to the  `Authorization` header.

## Backend

### Configuration
The Server has a default configuration (`application.properties`). The configuration is located inside the jar (path: `\BOOT-INF\classes\`).

To add or overwrite parts of the configuration add a new `application.properties` file next to the jar (the config file can also be placed into a folder).

### Start the Webserver on a local machine
It is required to have a java installation.

1. Open the comandline
2. Go to the folder where the jar is located
3. Run the command `java -jar backend-0.0.1-SNAPSHOT.jar`
Note: Binding a port below 1024 on a linux system may requires root privileges

### Testing if the Webserver is running
Access ip/domain of the webserver with the path `/hello-world`. The Webserver will return code `200` with the message `Hello World!`.

### OpenAPI/Swagger-UI
The api definitions can be found [here](https://msp-ws2122-5.mobile.ifi.lmu.de/api-docs).
The request can be tested in the Swagger-UI.

#### Test requests which require login
A valid JWT token musst be added.
The token can be obtained by validly logging in to the android app. Note that a token can expire.

## Webserver on LMU Server
This project has a virtual Server that is hostet by the LMU/LRZ

### Domain
The domain is [msp-ws2122-5.mobile.ifi.lmu.de](https://msp-ws2122-5.mobile.ifi.lmu.de).

### SSL Certificate
Generated with **Certbot** provided by **Let's Encrypt** and exported to a `p12` file. To enable **ssl** the `springboot` user must be able to read the cert file.

### Upload and start new backend version
Note: The jar file must be able to be executed (without java -jar filename.jar).
1. Save the new file as `/home/praktikant/backend/backend.jar`. (E.g. use **scp** to upload the new jar)
2. Use **ssh** to access the commandline
3. Execute `/home/praktikant/backend/updateJar.sh`. This script will move the uploaded jar and modify its rights and restart the backend.

The backend is executed with the user `springboot`.

### Restart/Start Backend
The Backend is running as **systemd** service (`springboot-backend.service`).
- Start: `sudo systemctl start springboot-backend.service`
- Stop: `sudo systemctl stop springboot-backend.service`
- Restart: `sudo systemctl restart springboot-backend.service`
- Restart: `sudo systemctl status springboot-backend.service`

The service is configurated to start on boot.

Note: Reload may work, but isn't configurated and tested yet.

### Logging
At the moment the output is logged by systemd.
To access the logs use `journalctl`. E.g. access the `springboot-backend` logs of the last 5min `sudo journalctl --since "5 min ago" -u springboot-backend`.

Note: journalctl uses **vi**. You can exit **vi** by typing `:q` + enter.

## Frontend
### Login credentials for examiners:
```
Email:	prüfer1@msp.de
PW: 	20Kotlin22
Email:	prüfer2@msp.de
PW: 	20Kotlin22
```
### Known Bugs:
- App crashes without internet
- App crashed when you send message to wrong userId
- Websocket disconnect or crash isn't noticable nor does the websocket reconnect automatically
- cached data isn't shown in the UI (chat and paint strokes)
- can not delete received messages
- User List doesn't refresh on name change or user delete
- minimum API version 26 required
- UI doesn't work with darktheme 
