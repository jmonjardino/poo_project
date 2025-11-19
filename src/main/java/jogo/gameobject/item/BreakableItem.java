package jogo.gameobject.item;

/**
 * Item do tipo ferramenta, quebrável, com durabilidade e atributos.
 *
 * Semântica da Semana 1: interagir (E) faz a recolha/registo; a durabilidade
 * é acompanhada mas não consumida aqui. A aplicação da ferramenta em voxels
 * será implementada mais tarde no caminho de interação de voxel.
 */
public class BreakableItem extends CollectibleItem {
    private boolean broken = false;
    private int durability;
    private final int maxDurability;
    private final String toolType;
    private final String tier;
    private final double efficiency;

    /**
     * Constrói um item quebrável.
     * @param name nome de apresentação
     * @param itemValue valor de jogo quando recolhido
     * @param maxDurability durabilidade inicial e máxima
     * @param toolType categoria da ferramenta (ex.: Axe, Pickaxe)
     * @param tier nível de material (ex.: Basic, Wood, Stone)
     * @param efficiency multiplicador de eficiência para ações futuras em voxel
     */
    public BreakableItem(String name, int itemValue, int maxDurability, String toolType, String tier, double efficiency) {
        super(name, itemValue);
        this.maxDurability = maxDurability;
        this.toolType = toolType;
        this.tier = tier;
        this.efficiency = efficiency;
        this.durability = maxDurability;
    }

    /** Indica se a ferramenta está quebrada (durabilidade zero). */
    public boolean isBroken() { return broken; }
    /** Valor atual de durabilidade. */
    public int getDurability() { return durability; }
    /** Durabilidade máxima configurada. */
    public int getMaxDurability() { return maxDurability; }
    /** Categoria da ferramenta (ex.: Axe, Pickaxe). */
    public String getToolType() { return toolType; }
    /** Nível de material (ex.: Basic, Wood, Stone). */
    public String getTier() { return tier; }
    /** Multiplicador de eficiência para futuras ações em voxel. */
    public double getEfficiency() { return efficiency; }

    /**
     * Interação da Semana 1: recolha/registo quando apontado e ao pressionar E.
     * O uso da ferramenta e o consumo de durabilidade ocorrem no caminho de interação de voxel.
     */
    @Override
    public void onInteract() {
        if (!isCollected()) {
            super.onInteract();
            System.out.println("Item recolhido: " + getName() + " (valor=" + itemValue + ")");
        } else {
            System.out.println(getName() + " [toolType=" + toolType + ", tier=" + tier + "] durabilidade=" + durability + "/" + maxDurability + (broken ? " (quebrada)" : ""));
        }
    }

    /**
     * Texto de resumo amigável para depuração.
     */
    @Override
    public String toString() {
        return "BreakableItem{name=" + getName() + ", value=" + itemValue + ", collected=" + collected + ", broken=" + broken + ", durability=" + durability + ", maxDurability=" + maxDurability + ", toolType=" + toolType + ", tier=" + tier + ", efficiency=" + efficiency + "}";
    }
}
