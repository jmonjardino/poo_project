package jogo.gameobject.item;

public class CollectibleItem extends Item{
    public final int itemValue;
    public boolean collected;

    public CollectibleItem(String name, int itemValue) {
        super(name);
        this.itemValue = itemValue;
        this.collected = false;
    }

    public int getValue () {
        return itemValue;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public void onInteract() {
        if (collected) {
            System.out.println("Item already collected");
            return;
        }

        System.out.println("Collected item: " + getName());
        collected = true;
    }

    @Override
    public String toString() {
        return "CollectibleItem{name=" + getName() + ", value=" + itemValue + ", collected=" + collected + "}";
    }
}
    
// Optional (future): remove from registry so it disappears
// This requires access to GameRegistry, which is not injected here in Week 1.
