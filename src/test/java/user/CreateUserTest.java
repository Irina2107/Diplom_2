package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest {
    private final UserGenerator generator = new UserGenerator();
    private User user = new User();
    private UserClient client;

    @Before
    @DisplayName("Создание пользователя со случайным name")
    public void setUp(){
        client = new UserClient();
        user = generator.random();
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void registrationUserTest(){
        var createResponseCr = client.createUser(user);
        int statusCodeCr = createResponseCr.extract().statusCode();
        String accessToken = createResponseCr.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        Assert.assertEquals(SC_OK, statusCodeCr);

        int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
        Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился

    }
    @Test
    @DisplayName("Создание пользователя с существующим логином")
    public void canNotCreateTwoSameUser(){
        client.createUser(user);
        ValidatableResponse courierResponse = client.createUser(user);
        int statusCodeSameCourier = courierResponse.extract().statusCode(); //получение кода ответа
        Assert.assertEquals(SC_FORBIDDEN, statusCodeSameCourier);
        if (statusCodeSameCourier == 201) {
            String accessToken = courierResponse.extract().path("accessToken");
            Assert.assertNotNull(accessToken);
            Assert.assertEquals(SC_OK, statusCodeSameCourier);
            int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
            Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился

        }
    }
    @Test
    @DisplayName("Создание пользователя с пустым логином")
    public void canNotCreateCourierNoLogin() {
        User user = new User("", "T&^&^*9987!", "Test22");
        ValidatableResponse courierResponse = client.createUser(user);
        int statusCodeNoLogin = courierResponse.extract().statusCode(); //получение кода ответа
        Assert.assertEquals(SC_FORBIDDEN, statusCodeNoLogin);
        if (statusCodeNoLogin == 201) {
            String accessToken = courierResponse.extract().path("accessToken");
            Assert.assertNotNull(accessToken);
            Assert.assertEquals(SC_OK, statusCodeNoLogin);
            int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
            Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился
        }
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void canNotCreateCourierWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse courierResponse = client.createUser(user);
        int statusCodeWithoutPass = courierResponse.extract().statusCode(); //получение кода ответа
        System.out.println(statusCodeWithoutPass);
        Assert.assertEquals(SC_FORBIDDEN, statusCodeWithoutPass);
        if (statusCodeWithoutPass == 201) {
            String accessToken = courierResponse.extract().path("accessToken");
            Assert.assertNotNull(accessToken);
            Assert.assertEquals(SC_OK, statusCodeWithoutPass);
            int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
            Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился
        }
    }
}
