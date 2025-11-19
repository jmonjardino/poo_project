package jogo.gameobject.item;

import jogo.gameobject.GameObject;

/**
 * Base neutra em relação ao motor para itens interativos na camada de jogo.
 *
 * As instâncias de {@code Item} são renderizadas pelo motor e podem ser
 * alvo do raio de interação. Quando o jogador pressiona a tecla de interação (E),
 * o motor encaminha a chamada para {@link #onInteract()} do item atingido.
 *
 * A renderização e o encaminhamento de input são tratados pelos AppStates;
 * este tipo guarda apenas identidade/lógica e mantém-se neutro em relação ao motor.
 */
public abstract class Item extends GameObject {

    /**
     * Constrói um item neutro em relação ao motor com um nome de apresentação.
     * @param name nome de apresentação usado para identificação/registo
     */
    protected Item(String name) {
        super(name);
    }

    /**
     * Ponto de extensão invocado quando o jogador interage com este item via o motor.
     * Implementadores devem realizar lógica leve (ex.: apanhar/alternar)
     * e evitar chamadas diretas ao motor, mantendo a camada de jogo neutra.
     */
    public void onInteract() {
        // Sem efeito por omissão; subclasses implementam o comportamento específico
    }
}
