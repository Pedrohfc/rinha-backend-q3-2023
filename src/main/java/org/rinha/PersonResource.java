package org.rinha;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.rinha.model.Person;

import java.net.URI;
import java.sql.SQLException;
import java.util.UUID;

@Path("/pessoas")
public class PersonResource
{
    private static final Logger LOG = Logger.getLogger(PersonResource.class);

    @Inject
    PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPerson(Person person)
    {
        try
        {
            personService.validatePerson(person);
            person.setId(UUID.randomUUID().toString());
            personService.createPerson(person);
            return Response.created(URI.create("/pessoas/" + person.getId())).entity(person).build();
        }
        catch (Exception ex)
        {
            LOG.error(ex.getMessage(), ex);
            return Response.status(422).entity("").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPessoas()
    {
        return new Person();
    }

    @GET()
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") String uuid) throws SQLException
    {
        Person person = personService.findPerson(uuid);
        if (person != null)
            return Response.ok(person).build();
        return Response.status(404).build();
    }
}
