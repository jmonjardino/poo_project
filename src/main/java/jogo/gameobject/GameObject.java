package jogo.gameobject;

import jogo.framework.math.Vec3;

/**
 * Base neutra em relação ao motor para todos os objetos de jogo usados pelos estudantes.
 * Guarda apenas a identidade e a posição lógica; a renderização/física é tratada pelos AppStates do motor.
 */
public abstract class GameObject {
    protected final String name;
    protected Vec3 position = new Vec3();

    protected GameObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 pos) {
        this.position.set(pos);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }
}
