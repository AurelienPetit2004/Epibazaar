Once your project is opened in your IDE, you can type the following commands to start every server:

42sh$ mvn clean install
42sh$ mvn quarkus:dev -pl inventory
42sh$ mvn quarkus:dev -pl item-producer
42sh$ mvn quarkus:dev -pl shop

Tips

As each service requires its own terminal if not detached, you can use IntelliJ Services with Alt +
8. You can then add a Quarkus service and launch your three services simultaneously.
It will launch your APIs on http://localhost:8081 for item-producer and on http://localhost:8082 for
shop. We have provided you two Hello World endpoints, so the next commands will return "Hello
World!".

42sh$ curl http://localhost:8081/hello
42sh$ curl http://localhost:8082/hello
