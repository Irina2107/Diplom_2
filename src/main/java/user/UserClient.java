package user;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.Client;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String USER_API = "/auth";

    @Step("Создание (регистрация) пользователя")
    public ValidatableResponse createUser(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(USER_API + "/register") //Create
                .then();
    }
    @Step("Успешный вход в систему (авторизация) пользователя")
    public ValidatableResponse logIn(Credentials credentials) {
        return getSpec()
                .body(credentials)
                .when()
                .post(USER_API + "/login") //log in
                // .then().extract().path("accessToken");
                .then().log().all();
    }
    @Step("Обновление информации о пользователе")
    public ValidatableResponse editUser(String accessToken, User user){
        return getSpec()
                .headers("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_API + "/user")
                .then().log().all();
    }
   @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken){
     return   given()
             .spec(getSpec())
                .headers("Authorization", accessToken)
                .when().log().everything()
                .delete(USER_API + "/user")
                .then().log().all();

    }
}
