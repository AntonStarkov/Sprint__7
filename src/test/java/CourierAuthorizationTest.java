import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.CreateCourierSerialization;
import ru.yandex.praktikum.DeleteCourierDeserialization;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierAuthorizationTest {
    private CreateCourierSerialization createCourierSerialization;
    @Before
    public void setUpUriAndCreateCourier() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        createCourierSerialization = new CreateCourierSerialization("ajnin", "1234", "saske");
        Response responseToCreateCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourierSerialization)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourier.then().assertThat().statusCode(201);
    }
    @Test
    @DisplayName("Created courier can log in")
    public void courierCanAuth(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"ajnin\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Impossible to log in without the login field")
    public void cannotAuthWithoutLogin(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("Impossible to log in without the password field")
    public void cannotAuthWithoutPassword(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"ajnin\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("Authorization without login returns an error")
    public void authWithoutLoginReturnError(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Authorization without password returns an error")
    public void authWithoutPasswordReturnError(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"ajnin\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Authorization with incorrect login returns an error")
    public void authWithIncorrectLoginReturnError(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"ajninw\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Authorization with incorrect password returns an error")
    public void authWithIncorrectPasswordReturnError(){
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"ajnin\", \"password\": \"12345\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("When authorizing the courier, returns the ID")
    public void courierAuthReturnId(){
        Response responseLoginReturnId =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"ajnin\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        responseLoginReturnId.then().assertThat().body("id", greaterThan(0));
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
