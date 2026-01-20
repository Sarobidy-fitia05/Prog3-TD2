import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class Dish {
    private Integer id;
    private Double price;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;
    private Map<Integer, Double> ingredientQuantities = new HashMap<>();
    private Map<Integer, String> ingredientUnits = new HashMap<>();

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDishCost() {
        double totalPrice = 0;
        if (ingredients == null) {
            return 0.0;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            Integer ingredientId = ingredient.getId();

           Double quantity = ingredientQuantities.get(ingredientId);
            if (quantity == null) {
                quantity = ingredient.getQuantity();
                if (quantity == null) {
                    continue;
                }
            }

            if (ingredient.getPrice() != null) {
                totalPrice = totalPrice + ingredient.getPrice() * quantity;
            }
        }
        return totalPrice;
    }

    public Dish() {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        if (ingredients == null) {
            this.ingredients = null;
            return;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).setDish(this);
        }
        this.ingredients = ingredients;
    }

    public Map<Integer, Double> getIngredientQuantities() {
        return ingredientQuantities;
    }

    public void setIngredientQuantities(Map<Integer, Double> ingredientQuantities) {
        this.ingredientQuantities = ingredientQuantities;
    }

    public Map<Integer, String> getIngredientUnits() {
        return ingredientUnits;
    }

    public void setIngredientUnits(Map<Integer, String> ingredientUnits) {
        this.ingredientUnits = ingredientUnits;
    }

    public void addIngredientQuantity(Integer ingredientId, Double quantity, String unit) {
        this.ingredientQuantities.put(ingredientId, quantity);
        this.ingredientUnits.put(ingredientId, unit);
    }

    public Double getIngredientQuantity(Integer ingredientId) {
        return ingredientQuantities.get(ingredientId);
    }

    public String getIngredientUnit(Integer ingredientId) {
        return ingredientUnits.get(ingredientId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, ingredients);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                '}';
    }


    public Double getGrossMargin() {
        if (price == null) {
            throw new RuntimeException("Price is null");
        }
        return price - getDishCost();
    }
}