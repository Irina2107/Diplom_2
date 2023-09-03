package org.example.order;

import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;
import com.github.javafaker.Faker;
public class OrderRequest extends OrderClient{
    public List<String> ingredients;

    public OrderRequest() {
    }
    static OrderClient orderClient = new OrderClient();
    @Override
    public String getIngredientBun() {
        return super.getIngredientBun();
    }

    @Step("Создать бургер со всеми инградиентами")
    public static OrderRequest createOrderWithIngredients() {
        OrderRequest createSuccessfullyOrderRequest = new OrderRequest();
        createSuccessfullyOrderRequest.ingredients = new ArrayList<>();
        createSuccessfullyOrderRequest.ingredients.add(0, orderClient.getIngredientBun()); //булочка
        createSuccessfullyOrderRequest.ingredients.add(1, orderClient.getIngredientFilling()); //начинка
        createSuccessfullyOrderRequest.ingredients.add(2, orderClient.getIngredientSouse()); //соус
        return createSuccessfullyOrderRequest;
    }
    @Step("Создать бургер без инградиентов")
    public static OrderRequest createOrderWithoutIngredients() {
        OrderRequest createOrderWithoutIngredientsRequest = new OrderRequest();
        createOrderWithoutIngredientsRequest.ingredients = new ArrayList<>();
        return createOrderWithoutIngredientsRequest;
    }
    @Step("Создать бургер c инградиентами, которых нет")
    public static OrderRequest createOrderWithWrongIngredients() {
        OrderRequest createWithWrongIngredientsOrderRequest = new OrderRequest();
        createWithWrongIngredientsOrderRequest.ingredients = new ArrayList<>();
        Faker faker = new Faker();
        String randomId1 = faker.internet().uuid();
        String randomId2 = faker.internet().uuid();
        String randomId3 = faker.internet().uuid();
        createWithWrongIngredientsOrderRequest.ingredients.add(0, randomId1); //булочка
        createWithWrongIngredientsOrderRequest.ingredients.add(1, randomId2); //начинка
        createWithWrongIngredientsOrderRequest.ingredients.add(2, randomId3); //соус
        return createWithWrongIngredientsOrderRequest;
    }

}
