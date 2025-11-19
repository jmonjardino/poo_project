package jogo.gameobject.character;

/**
 * Entidade inimiga neutra em relação ao motor.
 * Mantém um atributo de força e expõe um dano de ataque simples.
 */
public class Enemy extends Character {
    /** Força base do inimigo usada para calcular dano de ataque. */
    private int strength = 10;

    /**
     * Constrói um inimigo com um nome de apresentação.
     * @param name nome de apresentação
     */
    public Enemy(String name) {
        super(name);
    }

    /**
     * Devolve a força atual do inimigo.
     */
    public int getStrength() { return strength; }
    /**
     * Define a força do inimigo.
     * Valores negativos são ajustados para 0.
     * @param strength nova força (não negativa)
     */
    public void setStrength(int strength) { this.strength = Math.max(0, strength); }
    /**
     * Dano de ataque baseado na força atual.
     * @return valor de dano
     */
    public int attackDamage() { return strength; }

    @Override
    public String toString() {
        return super.toString() + " Strength: " + strength;
    }
}
