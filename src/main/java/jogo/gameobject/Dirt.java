package jogo.gameobject;

import jogo.gameobject.item.CollectibleItem;

/**
 * Item de terra simples, colecionável.
 */
public class Dirt extends CollectibleItem {

    /**
     * Constrói um item de terra.
     * 
     * @param name      nome de apresentação
     * @param itemValue valor de jogo quando recolhido
     */
    public Dirt(String name, int itemValue) {
        super(name, itemValue);
    }

    @Override
    public void onInteract() {
        System.out.println("Interacting with " + getName());
    }
}
