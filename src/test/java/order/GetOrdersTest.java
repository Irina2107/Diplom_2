package order;
import io.qameta.allure.junit4.DisplayName;
import org.example.order.OrderClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import user.Credentials;
import user.User;
import user.UserClient;
import user.UserGenerator;

import static org.apache.http.HttpStatus.*;

public class GetOrdersTest {
    private OrderClient orderClient = new OrderClient();
    private final UserGenerator generator = new UserGenerator();

    private UserClient client;
    String accessToken;

    @After
    public void cleanUp() {
        if (accessToken !=null) {
            client.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение списка последних 50-ти заказов")
    public void getAllOrdersTest() {
        var getAllOrdersResponse = orderClient.getOrderList();
        int StatusCodeAllOrders = getAllOrdersResponse.extract().statusCode();
        Assert.assertEquals(SC_OK,StatusCodeAllOrders);
    }
    @Test
    @DisplayName("Получение списка заказов авторизированным пользователем")
    public void getUserOrderListWithToken(){
        User user;
        client = new UserClient();
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var getUserOrderListWithTokenResponse = orderClient.getUserOrderList(accessToken);
        int StatusCodeOrderListWithToken = getUserOrderListWithTokenResponse.extract().statusCode();
        Assert.assertEquals(SC_OK, StatusCodeOrderListWithToken);
    }
    @Test
    @DisplayName("Получение всех инградиенов")
    public void getAllIngredientTest() {
        var getAllOrdersResponse = orderClient.getIngredientList();
        int StatusCodeAllIng = getAllOrdersResponse.extract().statusCode();
        Assert.assertEquals(SC_OK, StatusCodeAllIng);
    }
    @Test
    @DisplayName("Получение списка заказов неавторизированным пользователем")
    public void getUserOrderListWithoutToken(){
        var getUserOrderListWithoutTokenResponse = orderClient.geNotAuthorizationUserOrderList();
        int StatusCodeOrderListWithoutToken = getUserOrderListWithoutTokenResponse.extract().statusCode();
        System.out.println(getUserOrderListWithoutTokenResponse);
        Assert.assertEquals(SC_UNAUTHORIZED, StatusCodeOrderListWithoutToken);
    }
}
