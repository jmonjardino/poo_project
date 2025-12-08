package jogo.persistence;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe de dados que contém todo o estado do jogo a ser persistido.
 * Inclui posição do jogador, inventário, saúde e seed do mundo.
 */
public class SaveData {

    /** Posição X do jogador no mundo. */
    private float playerX;

    /** Posição Y do jogador no mundo. */
    private float playerY;

    /** Posição Z do jogador no mundo. */
    private float playerZ;

    /** Saúde atual do jogador. */
    private int playerHealth;

    /** Mapa de itemType -> count representando o inventário. */
    private Map<Integer, Integer> inventory;

    /** Seed usado para geração do mundo (para regeneração determinística). */
    private long worldSeed;

    /** Slot atualmente selecionado no inventário. */
    private int selectedSlot;

    /** Construtor vazio para deserialização. */
    public SaveData() {
        this.inventory = new LinkedHashMap<>();
    }

    /** Construtor completo. */
    public SaveData(float playerX, float playerY, float playerZ, int playerHealth,
            Map<Integer, Integer> inventory, long worldSeed, int selectedSlot) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerZ = playerZ;
        this.playerHealth = playerHealth;
        this.inventory = inventory != null ? new LinkedHashMap<>(inventory) : new LinkedHashMap<>();
        this.worldSeed = worldSeed;
        this.selectedSlot = selectedSlot;
    }

    // Getters e Setters

    public float getPlayerX() {
        return playerX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }

    public float getPlayerZ() {
        return playerZ;
    }

    public void setPlayerZ(float playerZ) {
        this.playerZ = playerZ;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public Map<Integer, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<Integer, Integer> inventory) {
        this.inventory = inventory != null ? inventory : new LinkedHashMap<>();
    }

    public long getWorldSeed() {
        return worldSeed;
    }

    public void setWorldSeed(long worldSeed) {
        this.worldSeed = worldSeed;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    @Override
    public String toString() {
        return "SaveData{pos=(" + playerX + "," + playerY + "," + playerZ +
                "), health=" + playerHealth + ", inventory=" + inventory.size() +
                " items, seed=" + worldSeed + ", slot=" + selectedSlot + "}";
    }
}
