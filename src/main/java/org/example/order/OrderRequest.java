package org.example.order;

import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
public class OrderRequest {
    public List<String> ingredients;
    public OrderRequest() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Step("Создать бургер со всеми инградиентами")
    public static OrderRequest createOrderWithIngredients() {
        OrderRequest createSuccessfullyOrderRequest = new OrderRequest();
        createSuccessfullyOrderRequest.ingredients = new ArrayList<>();
        createSuccessfullyOrderRequest.ingredients.add(0,"61c0c5a71d1f82001bdaaa73"); //соус
        createSuccessfullyOrderRequest.ingredients.add(1,"61c0c5a71d1f82001bdaaa79"); //салат
        createSuccessfullyOrderRequest.ingredients.add(2,"61c0c5a71d1f82001bdaaa6d"); //булка
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
        // createTheOrderRequest.ingredients.add(0,"1234!asdd@#^%");
        createWithWrongIngredientsOrderRequest.ingredients.add(0,"61c0c5a71d1f8200k73"); //соус
        createWithWrongIngredientsOrderRequest.ingredients.add(1,"6100d1f82001bdaaa79"); //салат
        createWithWrongIngredientsOrderRequest.ingredients.add(2,"61c0c5a71dTnod"); //булка
        return createWithWrongIngredientsOrderRequest;
    }

}
