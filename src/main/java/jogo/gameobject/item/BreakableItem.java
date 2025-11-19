package jogo.gameobject.item;

public class BreakableItem extends CollectibleItem {
    private boolean broken = false;
    private int durability;
    private final int maxDurability;
    private final String toolType;
    private final String tier;
    private final double efficiency;

    public BreakableItem(String name, int itemValue, int maxDurability, String toolType, String tier, double efficiency) {
        super(name, itemValue);
        this.maxDurability = maxDurability;
        this.toolType = toolType;
        this.tier = tier;
        this.efficiency = efficiency;
        this.durability = maxDurability;
    }

    public boolean isBroken() { return broken; }
    public int getDurability() { return durability; }
    public int getMaxDurability() { return maxDurability; }
    public String getToolType() { return toolType; }
    public String getTier() { return tier; }
    public double getEfficiency() { return efficiency; }

    @Override
    public void onInteract() {
        if (!isCollected()) {
            super.onInteract();
            System.out.println("Collected " + getName() + " (value=" + itemValue + ")");
        } else {
            System.out.println(getName() + " [toolType=" + toolType + ", tier=" + tier + "] durability=" + durability + "/" + maxDurability + (broken ? " (broken)" : ""));
        }
    }

    @Override
    public String toString() {
        return "BreakableItem{name=" + getName() + ", value=" + itemValue + ", collected=" + collected + ", broken=" + broken + ", durability=" + durability + ", maxDurability=" + maxDurability + ", toolType=" + toolType + ", tier=" + tier + ", efficiency=" + efficiency + "}";
    }
}
