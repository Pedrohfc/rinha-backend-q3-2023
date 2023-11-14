package org.rinha;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.hamcrest.Matchers;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rinha.model.Person;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class PersonResourceTest
{
    Person person;

    @BeforeEach
    void setUp()
    {
        person = new Person();
        person.setApelido(UUID.randomUUID().toString().replaceAll("-", ""));
        person.setNome("Pedro");
        person.setNascimento("1996-06-22");
        person.setStack(List.of("JAVA", "C++", "Javascript"));
    }

    private void postPersonShouldReturn(int statusCode)
    {
        postPersonShouldReturn(statusCode, person);
    }

    private static void postPersonShouldReturn(int statusCode, Object json)
    {
        postPerson(json)
                .then()
                .statusCode(statusCode);
    }

    private static Response postPerson(Object json)
    {
        return given()
                .body(json)
                .contentType("application/json")
                .when()
                .post("/pessoas");
    }

    @Test
    void postPersonWithInvalidNickName_shouldReturn422()
    {
        String[] invalidNickName = { null, "", "a string with 33 characteres #--#" };
        for (String nickName : invalidNickName)
        {
            person.setApelido(nickName);
            postPersonShouldReturn(422);
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
            postPersonShouldReturn(422);
        }
    }

    @Test
    void postPersonWithInvalidBirthDate_shouldReturn422()
    {
        String[] invalidDates = { "", null, "abc", "22/06/1996", "1996-13-01", "1996-12-50" };
        for (String date : invalidDates)
        {
            person.setNascimento(date);
            postPersonShouldReturn(422);
        }
    }

    @Test
    void postPersonWithInvalidStack_shouldReturn422()
    {
        person.setStack(List.of("a string with 33 characteres #--#"));
        postPersonShouldReturn(422);
    }

    @Test
    void postPerson_shoulReturn201AndLocationHeader()
    {
        postPerson(person)
                .then()
                .statusCode(201)
                .header("Location", MatchesPattern.matchesPattern("https?://.+/pessoas/.+"));
    }

    @Test
    void postPersonWithDuplicatedNickName_shouldReturn422()
    {
        postPersonShouldReturn(201);
        postPersonShouldReturn(422);
    }

    @Test
    void postPersonWithNameInvalidSyntax_shouldReturn400()
    {
        JsonObject json = Json.createObjectBuilder()
                .add("nome", 1)
                .add("apelido", person.getApelido())
                .add("nascimento", person.getNascimento())
                .add("stack", Json.createArrayBuilder(person.getStack()))
                .build();
        postPersonShouldReturn(400, json.toString());
    }

    @Test
    void postPersonWithStackInvalidSyntax_shouldReturn400()
    {
        JsonObject json = Json.createObjectBuilder()
                .add("nome", person.getNome())
                .add("apelido", person.getApelido())
                .add("nascimento", person.getNascimento())
                .add("stack", Json.createArrayBuilder(List.of("JAVA", 1)))
                .build();
        postPersonShouldReturn(400, json.toString());
    }

    @Test
    void getPostedPerson()
    {
        Response response = postPerson(person);
        String location = response.header("Location");
        given()
                .get(URI.create(location))
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(location.split("/")[4]))
                .body("nome", Matchers.equalTo(person.getNome()))
                .body("nascimento", Matchers.equalTo(person.getNascimento()))
                .body("stack", Matchers.equalTo(person.getStack()))
                .body("apelido", Matchers.equalTo(person.getApelido()));
    }

    @Test
    void getPersonWithInvalidId_shouldReturn404()
    {
        given()
                .get("/pessoas/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    void searchWithoutT_shouldReturn400()
    {
        given()
                .get("/pessoas")
                .then()
                .statusCode(400);
    }
}