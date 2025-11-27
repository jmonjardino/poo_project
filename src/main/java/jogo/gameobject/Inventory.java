package jogo.gameobject;

public class Inventory {
    private int[] slots;
    private int size;

    public Inventory(int size) {
        this.size = size;
        slots = new int[size];
    }

    public int getSize() {
        return size;
    }

    public int getSlot(int index) {
        return slots[index];
    }

    public void setSlot(int index, int item) {
        slots[index] = item;
    }

    public void addItem(int item) {
        for (int i = 0; i < size; i++) {
            if (slots[i] == 0) {
                slots[i] = item;
                return;
            }
        }
    }

    public void removeItem(int item) {
        for (int i = 0; i < size; i++) {
            if (slots[i] == item) {
                slots[i] = 0;
                return;
            }
        }
    }

    
}
