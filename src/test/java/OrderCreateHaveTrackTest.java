import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.CreateOrderSerialization;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateHaveTrackTest {
    private String deleteOrder;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("When an order is created, its track is returned")
    public void createOrderReturnTrack(){
        Response responseToCreateOrder =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(new CreateOrderSerialization("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha"))
                        .when()
                        .post("/api/v1/orders");
        responseToCreateOrder.then().assertThat().body("track", notNullValue());
        deleteOrder = responseToCreateOrder.getBody().path("track").toString();
    }
    @After
    public void orderCancel(){
        Response responseToCancelOrder =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .param("track", deleteOrder)
                        .when()
                        .put("/api/v1/orders/cancel");
    }
}
