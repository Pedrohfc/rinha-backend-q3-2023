package org.rinha;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.rinha.model.Person;

import java.net.URI;
import java.util.UUID;

/* * Copyright (c) - UOL Inc,
 * Todos os direitos reservados
 *
 * Este arquivo e uma propriedade confidencial do Universo Online Inc.
 * Nenhuma parte do mesmo pode ser copiada, reproduzida, impressa ou
 * transmitida por qualquer meio sem autorização expressa e por escrito
 * de um representante legal do Universo Online Inc.
 *
 * All rights reserved
 *
 * This file is a confidential property of Universo Online Inc.
 * No part of this file may be reproduced or copied in any form or by
 * any means without written permission from an authorized person from
 * Universo Online Inc.
 *
 * Histórico de revisões
 * Autor Data Motivo
 * --------------------------------- ---------- -----------------------
 * phcassiano 22/09/2023 Criacao inicial
 */
@Path("/pessoas")
public class PersonResource
{
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
            ex.printStackTrace();
            return Response.status(422).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPessoas()
    {
        return new Person();
    }
}
