package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.github.javafaker.Faker;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest extends UserClient{
    private final UserGenerator generator = new UserGenerator();
    private ValidatableResponse createResponse;


    private User user = new User();
    Faker faker = new Faker();
    String name = faker.funnyName().name();
    String password = faker.internet().password();
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
        var createResponse = client.createUser(user);
        int statusCodeCr = createResponse.extract().statusCode();
        accessToken = createResponse.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        Assert.assertEquals(SC_OK, statusCodeCr);
     }
    @Test
    @DisplayName("Создание пользователя с существующим логином")
    public void canNotCreateTwoSameUser(){
        client.createUser(user);
        ValidatableResponse courierResponse = client.createUser(user);
        int statusCodeSameCourier = courierResponse.extract().statusCode(); //получение кода ответа
        Assert.assertEquals(SC_FORBIDDEN, statusCodeSameCourier);
        if (statusCodeSameCourier == 201) {
        accessToken = courierResponse.extract().path("accessToken");
        Assert.assertNotNull(accessToken);
        Assert.assertEquals(SC_OK, statusCodeSameCourier);
         }
    }
    @Test
    @DisplayName("Создание пользователя с пустым логином")
    public void canNotCreateCourierNoLogin() {
        User user = new User("", password, name);
        ValidatableResponse courierResponse = client.createUser(user);
        int statusCodeNoLogin = courierResponse.extract().statusCode(); //получение кода ответа
        Assert.assertEquals(SC_FORBIDDEN, statusCodeNoLogin);
        if (statusCodeNoLogin == 201) {
            accessToken = courierResponse.extract().path("accessToken");
            Assert.assertNotNull(accessToken);
            Assert.assertEquals(SC_OK, statusCodeNoLogin);
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
            accessToken = courierResponse.extract().path("accessToken");
            Assert.assertNotNull(accessToken);
            Assert.assertEquals(SC_OK, statusCodeWithoutPass);
        }
    }
}
