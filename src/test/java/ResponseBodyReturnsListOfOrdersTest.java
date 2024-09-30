import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.pojo.CreateOrderSerialization;

import static org.hamcrest.Matchers.notNullValue;

public class ResponseBodyReturnsListOfOrdersTest {
    private int trackId;
    private int courierId;
    private int orderId;
    private static Courier courier;
    @Before
    public void setUpBaseUriAndTakeOrder() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier();
        Response responseToCreateCourier = courier.createCourier("{\"login\": \"ajnin\", \"password\": \"1234\", \"firstName\": \"saske\"}");
        responseToCreateCourier.
                then().
                assertThat().
                statusCode(201);
        Response responseToCreateOrder = courier.createOrder(new CreateOrderSerialization("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha"));
        responseToCreateOrder.
                then().
                assertThat().
                body("track", notNullValue());
        trackId = responseToCreateOrder.jsonPath().getInt("track");
        orderId = courier.takeOrderId(trackId);
        Response getCourierId = courier.loginCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
        courierId = getCourierId.jsonPath().getInt("id");
        Response responseToAcceptOrder = courier.acceptOrder(orderId, courierId);
        responseToAcceptOrder.
                then().
                assertThat().
                statusCode(200);
    }
    @Test
    @DisplayName("The response body returns a list of orders")
    public void getListOfOrders(){
        Assert.assertNotNull(courier.listOfOrders(courierId).getOrders());
    }
    @After
    public void orderCancel(){
        courier.cancelOrder(trackId);
        courier.deleteCourier("{\"login\": \"ajnin\", \"password\": \"1234\"}");
    }
}
