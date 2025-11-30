package jogo.gameobject.capability;
import jogo.framework.math.Vec3;

public class AIContext {
    public final Vec3 playerPos;
    public AIContext(Vec3 playerPos) {
        this.playerPos = playerPos;
    }

    public double distanceXZ(Vec3 from) {
        double dx = playerPos.x - from.x;
        double dz = playerPos.z - from.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public boolean shouldFollow(Vec3 allyPos, double followRange) {
        return distanceXZ(allyPos) <= followRange;
    }

    public boolean shouldStop(Vec3 allyPos, double stopRange) {
        return distanceXZ(allyPos) <= stopRange;
    }

    public Vec3 computeStepTowardsPlayerXZ(Vec3 from, double maxStep, double stopRange) {
        double dx = playerPos.x - from.x;
        double dz = playerPos.z - from.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist <= stopRange || dist == 0.0) return new Vec3(0f, 0f, 0f);
        double step = Math.min(maxStep, Math.max(0.0, dist - stopRange));
        double scale = step / dist;
        return new Vec3((float)(dx * scale), 0f, (float)(dz * scale));
    }
}
