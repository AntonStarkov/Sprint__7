import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.DeleteCourierDeserialization;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreatorTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Possible to create a courier with correct fields")
    public void canCreateCourier(){
        String jsonToCreateCourier = "{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}";
        String jsonToLoginByCreatedCourier = "{\"login\": \"ajnin\", \"password\": \"1234\"}";
                Response responseToCreateCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        Response responseToLoginByCreatedCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToLoginByCreatedCourier)
                        .when()
                        .post("/api/v1/courier/login");
        responseToLoginByCreatedCourier.then().assertThat().body("id", notNullValue());
    }
    @Test
    @DisplayName("Impossible to create two identical couriers")
    public void cannotCreateEqualsCouriersTwoTimes(){
        String json = "{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}";
        Response responseToCreateCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourier.then().assertThat().statusCode(201);
        Response responseToCreateSecondCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateSecondCourier.then().assertThat().statusCode(409);
    }
    @Test
    @DisplayName("Impossible to create a courier without a login field")
    public void cannotCreateCourierWithoutLogin(){
        String jsonToCreateCourier = "{\"password\": \"1234\", \"firstName\": \"saske\"}";
        Response responseToCreateCourierWithoutLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourierWithoutLogin.then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("Impossible to create a courier without a password field")
    public void cannotCreateCourierWithoutPassword(){
        String jsonToCreateCourier = "{\"login\": \"ajnin\", \"firstName\": \"saske\"}";
        Response responseToCreateCourierWithoutPassword =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourierWithoutPassword.then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("Impossible to create a courier without a firstname field")
    public void cannotCreateCourierWithoutFirstname(){
        String jsonToCreateCourier = "{\"login\": \"ajnin\", \"password\": \"1234\"}";
        Response responseToCreateCourierWithoutFirstname =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourierWithoutFirstname.then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("When creating a courier, returns the correct status code")
    public void returnCorrectStatusCodeWhenCreateCourier(){
        String json = "{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201);
    }
    @Test
    @DisplayName("When creating a courier, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourier(){
        String json = "{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true));
    }
    @Test
    @DisplayName("When creating a courier without login field, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourierWithoutLogin(){
        String jsonToCreateCourier = "{\"password\": \"1234\", \"firstName\": \"saske\"}";
        Response responseToCreateCourierWithoutLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourierWithoutLogin.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("When creating a courier without password field, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourierWithoutPassword(){
        String jsonToCreateCourier = "{\"login\": \"ajnin\", \"firstName\": \"saske\"}";
        Response responseToCreateCourierWithoutPassword =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourierWithoutPassword.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("When creating a courier without firstname field, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourierWithoutFirstname(){
        String jsonToCreateCourier = "{\"login\": \"ajnin\", \"password\": \"1234\"}";
        Response responseToCreateCourierWithoutFirstname =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourierWithoutFirstname.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("When creating two identical couriers, returns the correct status code")
    public void errorIfCreateEqualsCourierLogins(){
        String json = "{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}";
        Response responseToCreateCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourier.then().assertThat().statusCode(201);
        Response responseToCreateSecondCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateSecondCourier.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }
    @After
    public void deleteCourier() {
        String json = "{\"login\": \"ajnin\", \"password\": \"1234\"}";
        DeleteCourierDeserialization deleteCourierDeserialization =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login")
                        .body()
                        .as(DeleteCourierDeserialization.class);
        while (true) {
            Response response =
                    given()
                            .delete("/api/v1/courier/" + deleteCourierDeserialization.getId());
            if (response.getStatusCode() != 200) {
                break;
            }
        }
    }
}