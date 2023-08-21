package order;

import io.qameta.allure.junit4.DisplayName;
import org.example.order.OrderClient;
import org.example.order.OrderRequest;
import org.junit.Assert;
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
    private OrderRequest orderRequest;
    String accessToken;

    @Test
    @DisplayName("Создание заказа авторизированным пользователем и инградиентами")
    public void createOrderUsersWithToken(){
        client = new UserClient();
        orderRequest = new OrderRequest();
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var createOrderUsersWithTokenResponse = createOrderWithIngredients();
        var requestCreateOrderSuccess =  orderClient.createOrder(createOrderUsersWithTokenResponse, accessToken);
        int StatusCodeCreateOrderSuccess = requestCreateOrderSuccess.extract().statusCode();
        Assert.assertEquals(SC_OK, StatusCodeCreateOrderSuccess);
        int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
        Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился

    }
    @Test
    @DisplayName("Создание заказа неавторизированным пользователем с инградиентами")
    public void createOrderUsersWithoutToken(){
        client = new UserClient();
        orderRequest = new OrderRequest();
        var createOrderUsersWithOutTokenResponse = createOrderWithIngredients();
        var requestCreateOrderUsersWithOutToken = orderClient.createOrderWithoutTokenOrIngredients(createOrderUsersWithOutTokenResponse);
        int statusCodeRequestCreateOrderUsersWithOutToken = requestCreateOrderUsersWithOutToken.extract().statusCode();
        System.out.println(statusCodeRequestCreateOrderUsersWithOutToken);
        Assert.assertEquals(SC_BAD_REQUEST, statusCodeRequestCreateOrderUsersWithOutToken);
    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем, но без инградиентов")
    public void createOrderUsersWithoutIngredients(){
        client = new UserClient();
        orderRequest = new OrderRequest();
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var createOrderUsersWithIn = createOrderWithoutIngredients();
        var responseCreateOrderUsersWithIn = orderClient.createOrderWithoutTokenOrIngredients(createOrderUsersWithIn);
        int statusCode = responseCreateOrderUsersWithIn.extract().statusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCode);
        client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя

    }

    @Test
    @DisplayName("Создание заказа авторизированным пользователем, но с инградиентами, которых нет")
    public void createOrderUsersWithWrongIngredients(){
        client = new UserClient();
        orderRequest = new OrderRequest();
        user = generator.random();
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        var createOrderUsersWrongIngredients = createOrderWithWrongIngredients();
        var requestCreateWithWrongIngredients = orderClient.createOrderWithoutTokenOrIngredients(createOrderUsersWrongIngredients);
        int statusCode = requestCreateWithWrongIngredients.extract().statusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCode);
        client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
    }

}
