package order;

import io.qameta.allure.junit4.DisplayName;
import org.example.order.OrderClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.Credentials;
import user.User;
import user.UserClient;
import user.UserGenerator;


import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.example.order.OrderRequest.*;

public class CreateOrdersTest {
    private OrderClient orderClient = new OrderClient();
    private final UserGenerator generator = new UserGenerator();
    private User user;
    private UserClient client;
    String accessToken;

    @Before
    @DisplayName("Создание пользователя со случайным name")
    public void setUp(){
        client = new UserClient();
    }
    @After
       public void cleanUp() {
        if (accessToken != null) {
            int statusCode = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
            Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился
        }
    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем и инградиентами")
    public void createOrderUsersWithToken(){
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var createOrderUsersWithTokenResponse = createOrderWithIngredients(); // создать заказ
        var requestCreateOrderSuccess =  orderClient.createOrder(createOrderUsersWithTokenResponse, accessToken);
        int StatusCodeCreateOrderSuccess = requestCreateOrderSuccess.extract().statusCode();
        Assert.assertEquals(SC_OK, StatusCodeCreateOrderSuccess);
    }
    @Test
    @DisplayName("Создание заказа неавторизированным пользователем с инградиентами")
    public void createOrderUsersWithoutToken(){
        var createOrderUsersWithOutTokenResponse = createOrderWithIngredients();
        accessToken = null;
        var requestCreateOrderUsersWithOutToken = orderClient.createOrderWithoutTokenOrIngredients(createOrderUsersWithOutTokenResponse);
        int statusCodeRequestCreateOrderUsersWithOutToken = requestCreateOrderUsersWithOutToken.extract().statusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCodeRequestCreateOrderUsersWithOutToken);
    }
    @Test
    @DisplayName("Создание заказа авторизированным пользователем, но без инградиентов")
    public void createOrderUsersWithoutIngredients(){
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var createOrderUsersWithIn = createOrderWithoutIngredients();
        var responseCreateOrderUsersWithIn = orderClient.createOrderWithoutTokenOrIngredients(createOrderUsersWithIn);
        int statusCode = responseCreateOrderUsersWithIn.extract().statusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCode);
    }
    @Test
    @DisplayName("Создание заказа авторизированным пользователем, но с инградиентами, которых нет")
    public void createOrderUsersWithWrongIngredients(){
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var createOrderUsersWrongIngredients = createOrderWithWrongIngredients();
        var requestCreateWithWrongIngredients = orderClient.createOrderWithoutTokenOrIngredients(createOrderUsersWrongIngredients);
        int statusCode = requestCreateWithWrongIngredients.extract().statusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCode);
    }

}
