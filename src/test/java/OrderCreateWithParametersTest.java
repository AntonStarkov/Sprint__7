import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.pojo.CreateOrderSerialization;

@RunWith(Parameterized.class)
public class OrderCreateWithParametersTest {
    private final CreateOrderSerialization createOrderSerialization;
    private final int expectedStatusCode;
    private static int trackId;
    private static Courier courier;
    public OrderCreateWithParametersTest(CreateOrderSerialization createOrderSerialization, int expectedStatusCode){
        this.createOrderSerialization = createOrderSerialization;
        this.expectedStatusCode = expectedStatusCode;
    }
    @Parameterized.Parameters
    public static Object[][] getOrderStatus(){
        return new Object[][]{
                {new CreateOrderSerialization("Naruto2", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha"), 201},
                {new CreateOrderSerialization("Naruto2", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[] {"BLACK", "GREY"}), 201},
                {new CreateOrderSerialization("Naruto2", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[] {"GREY"}), 201},
                {new CreateOrderSerialization("Naruto2", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[] {"BLACK"}), 201}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier();

    }
    @Test
    @DisplayName("Create an order with a choice of different scooter colors")
    public void testCreateOrder(){
        Response responseToCreateOrder = courier.createOrder(createOrderSerialization);
        responseToCreateOrder.
                then().
                assertThat().
                statusCode(expectedStatusCode);
        trackId = responseToCreateOrder.jsonPath().getInt("track");
    }
    @After
    public void orderCancel(){
        courier.cancelOrder(trackId);
    }
}
