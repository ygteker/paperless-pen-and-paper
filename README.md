# gruppe5

---

## Backend

### Configuration
The Server has a default configuration (`application.properties`). The configuration is located inside the jar (path: `\BOOT-INF\classes\`).

To add or overwrite parts of the configuration add a new `application.properties` file next to the jar (the config file can also be placed into a folder).

### Start the Webserver
It is required to have a java installation.

1) Open the comandline
2) Go to the folder where the jar is located
3) Run the command `java -jar backend-0.0.1-SNAPSHOT.jar`
Note: Binding a port below 1024 on a linux system may requires root privileges

### Testing if the Webserver is running
Access ip/domain of the webserver with the path `/hello-world`. The Webserver will return code `200` with the message `Hello World!`.

### OpenAPI/Swagger-UI
The api definitions can be found [here](http://msp-ws2122-5.mobile.ifi.lmu.de/api-docs).

---

## Frontend

---