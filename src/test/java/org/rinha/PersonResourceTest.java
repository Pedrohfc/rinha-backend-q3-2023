package org.rinha;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rinha.model.Person;

import java.util.List;

import static io.restassured.RestAssured.given;

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