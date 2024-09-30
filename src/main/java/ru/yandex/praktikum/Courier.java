package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.yandex.praktikum.pojo.CreateOrderSerialization;
import ru.yandex.praktikum.pojo.DeleteCourierDeserialization;
import ru.yandex.praktikum.pojo.OrderListDeserialization;

import static io.restassured.RestAssured.given;

public class Courier {
    @Step("Send POST request to create courier to /api/v1/courier")
    public Response createCourier(String jsonToCreateCourier){
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToCreateCourier)
                        .when()
                        .post("/api/v1/courier");
    }
    @Step("Send POST request to login courier to /api/v1/courier/login")
    public Response loginCourier(String jsonToLoginByCreatedCourier){
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonToLoginByCreatedCourier)
                        .when()
                        .post("/api/v1/courier/login");
    }
    @Step("Send DELETE request with courier id to delete courier to /api/v1/courier/")
    public void deleteCourier(String jsonToDeleteCourier){
        DeleteCourierDeserialization deleteCourierDeserialization =
        given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonToDeleteCourier)
                .when()
                .post("/api/v1/courier/login")
                .body()
                .as(DeleteCourierDeserialization.class);
        while (true) {
            Response response =
                    given()
                            .delete("/api/v1/courier/" + (deleteCourierDeserialization.getId()));
            if (response.getStatusCode() != 200) {
                break;
            }
        }
    }
    @Step("Send POST request to create order to /api/v1/orders")
    public Response createOrder(CreateOrderSerialization createOrderSerialization){
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createOrderSerialization)
                        .when()
                        .post("/api/v1/orders");
    }
    @Step("Send PUT request to cancel order to /api/v1/orders/cancel")
    public Response cancelOrder(int trackId){
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .param("track", trackId)
                        .when()
                        .put("/api/v1/orders/cancel");
    }
    @Step("Send GET request to order ID to /api/v1/orders")
    public int takeOrderId(int trackId){
        Response responseToTakeOrderId = given()
                .get("/api/v1/orders/track?t=" + trackId);
        return responseToTakeOrderId.jsonPath().getInt("order.id");
    }
    @Step("Send PUT request to accept order to /api/v1/orders/accept")
    public Response acceptOrder(int orderId, int courierId){
        return given()
                .put("/api/v1/orders/accept/" + orderId + "?courierId=" + courierId);
    }
    @Step("Send GET request to return list of orders to /api/v1/orders")
    public OrderListDeserialization listOfOrders(int getCourierId){
        OrderListDeserialization orderListDeserialization =
                given()
                        .when()
                        .get("/api/v1/orders?courierId=" + getCourierId)
                        .body()
                        .as(OrderListDeserialization.class);
        return orderListDeserialization;
    }
}
