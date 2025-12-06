package jogo.gameobject.character;

import jogo.gameobject.capability.HasAI;
import jogo.gameobject.capability.AIContext;
import jogo.framework.math.Vec3;

/**
 * Entidade inimiga neutra em relação ao motor.
 * Mantém um atributo de força e expõe um dano de ataque simples.
 */
public class Enemy extends Character implements HasAI {
    /** Contexto de execução para a IA do inimigo. */

    /** Força base do inimigo usada para calcular dano de ataque. */
    private float strength;
    /** Poder de envenenamento base do inimigo usada para calcular dano de envenenamento. */
    private float poison;
    /** Distância máxima para seguir o jogador. */
    public float chaseRange = 15f;
    /** Distancia maxima para atacar o jogador */
    public float attackRange = 3f;
    /**Possíveis estados do inimigo */
    private enum enemyState {IDLE, CHASE, ATTACK};
    /** Estado atual do inimigo. */
    private enemyState state = enemyState.IDLE;
    

    /**
     * Constrói um inimigo com um nome de apresentação.
     * 
     * @param name nome de apresentação
     */
    public Enemy(String name, float strength, float poison) {
        super(name);
        this.strength = strength;
        this.poison = poison;
    }

    /** Devolve a força atual do inimigo. */
    public float getStrength() {
        return strength;
    }

    public float getPoison() {
        return poison;
    }

    /** Define a força do inimigo. Valores negativos são ajustados para 0. */
    public void setStrength(float strength) {
        this.strength = Math.max(0, strength);
    }

    public void setPoison(float poison) {
        this.poison = Math.max(0, poison);
    }

    /** Dano de ataque baseado na força atual. */
    public float attackDamage() {
        return strength;
    }

    @Override
    public void updateAI(AIContext context) {
        double dist = context.distanceXZ(getPosition());

        if (dist <= attackRange) {
            state = enemyState.ATTACK;
        } else if (dist <= chaseRange) {
            state = enemyState.CHASE;
            Vec3 step = context.computeStepTowardsPlayerXZ(getPosition(), 1.5);
            setPosition(getPosition().x + step.x, getPosition().y + step.y, getPosition().z + step.z);
        } else if (dist > chaseRange) {
            state = enemyState.IDLE;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " Strength: " + strength;
    }
}
