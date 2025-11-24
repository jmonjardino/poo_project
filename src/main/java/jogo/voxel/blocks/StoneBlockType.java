package jogo.voxel.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture;
import jogo.util.ProcTextures;
import jogo.voxel.VoxelBlockType;

public class StoneBlockType extends VoxelBlockType {
    public StoneBlockType() {
        super("stone");
    }
    // isSolid() inherits true from base

    @Override
    public float getHardness() {
        return 2.5f;
    }

    @Override
    public Material getMaterial(AssetManager assetManager) {
        Texture2D tex = (Texture2D) assetManager.loadTexture("textures/blocks/stone.png");
        tex.setMagFilter(Texture.MagFilter.Nearest);
        tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        tex.setAnisotropicFilter(1);
        tex.setWrap(Texture.WrapMode.Repeat);
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", tex);
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", ColorRGBA.White);
        m.setColor("Specular", ColorRGBA.White.mult(0.0f));
        m.setFloat("Shininess", 32f);
        return m;
    }
}
