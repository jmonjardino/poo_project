package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import jogo.crafting.CraftingService;
import jogo.crafting.Recipe;
import jogo.crafting.RecipeBook;
import jogo.gameobject.StackingInventory;
import jogo.gameobject.character.Player;

public class CraftingAppState extends BaseAppState {
    private final InputAppState input;
    private final PlayerAppState playerAppState;
    private final HudAppState hud;
    private boolean open = false;

    public CraftingAppState(InputAppState input, PlayerAppState playerAppState, HudAppState hud) {
        this.input = input;
        this.playerAppState = playerAppState;
        this.hud = hud;
    }

    @Override
    protected void initialize(Application app) { }

    @Override
    public void update(float tpf) {
        if (input.consumeCraftRequested()) {
            open = !open;
            if (open) hud.showStatus(listRecipes(), 3.5f);
            else hud.showStatus("", 0f);
        }
        if (!open) return;
        if (input.consumeCraftRecipe1Requested()) tryCraft(0);
        if (input.consumeCraftRecipe2Requested()) tryCraft(1);
    }

    private String listRecipes() {
        java.util.List<Recipe> all = RecipeBook.getAll();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < all.size(); i++) {
            Recipe r = all.get(i);
            sb.append(i + 1).append(") ").append(r.getName());
            if (i < all.size() - 1) sb.append("  ");
        }
        return "Crafting: " + sb.toString();
    }

    private void tryCraft(int idx) {
        Player p = playerAppState != null ? playerAppState.getPlayer() : null;
        if (p == null) return;
        StackingInventory inv = p.getInventory();
        Recipe r = RecipeBook.get(idx);
        boolean ok = CraftingService.craft(inv, r);
        if (ok) hud.showStatus("Crafted " + r.getName() + " x" + r.getOutputQty(), 2.0f);
        else hud.showStatus("Cannot craft " + r.getName(), 2.0f);
    }

    @Override
    protected void cleanup(Application app) { }
    @Override
    protected void onEnable() { }
    @Override
    protected void onDisable() { }
}
