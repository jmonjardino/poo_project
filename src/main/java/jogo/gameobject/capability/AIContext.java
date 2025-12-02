package jogo.gameobject.capability;
import jogo.framework.math.Vec3;

/**
 * Contexto de execução para IA.
 * Fornece posição do jogador e parâmetros de tempo/velocidade base
 * para decisões determinísticas (ex.: follow com passo limitado).
 */
public class AIContext {
    /** Posição lógica atual do jogador no mundo (camada de jogo). */
    public final Vec3 playerPos;
    /** Velocidade base de caminhada do jogador (sem sprint), usada como limite do Ally. */
    public final double playerBaseSpeed;
    /** Tempo por frame (tpf) do ciclo de atualização, para converter velocidade em deslocação. */
    public final float tpf;
    /** Constrói contexto com posição do jogador, velocidade base e tpf corrente. */
    public AIContext(Vec3 playerPos, double playerBaseSpeed, float tpf) {
        this.playerPos = playerPos;
        this.playerBaseSpeed = playerBaseSpeed;
        this.tpf = tpf;
    }

    /** Distância no plano XZ entre uma origem e a posição do jogador. */
    public double distanceXZ(Vec3 from) {
        double dx = playerPos.x - from.x;
        double dz = playerPos.z - from.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    /** Indica se o Ally deve transitar para FOLLOW (dentro do followRange). */
    public boolean shouldFollow(Vec3 allyPos, double followRange) {
        return distanceXZ(allyPos) <= followRange;
    }

    /** Indica se o Ally deve parar (IDLE) por estar dentro do stopRange. */
    public boolean shouldStop(Vec3 allyPos, double stopRange) {
        return distanceXZ(allyPos) <= stopRange;
    }

    /**
     * Calcula um passo XZ limitado em direção ao jogador.
     * maxStep define o deslocamento máximo permitido nesta atualização.
     */
    public Vec3 computeStepTowardsPlayerXZ(Vec3 from, double maxStep, double stopRange) {
        double dx = playerPos.x - from.x;
        double dz = playerPos.z - from.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist <= stopRange || dist == 0.0) return new Vec3(0f, 0f, 0f);
        double step = Math.min(maxStep, Math.max(0.0, dist - stopRange));
        double scale = step / dist;
        return new Vec3((float)(dx * scale), 0f, (float)(dz * scale));
    }
    /** Versão que usa velocidade base do jogador e tpf para calcular maxStep. */
    public Vec3 computeStepTowardsPlayerXZ(Vec3 from, double stopRange) {
        return computeStepTowardsPlayerXZ(from, playerBaseSpeed * Math.max(0f, tpf), stopRange);
    }
}
