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

    /** Verifica se o menu de crafting está aberto. */
    public boolean isOpen() {
        return open;
    }

    @Override
    protected void initialize(Application app) {
    }

    @Override
    public void update(float tpf) {
        // Apenas processar abertura/fecho do menu (tecla C)
        if (input.consumeCraftRequested()) {
            open = !open;
            if (open)
                hud.showStatus(listRecipes(), 3.5f);
            else
                hud.showStatus("", 0f);
        }
        // NÃO consumir teclas 1-4 aqui - delegado ao InteractionAppState
    }

    /**
     * Tenta fabricar a receita no índice especificado. Chamado pelo
     * InteractionAppState.
     */
    public void tryCraftByIndex(int idx) {
        if (!open)
            return; // Só fabricar se menu estiver aberto
        tryCraft(idx);
    }

    private String listRecipes() {
        Player p = playerAppState != null ? playerAppState.getPlayer() : null;
        if (p == null)
            return "Crafting: (No player)";

        StackingInventory inv = p.getInventory();
        java.util.List<Recipe> all = RecipeBook.getAll();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < all.size(); i++) {
            Recipe r = all.get(i);
            if (CraftingService.canCraft(inv, r)) {
                if (count > 0)
                    sb.append("  ");
                sb.append(i + 1).append(") ").append(r.getName());
                count++;
            }
        }
        if (count == 0)
            return "Crafting: (No resources)";
        return "Crafting: " + sb.toString();
    }

    private void tryCraft(int idx) {
        Player p = playerAppState != null ? playerAppState.getPlayer() : null;
        if (p == null)
            return;
        StackingInventory inv = p.getInventory();
        Recipe r = RecipeBook.get(idx);
        boolean ok = CraftingService.craft(inv, r);
        if (ok)
            hud.showStatus("Crafted " + r.getName() + " x" + r.getOutputQty(), 2.0f);
        else
            hud.showStatus("Cannot craft " + r.getName(), 2.0f);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
}
