import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.pojo.CreateOrderSerialization;

import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateHaveTrackTest {
    private static int trackId;
    private static Courier courier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier();
    }
    @Test
    @DisplayName("When an order is created, its track is returned")
    public void createOrderReturnTrack(){
        Response responseToCreateOrder = courier.createOrder(new CreateOrderSerialization("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha"));
        responseToCreateOrder.
                then().
                assertThat().
                body("track", notNullValue());
        trackId = responseToCreateOrder.jsonPath().getInt("track");
    }
    @After
    public void cancelCreatedOrder(){
        courier.cancelOrder(trackId);
    }
}
