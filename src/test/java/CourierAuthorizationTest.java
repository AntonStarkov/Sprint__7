import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import static org.hamcrest.Matchers.*;

public class CourierAuthorizationTest {
    private static Courier courier;
    @Before
    public void setUpUriAndCreateCourier() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier();
        courier.deleteCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        Response responseToCreateCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourier.
                then().
                assertThat().
                statusCode(201);
    }
    @Test
    @DisplayName("Created courier can log in")
    public void courierCanAuth(){
        Response responseLogin = courier.loginCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        responseLogin.
                then().
                assertThat().
                statusCode(200);
    }
    @Test
    @DisplayName("Impossible to log in without the login field")
    public void cannotAuthWithoutLogin(){
        Response responseLogin = courier.loginCourier("{\"password\": \"1234\"}");
        responseLogin.
                then().
                assertThat().
                statusCode(400);
    }
    @Test
    @DisplayName("Impossible to log in without the password field")
    public void cannotAuthWithoutPassword(){
        Response responseLogin = courier.loginCourier("{\"login\": \"ajnin\"}");
        responseLogin.
                then().
                assertThat().
                statusCode(400);
    }
    @Test
    @DisplayName("Authorization without login returns an error")
    public void authWithoutLoginReturnError(){
        Response responseLogin = courier.loginCourier("{\"password\": \"1234\"}");
        responseLogin.
                then().
                assertThat().
                body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Authorization without password returns an error")
    public void authWithoutPasswordReturnError(){
        Response responseLogin = courier.loginCourier("{\"login\": \"ajnin\"}");
        responseLogin.
                then().
                assertThat().
                body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Authorization with incorrect login returns an error")
    public void authWithIncorrectLoginReturnError(){
        Response responseLogin = courier.loginCourier("{\"login\": \"ajninw\", \"password\": \"1234\"}");
        responseLogin.
                then().
                assertThat().
                body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Authorization with incorrect password returns an error")
    public void authWithIncorrectPasswordReturnError(){
        Response responseLogin = courier.loginCourier("{\"login\": \"ajnin\", \"password\": \"12345\"}");
        responseLogin.
                then().
                assertThat().
                body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("When authorizing the courier, returns the ID")
    public void courierAuthReturnId(){
        Response responseLogin = courier.loginCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        responseLogin.
                then().
                assertThat().
                body("id", greaterThan(0));
    }
    @After
    public void deleteCourier() {
        courier.deleteCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
    }

}
