package org.example.order;
import java.util.List;
public class Orders {
    private List<String> ingredients;
    String accessToken;

    public Orders() {
    }
    public Orders(List<String> ingredients, String accessToken) {
        this.ingredients = ingredients;
        this.accessToken = accessToken;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
