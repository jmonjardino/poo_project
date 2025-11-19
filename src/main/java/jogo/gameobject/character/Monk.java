package jogo.gameobject.character;

import jogo.gameobject.capability.Guardian;

public class Monk extends Ally implements Guardian {
    private float guardRadius;

    /**
     * Constrói um guardião de Monk com um raio de guarda especificado.
     * 
     * @param guardRadius raio de guarda (deve ser não negativo)
     */
    public Monk(String name, String role, int assistPower, int healPower, double protectionPower, float guardRadius) {
        super(name, role, assistPower, healPower, protectionPower);
        this.guardRadius = Math.max(0, guardRadius);
    }

    @Override
    public float getGuardRadius() {
        return guardRadius;
    }

    public void setGuardRadius(float guardRadius) {
        this.guardRadius = Math.max(0, guardRadius);
    }

    @Override
    public boolean isWithinGuardArea(double x, double y, double z) {
        double dx = x - getPosition().x;
        double dy = y - getPosition().y;
        double dz = z - getPosition().z;
        double distanceSquared = dx * dx + dy * dy + dz * dz;
        return distanceSquared <= guardRadius * guardRadius;
    }
}
