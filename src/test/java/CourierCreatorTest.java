import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import static org.hamcrest.Matchers.*;

public class CourierCreatorTest {
    private static Courier courier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier();
        courier.deleteCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
    }
    @Test
    @DisplayName("Possible to create a courier with correct fields")
    public void canCreateCourier(){
        courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        Response responseToLoginByCreatedCourier = courier.loginCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        responseToLoginByCreatedCourier.
                then().
                assertThat().
                body("id", notNullValue());
    }
    @Test
    @DisplayName("Impossible to create two identical couriers")
    public void cannotCreateEqualsCouriersTwoTimes(){
        Response responseToCreateCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourier.
                then().
                assertThat().
                statusCode(201);
        Response responseToCreateSecondCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateSecondCourier.
                then().
                assertThat().
                statusCode(409);
    }
    @Test
    @DisplayName("Impossible to create a courier without a login field")
    public void cannotCreateCourierWithoutLogin(){
        Response responseToCreateCourierWithoutLogin = courier.createCourier("{\"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourierWithoutLogin.
                then().
                assertThat().
                statusCode(400);
    }
    @Test
    @DisplayName("Impossible to create a courier without a password field")
    public void cannotCreateCourierWithoutPassword(){
        Response responseToCreateCourierWithoutPassword = courier.createCourier("{\"login\": \"ajnin\", \"firstName\": \"saske\"}");
        responseToCreateCourierWithoutPassword.
                then().
                assertThat().
                statusCode(400);
    }
    @Test
    @DisplayName("Impossible to create a courier without a firstname field")
    public void cannotCreateCourierWithoutFirstname(){
        Response responseToCreateCourierWithoutFirstname = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        responseToCreateCourierWithoutFirstname.
                then().
                assertThat().
                statusCode(400);
    }
    @Test
    @DisplayName("When creating a courier, returns the correct status code")
    public void returnCorrectStatusCodeWhenCreateCourier(){
        Response responseToCreateCourierReturnCorrectStatusCode = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        responseToCreateCourierReturnCorrectStatusCode.
                then().
                assertThat().
                statusCode(201);
    }
    @Test
    @DisplayName("When creating a courier, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourier(){
        Response responseToCreateCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourier.
                then().
                assertThat().
                body("ok", equalTo(true));
    }
    @Test
    @DisplayName("When creating a courier without login field, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourierWithoutLogin(){
        Response responseToCreateCourierWithoutLogin = courier.createCourier("{\"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourierWithoutLogin.
                then().
                assertThat().
                body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("When creating a courier without password field, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourierWithoutPassword(){
        Response responseToCreateCourierWithoutPassword = courier.createCourier("{\"login\": \"ajnin\", \"firstName\": \"saske\"}");
        responseToCreateCourierWithoutPassword.
                then().
                assertThat().
                body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("When creating a courier without firstname field, returns the correct JSON body")
    public void returnCorrectResponseWhenCreateCourierWithoutFirstname(){
        Response responseToCreateCourierWithoutFirstname = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        responseToCreateCourierWithoutFirstname.
                then().
                assertThat().
                body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("When creating two identical couriers, returns the correct status code")
    public void errorIfCreateEqualsCourierLogins(){
        Response responseToCreateCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourier.
                then().
                assertThat().
                statusCode(201);
        Response responseToCreateSecondCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateSecondCourier.
                then().
                assertThat().
                body("message", equalTo("Этот логин уже используется"));
    }
    @After
    public void deleteCourier() {
        courier.deleteCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        }
    }