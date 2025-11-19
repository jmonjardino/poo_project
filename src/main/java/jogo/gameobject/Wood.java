package jogo.gameobject;

import jogo.gameobject.item.CollectibleItem;

/**
 * Item de madeira simples, colecionável, com uma etiqueta de tipo de madeira.
 */
public class Wood extends CollectibleItem{
    /** Classificação de madeira (ex.: Carvalho, Bétula). */
    public String woodType;

    /**
     * Constrói um item de madeira.
     * @param name nome de apresentação
     * @param itemValue valor de jogo quando recolhido
     * @param woodType etiqueta de classificação de madeira
     */
    public Wood(String name, int itemValue, String woodType) {
        super(name, itemValue);
        this.woodType = woodType;
    }
        
    /**
     * Regista uma mensagem na interação.
     * Nota: não chama o super; o comportamento de recolha pode ser personalizado.
     */
    @Override
    public void onInteract() {
        System.out.println("Interacting with " + getName() + " of type " + woodType);
    }
}
