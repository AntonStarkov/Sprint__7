import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ru.yandex.praktikum.CreateOrderSerialization;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrderCreateWithParametersTest {
    private final CreateOrderSerialization createOrderSerialization;
    private final int expectedStatusCode;
    private String deleteOrder;
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
    }
    @Test
    @DisplayName("Create an order with a choice of different scooter colors")
    public void testCreateOrder(){
        Response responseToCreateOrder =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createOrderSerialization)
                        .when()
                        .post("/api/v1/orders");
        responseToCreateOrder.then().assertThat().statusCode(expectedStatusCode);
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
