package jogo.gameobject.character;

import jogo.gameobject.GameObject;

/**
 * Tipo base de personagem neutro em relação ao motor.
 * Guarda estado lógico da personagem (ex.: saúde) enquanto os AppStates do
 * motor
 * tratam movimento/física e câmara.
 */
public abstract class Character extends GameObject {

    /** Constrói uma personagem com um nome de apresentação. */
    protected Character(String name) {
        super(name);
    }

    // Ganchos de estado que os estudantes podem estender
    /** Valor atual de saúde. */
    private int health = 100;
    /** Valor máximo de saúde. */
    private final int maxHealth = 100;

    private boolean isAlive = true;

    /** Valor atual de saúde. */
    public int getHealth() {
        return health;
    }

    /**
     * Devolve o valor máximo de saúde configurado para a personagem.
     * Este valor define o limite superior aplicado em {@link #setHealth(int)}.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Define o valor de saúde, garantindo as invariantes do estado.
     * O valor é cortado para o intervalo [0, {@link #getMaxHealth()}].
     * Valores negativos tornam a saúde 0; valores acima do máximo tornam a saúde
     * igual a {@link #getMaxHealth()}.
     * 
     * @param health novo valor de saúde pretendido
     */
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    /**
     * Indica se a personagem está viva.
     * 
     * @return {@code true} se a saúde atual for maior que 0; caso contrário
     *         {@code false}
     */
    public boolean isAlive() {
        return isAlive;
    }

    public void takeDamage(int amount) {
        if (amount > 0) {
            setHealth(health - amount);
        }
        if (health <= 0) {
            isAlive = false;
        }
    }

    public void heal(int amount) {
        if (amount > 0) {
            setHealth(health + amount);
        }
        if (health > 0) {
            isAlive = true;
        }
    }
}
