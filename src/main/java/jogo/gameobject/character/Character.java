package jogo.gameobject.character;

import jogo.gameobject.GameObject;

/**
 * Tipo base de personagem neutro em relação ao motor.
 * Guarda estado lógico da personagem (ex.: saúde) enquanto os AppStates do motor
 * tratam movimento/física e câmara.
 */
public abstract class Character extends GameObject {

    /** Constrói uma personagem com um nome de apresentação. */
    protected Character(String name) {
        super(name);
    }

    // Example state hooks students can extend
    private int health = 100;

    /** Valor atual de saúde. */
    public int getHealth() { return health; }
    /** Define o valor de saúde. */
    public void setHealth(int health) { this.health = health; }
}
