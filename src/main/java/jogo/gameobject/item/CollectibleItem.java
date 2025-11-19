package jogo.gameobject.item;

/**
 * Item colecionável simples com um valor de jogo.
 *
 * Quando é interagido, o item marca-se como recolhido e regista uma mensagem.
 * Esta classe mantém-se neutra em relação ao motor; a renderização e o
 * encaminhamento de interações são fornecidos pelos AppStates do motor.
 */
public class CollectibleItem extends Item{
    /**
     * Valor de jogo (ex.: pontuação/moeda) associado ao colecionável.
     */
    public final int itemValue;
    /**
     * Indica se o item já foi recolhido (interação idempotente).
     */
    public boolean collected;

    /**
     * Constrói um item colecionável.
     * @param name nome de apresentação
     * @param itemValue valor de jogo atribuído quando recolhido
     */
    public CollectibleItem(String name, int itemValue) {
        super(name);
        this.itemValue = itemValue;
        this.collected = false;
    }

    /**
     * Devolve o valor de jogo do colecionável.
     */
    public int getValue () {
        return itemValue;
    }

    /**
     * Indica se este item já foi recolhido.
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Interage com o item marcando-o como recolhido e registando.
     * Interações subsequentes são sem efeito e apenas informativas.
     */
    @Override
    public void onInteract() {
        if (collected) {
            System.out.println("Item já recolhido");
            return;
        }

        System.out.println("Item recolhido: " + getName());
        collected = true;
    }

    /**
     * Representação amigável para depuração deste colecionável.
     */
    @Override
    public String toString() {
        return "CollectibleItem{name=" + getName() + ", value=" + itemValue + ", collected=" + collected + "}";
    }
}
