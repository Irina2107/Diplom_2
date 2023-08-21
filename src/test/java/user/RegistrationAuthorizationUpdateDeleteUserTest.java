package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;

public class RegistrationAuthorizationUpdateDeleteUserTest {
    private final UserGenerator generator = new UserGenerator();
    private User user;
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
    @DisplayName("Пользователь может авторизоваться")
    public void logInUserTest(){
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
//String body = logInResponseLn.extract().body().asString();
        String accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
        Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился
    }
    @Test
    @DisplayName("Обновление данных о пользователе")
    public void updateUserDataTest(){
        client.createUser(user);
        String accessToken = client.logIn(Credentials.from(user)).extract().path("accessToken");
        client.editUser("accessToken", user).extract().statusCode();
        Assert.assertNotNull(accessToken);
        int statusCode  = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
        Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился
    }
    @Test
    @DisplayName("Успешное удаление авторизованного пользователя")
    public void deleteUserTest() {
        client.createUser(user);
        String accessToken = client.logIn(Credentials.from(user)).extract().path("accessToken");
        int statusCode  = client.deleteUser(accessToken).extract().statusCode();
        Assert.assertEquals(SC_ACCEPTED, statusCode);
    }
}
