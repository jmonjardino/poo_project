package jogo.gameobject.capability;

import jogo.framework.math.Vec3;
import jogo.gameobject.character.Character;

/**
 * Contexto de execução para IA.
 * Fornece posição do jogador e parâmetros de tempo/velocidade base
 * para decisões determinísticas (ex.: follow com passo limitado).
 */
public class AIContext {
    /** Posição lógica atual do jogador no mundo (camada de jogo). */
    public final Vec3 playerPos;
    /**
     * Velocidade base de caminhada do jogador (sem sprint), usada como limite do
     * Ally.
     */
    public final double playerBaseSpeed;
    /**
     * Tempo por frame (tpf) do ciclo de atualização, para converter velocidade em
     * deslocação.
     */
    public final float tpf;
    /** Referência para o personagem do jogador. */
    public final Character playerRef;
    /** Referência para os outros objetos do jogo (para evitar colisão). */
    public final java.util.Collection<jogo.gameobject.GameObject> neighbors;

    /** Constrói contexto com posição do jogador, velocidade base e tpf corrente. */
    public AIContext(Vec3 playerPos, double playerBaseSpeed, float tpf, Character playerRef,
            java.util.Collection<jogo.gameobject.GameObject> neighbors) {
        this.playerPos = playerPos;
        this.playerBaseSpeed = playerBaseSpeed;
        this.tpf = tpf;
        this.playerRef = playerRef;
        this.neighbors = neighbors;
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
        if (dist <= stopRange || dist == 0.0)
            return new Vec3(0f, 0f, 0f);
        // Initial step towards player
        double step = Math.min(maxStep, Math.max(0.0, dist - stopRange));
        double scale = step / dist;
        float sx = (float) (dx * scale);
        float sz = (float) (dz * scale);

        // Repulsion from neighbors
        if (neighbors != null) {
            for (jogo.gameobject.GameObject obj : neighbors) {
                // Ignore self and player (player handled by stopRange, self handled by
                // identity)
                // We rely on caller passing 'from' as their own position, but we don't have
                // 'this' reference here easily.
                // However, distance to self is 0.
                Vec3 np = obj.getPosition();
                double d2 = (from.x - np.x) * (from.x - np.x) + (from.z - np.z) * (from.z - np.z);
                if (d2 > 0.0001 && d2 < 1.0) { // If closer than 1.0 unit (and not self)
                    double d = Math.sqrt(d2);
                    double push = (1.0 - d) * 2.0 * tpf; // Strong push back
                    double px = (from.x - np.x) / d;
                    double pz = (from.z - np.z) / d;
                    sx += px * push;
                    sz += pz * push;
                }
            }
        }

        return new Vec3(sx, 0f, sz);
    }

    /** Versão que usa velocidade base do jogador e tpf para calcular maxStep. */
    public Vec3 computeStepTowardsPlayerXZ(Vec3 from, double stopRange) {
        return computeStepTowardsPlayerXZ(from, playerBaseSpeed * Math.max(0f, tpf), stopRange);
    }
}
