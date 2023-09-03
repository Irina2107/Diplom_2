package org.example.order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
public class OrderClient {

    private static final String ORDER_CREATE = "https://stellarburgers.nomoreparties.site/api/orders";
    //get - получить заказы пользователя
    //post - создание заказа
    private static final String INGREDIENT_DATA = " https://stellarburgers.nomoreparties.site/api/ingredients";
    //get - получение данных об инградиетах
    private static final String ORDER_ALL = "https://stellarburgers.nomoreparties.site/api/orders/all";
    //get - последние 50 заказов

    @Step("Создание ордера")
    public ValidatableResponse createOrder(OrderRequest orderRequest, String accessToken) {
        return  given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization",accessToken)
                .body(orderRequest)
                .when()
                .post(ORDER_CREATE)
                .then();
    }
    @Step("Создание ордера без accessToken")
    public ValidatableResponse createOrderWithoutTokenOrIngredients(OrderRequest orderRequest) {
        return given().log().all()
                .body(orderRequest)
                .when()
                .post(ORDER_CREATE)   // CREATE
                .then().log().all();
    }
    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given().log().all()
                .get(ORDER_ALL) //GET
                .then().log().all();
    }
    @Step("Получение списка заказов авторизированного пользователя")
    public ValidatableResponse getUserOrderList(String accessToken) {
        return given().log().all()
                .headers("Authorization", accessToken)
                .when()
                .get(ORDER_CREATE) //GET
                .then().log().all();
    }
    @Step ("Получение списка заказов, если пользователь не авторизирован")
    public ValidatableResponse geNotAuthorizationUserOrderList() {
        return given().log().all()
                .get(ORDER_CREATE) //GET
                .then().log().all();
    }
    @Step("Получение всех инградиентов")
    public ValidatableResponse getIngredientList() {
        return given().log().all()
                .get(INGREDIENT_DATA) //GET
                .then().log().all();
    }

    @Step("Получение id булочки")
    public String getIngredientBun() {
        String bunId = given().log().all()
                .get(INGREDIENT_DATA) //GET
                .then().extract().body().path("data[0]._id");
        return bunId;
    }
    @Step("Получение id начинки")
    public String getIngredientFilling() {
        String fillingId = given().log().all()
                .get(INGREDIENT_DATA) //GET
                .then().extract().body().path("data[2]._id");
        return fillingId;
    }
    @Step("Получение id соуса")
    public String getIngredientSouse() {
        String souseId = given().log().all()
                .get(INGREDIENT_DATA) //GET
                .then().extract().body().path("data[4]._id");
        return souseId;
    }

}
