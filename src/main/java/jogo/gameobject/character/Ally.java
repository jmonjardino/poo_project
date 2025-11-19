package jogo.gameobject.character;

/**
 * Entidade aliada neutra em relação ao motor.
 * Suporta funções de assistência com atributos simples e validação.
 */
public class Ally extends Character {
    /** Papel/funcão do aliado (ex.: Guard, Medic, Scout). */
    private String role;
    /** Poder de proteção aplicado em contexto defensivo (não negativo). */
    private double protectionPower;
    /** Potência de assistência ofensiva (não negativa). */
    private int assistPower;
    /** Potência de cura por ação de suporte (não negativa). */
    private int healPower;

    /**
     * Constrói um aliado com papel e proteção.
     * @param name nome de apresentação
     * @param role função do aliado
     * @param protectionPower poder de proteção (não negativo)
     */
    public Ally(String name, String role, double protectionPower) {
        super(name);
        this.role = role;
        this.protectionPower = Math.max(0.0, protectionPower);
        this.assistPower = 0;
        this.healPower = 0;
    }

    /**
     * Constrói um aliado com papel, assistência e cura.
     * @param name nome de apresentação
     * @param role função do aliado
     * @param assistPower potência de assistência (não negativa)
     * @param healPower potência de cura (não negativa)
     * @param protectionPower poder de proteção (não negativo)
     */
    public Ally(String name, String role, int assistPower, int healPower, double protectionPower) {
        super(name);
        this.role = role;
        this.assistPower = Math.max(0, assistPower);
        this.healPower = Math.max(0, healPower);
        this.protectionPower = Math.max(0.0, protectionPower);
    }

    /** Devolve o papel atual do aliado. */
    public String getRole() { return role; }
    /** Define o papel do aliado. */
    public void setRole(String role) { this.role = role; }

    /** Devolve o poder de proteção. */
    public double getProtectionPower() { return protectionPower; }
    /** Define o poder de proteção (não negativo). */
    public void setProtectionPower(double protectionPower) { this.protectionPower = Math.max(0.0, protectionPower); }

    /** Devolve a potência de assistência. */
    public int getAssistPower() { return assistPower; }
    /** Define a potência de assistência (não negativa). */
    public void setAssistPower(int assistPower) { this.assistPower = Math.max(0, assistPower); }

    /** Devolve a potência de cura. */
    public int getHealPower() { return healPower; }
    /** Define a potência de cura (não negativa). */
    public void setHealPower(int healPower) { this.healPower = Math.max(0, healPower); }

    /**
     * Dano de suporte ofensivo baseado na potência de assistência.
     * @return valor de dano de suporte
     */
    public int supportDamage() { return assistPower; }
    /**
     * Aplica cura ao alvo com base na potência de cura.
     * @param target personagem alvo da cura
     */
    public void supportHeal(Character target) { if (target != null && healPower > 0) target.heal(healPower); }

    @Override
    public String toString() {
        return super.toString() + " Role: " + role;
    }
}
