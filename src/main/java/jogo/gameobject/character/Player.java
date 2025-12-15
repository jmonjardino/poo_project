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

    // === Sistema de Pontuação para Highscores ===

    /** Número de blocos minerados pelo jogador. */
    private int blocksMined = 0;

    /** Número de inimigos derrotados pelo jogador. */
    private int enemiesDefeated = 0;

    /** Incrementa o contador de blocos minerados. */
    public void incrementBlocksMined() {
        blocksMined++;
    }

    /** Incrementa o contador de inimigos derrotados. */
    public void incrementEnemiesDefeated() {
        enemiesDefeated++;
    }

    /** Devolve o número de blocos minerados. */
    public int getBlocksMined() {
        return blocksMined;
    }

    /** Devolve o número de inimigos derrotados. */
    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    /** Calcula e devolve a pontuação total. */
    public int getScore() {
        return blocksMined + (enemiesDefeated * 10);
    }

    /** Reinicia contadores de pontuação. */
    public void resetScore() {
        blocksMined = 0;
        enemiesDefeated = 0;
    }
}
