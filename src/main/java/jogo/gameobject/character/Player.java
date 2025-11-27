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

    public StackingInventory getInventory() {
        return inventory;
    }
}
