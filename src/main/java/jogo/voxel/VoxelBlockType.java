package jogo.voxel;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

public abstract class VoxelBlockType {
    private final String name;

    protected VoxelBlockType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** Whether this block is physically solid (collides/occludes). */
    public boolean isSolid() { return true; }

    /**
     * Relative resistance to breaking or mining for this block type.
     * Unitless value used by game logic to scale tool effectiveness and break time.
     * Typical range: softer materials &lt; 1.0, woods around ~1.0, hard stone &gt; 2.0.
     * Defaults to 1.0.
     * @return hardness value (non-negative)
     */
    public float getHardness() { return 1.0f; }

    /**
     * Returns the Material for this block type. Override in subclasses for custom materials.
     */
    public abstract Material getMaterial(AssetManager assetManager);

    /**
     * Returns the Material for this block type at a specific block position.
     * Default implementation ignores the position for backward compatibility.
     * Subclasses can override to use blockPos.
     */
    public Material getMaterial(AssetManager assetManager, jogo.framework.math.Vec3 blockPos) {
        return getMaterial(assetManager);
    }
}
