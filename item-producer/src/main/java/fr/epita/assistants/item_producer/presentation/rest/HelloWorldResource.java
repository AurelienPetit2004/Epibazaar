package fr.epita.assistants.item_producer.presentation.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class HelloWorldResource {
    @GET @Path("/")
    public String helloWorld() {
        return "Hello World!";
    }

    @GET @Path("/{name}")
    public String helloWorld(@PathParam("name") String name) {
        return "Hello " + name + "!";
    }
}
