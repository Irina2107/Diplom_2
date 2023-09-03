package user;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;

public class RegistrationAuthorizationUpdateDeleteUserTest {
    private final UserGenerator generator = new UserGenerator();
    private User user;
    private UserClient client;
    String accessToken;

    @Before
    @DisplayName("Создание пользователя со случайным name")
    public void setUp(){
        client = new UserClient();
        user = generator.random();
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            int statusCode = client.deleteUser(accessToken).extract().statusCode(); //удалить пользователя
            Assert.assertEquals(SC_ACCEPTED, statusCode); //проверить, что пользователь успешно удалился
        }
    }
    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void registrationUserTest(){
        var createResponseCr = client.createUser(user);
        int statusCodeCr = createResponseCr.extract().statusCode();
        accessToken = createResponseCr.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        Assert.assertEquals(SC_OK, statusCodeCr);
    }
    @Test
    @DisplayName("Пользователь может авторизоваться")
    public void logInUserTest(){
        client.createUser(user);
        var logInResponseLn = client.logIn(Credentials.from(user));
        accessToken = logInResponseLn.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
    }
    @Test
    @DisplayName("Обновление данных о пользователе")
    public void updateUserDataTest(){
        client.createUser(user);
        accessToken = client.logIn(Credentials.from(user)).extract().path("accessToken");
        client.editUser("accessToken", user).extract().statusCode();
        Assert.assertNotNull(accessToken);
    }
    @Test
    @DisplayName("Успешное удаление авторизованного пользователя")
    public void deleteUserTest() {
        client.createUser(user);
        accessToken = client.logIn(Credentials.from(user)).extract().path("accessToken");
    }
}
