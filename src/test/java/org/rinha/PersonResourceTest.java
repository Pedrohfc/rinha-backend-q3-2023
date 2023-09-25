package org.rinha;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rinha.model.Person;

import java.util.List;

import static io.restassured.RestAssured.given;

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
 * phcassiano 24/09/2023 Criacao inicial
 */
@QuarkusTest
class PersonResourceTest
{
    Person person;

    @BeforeEach
    void setUp()
    {
        person = new Person();
        person.setApelido("farofa");
        person.setNome("Pedro");
        person.setNascimento("1996-06-22");
        person.setStack(List.of("JAVA"));
    }

    private void shouldReturn422()
    {
        given()
                .body(person)
                .contentType("application/json")
                .when()
                .post("/pessoas")
                .then()
                .statusCode(422);
    }

    @Test
    void postPersonWithInvalidNickName_shouldReturn422()
    {
        String[] invalidNickName = { null, "", "a string with 33 characteres #--#" };
        for (String nickName : invalidNickName)
        {
            person.setApelido(nickName);
            shouldReturn422();
        }
    }

    @Test
    void postPersonWitoutName_shouldReturn422()
    {
        String[] invalidName = { null, "",
                                 "a string with 101 characteres #---------------------------------------------------------------------#" };
        for (String name : invalidName)
        {
            person.setNome(name);
            shouldReturn422();
        }
    }

    @Test
    void postPersonWithInvalidBirthDate_shouldReturn422()
    {
        String[] invalidDates = {"", null, "abc", "22/06/1996", "1996-13-01", "1996-12-50"};
        for (String date : invalidDates)
        {
            person.setNascimento(date);
            shouldReturn422();
        }
    }

    @Test
    void postPersonWithInvalidStack_shouldReturn422()
    {
        person.setStack(List.of("a string with 33 characteres #--#"));
        shouldReturn422();
    }
}