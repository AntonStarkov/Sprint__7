import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.CreateOrderSerialization;
import ru.yandex.praktikum.DeleteCourierDeserialization;
import ru.yandex.praktikum.OrderListDeserialization;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ResponseBodyReturnsListOfOrdersTest {
    private int trackIdToDeleteOrder;
    private int getCourierId;
    @Before
    public void setUpBaseUriAndTakeOrder() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        String jsonToCreateCourier = "{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}";
        Response responseToCreateCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
        responseToCreateCourier.then().assertThat().statusCode(201);
        Response responseToCreateOrder =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(new CreateOrderSerialization("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha"))
                        .when()
                        .post("/api/v1/orders");
        responseToCreateOrder.then().assertThat().body("track", notNullValue());
        trackIdToDeleteOrder = responseToCreateOrder.jsonPath().getInt("track");
        Response responseToTakeOrderId =
                given()
                        .get("/api/v1/orders/track?t=" + trackIdToDeleteOrder);
        int orderId = responseToTakeOrderId.jsonPath().getInt("order.id");
        String jsonToLoginCourier = "{\"login\": \"ajnin\", \"password\": \"1234\"}";
        DeleteCourierDeserialization deleteCourierDeserialization =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToLoginCourier)
                        .when()
                        .post("/api/v1/courier/login")
                        .body()
                        .as(DeleteCourierDeserialization.class);
        getCourierId = deleteCourierDeserialization.getId();
        Response responseToAcceptOrder =
                given()
                        .put("/api/v1/orders/accept/" + orderId + "?courierId=" + getCourierId);
        responseToAcceptOrder.then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("The response body returns a list of orders")
    public void getListOfOrders(){
        OrderListDeserialization orderListDeserialization =
                given()
                        .when()
                        .get("/api/v1/orders?courierId=" + getCourierId)
                        .body()
                        .as(OrderListDeserialization.class);
        Assert.assertNotNull(orderListDeserialization.getOrders());
    }
    @After
    public void orderCancel(){
        Response responseToCancelOrder =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .param("track", trackIdToDeleteOrder)
                        .when()
                        .put("/api/v1/orders/cancel");
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
