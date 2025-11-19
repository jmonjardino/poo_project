package jogo.gameobject;

import jogo.gameobject.item.CollectibleItem;

public class Wood extends CollectibleItem{
    public String woodType;

    public Wood(String name, int itemValue, String woodType) {
        super(name, itemValue);
        this.woodType = woodType;
    }
        
    @Override
    public void onInteract() {
        System.out.println("Interacting with " + getName() + " of type " + woodType);
    }
}
