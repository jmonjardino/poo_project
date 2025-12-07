package jogo.gameobject.character;

import jogo.gameobject.StackingInventory;

/**
 * Entidade de jogador neutra em relação ao motor, que suporta o controlador do
 * jogador.
 * Aspetos visuais, física e input são geridos pelo PlayerAppState.
 */
public class Player extends Character {
    private final StackingInventory inventory;

    /** Constrói o jogador com um nome de apresentação por omissão. */
    public Player() {
        super("Player");
        this.inventory = new StackingInventory(8, 64);
    }

    private int selectedSlot = 0;

    public StackingInventory getInventory() {
        return inventory;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < inventory.capacity()) {
            this.selectedSlot = slot;
        }
    }
}
