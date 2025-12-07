package jogo.crafting;

import jogo.gameobject.StackingInventory;

import java.util.Map;

public final class CraftingService {
    public static boolean canCraft(StackingInventory inv, Recipe recipe) {
        for (Map.Entry<Integer, Integer> e : recipe.getInputs().entrySet()) {
            int have = inv.getCount(e.getKey());
            if (have < e.getValue())
                return false;
        }
        for (Map.Entry<Integer, Integer> e : recipe.getRequirements().entrySet()) {
            int have = inv.getCount(e.getKey());
            if (have < e.getValue())
                return false;
        }
        if (!inv.hasSpaceFor(recipe.getOutputType(), recipe.getOutputQty()))
            return false;
        return true;
    }

    public static boolean craft(StackingInventory inv, Recipe recipe) {
        if (!canCraft(inv, recipe))
            return false;
        java.util.Map<Integer, Integer> removed = new java.util.LinkedHashMap<>();
        try {
            for (Map.Entry<Integer, Integer> e : recipe.getInputs().entrySet()) {
                inv.remove(e.getKey(), e.getValue());
                removed.put(e.getKey(), e.getValue());
            }
            inv.add(recipe.getOutputType(), recipe.getOutputQty());
            return true;
        } catch (RuntimeException ex) {
            for (Map.Entry<Integer, Integer> e : removed.entrySet()) {
                inv.add(e.getKey(), e.getValue());
            }
            return false;
        }
    }
}
