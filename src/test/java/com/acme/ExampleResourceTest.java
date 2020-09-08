package com.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testWhenItsLounchd() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body(
                        containsString("Matic"),
                        containsString("Luka"),
                        containsString("Marko"));
    }
    @Test
    public void testUserID() {

        given().when().get("users/1").then().statusCode(200).body(containsString("Marko"));
        given().when().get("users/string").then().statusCode(404);
    }
    @Test
    public void testAddOKUser() {
        given()
                .when()
                .body("{\"username\" : \"user\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body(
                        containsString("Matic"),
                        containsString("Luka"),
                        containsString("user"),
                        containsString("Marko"));
    }
    @Test
    public void testAddFAILUser() {
        given()
                .when()
                .body("{\"username1\" : \"user1\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(422);
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body(
                        containsString("Matic"),
                        not(containsString("user1")),
                        containsString("Luka"),
                        containsString("Marko"));

    }
    @Test
    public void testNotFound() {
        given().when().get("users1/string").then().statusCode(404);
    }
    @Test
    public void like() {

    }
    @Test
    void shouldNotAccessUserWhenAdminAuthenticated() {
        given()
                .when()
                .body("{\"username\" : \"user\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);

        given()
                .auth().preemptive().basic("user1", "user123")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
    @Test
    void shouldAccessUser() {
        given()
                .when()
                .body("{\"username\" : \"user\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);

        given()
                .auth().preemptive().basic("user", "user123")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    void shouldLikeAccessUser() {
        given()
                .when()
                .body("{\"username\" : \"user4\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);

        given()
                .auth().preemptive().basic("user4", "user123")
                .when()
                .get("/api/users/like/1")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    void shouldDisLikeAccessUser() {
        given()
                .when()
                .body("{\"username\" : \"user5\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);

        given()
                .auth().preemptive().basic("user5", "user123")
                .when()
                .get("/api/users/like/1")
                .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .auth().preemptive().basic("user5", "user123")
                .when()
                .get("/api/users/unlike/1")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    void shouldNOTDisLikeAccessUser() {
        given()
                .when()
                .body("{\"username\" : \"user5\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);

        given()
                .auth().preemptive().basic("user5", "user123")
                .when()
                .get("/api/users/like/1")
                .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .auth().preemptive().basic("user5", "user123")
                .when()
                .get("/api/users/unlike/ss")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
    @Test
    void changePass() {
        given()
                .when()
                .body("{\"username\" : \"user5\",\"password\" : \"user123\" }")
                .contentType("application/json")
                .post("/users/singUp")
                .then()
                .statusCode(201);

        given()
                .auth().preemptive().basic("user5", "user123")
                .when()
                .get("api/users/newPass/matic123")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);

        given()
                .auth().preemptive().basic("user5", "matic123")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}