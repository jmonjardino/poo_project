package jogo.crafting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RecipeBook {
    private static final List<Recipe> RECIPES = new ArrayList<>();
    static {
        Map<Integer, Integer> r1 = new LinkedHashMap<>();
        r1.put(200, 2);
        RECIPES.add(new Recipe("Planks", r1, 210, 4));

        Map<Integer, Integer> r2 = new LinkedHashMap<>();
        r2.put(210, 4);
        RECIPES.add(new Recipe("Workbench", r2, 300, 1));
    }

    public static List<Recipe> getAll() { return RECIPES; }
    public static Recipe get(int index) { return RECIPES.get(index); }
}
