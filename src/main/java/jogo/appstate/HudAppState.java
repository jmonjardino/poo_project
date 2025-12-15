package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import jogo.gameobject.character.Player;

public class HudAppState extends BaseAppState {

    private final Node guiNode;
    private final AssetManager assetManager;
    private BitmapText crosshair;
    private BitmapText inventoryText;
    private BitmapText statusText;
    private final PlayerAppState playerAppState;
    private int lastCapacity = -1;
    private float statusTTL = 0f;
    private BitmapText healthText;

    public HudAppState(Node guiNode, AssetManager assetManager, PlayerAppState playerAppState) {
        this.guiNode = guiNode;
        this.assetManager = assetManager;
        this.playerAppState = playerAppState;
    }

    @Override
    protected void initialize(Application app) {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        crosshair = new BitmapText(font, false);
        crosshair.setText("+");
        crosshair.setSize(font.getCharSet().getRenderedSize() * 2f);
        guiNode.attachChild(crosshair);
        centerCrosshair();
        System.out.println("HudAppState inicializado: mira anexada");

        inventoryText = new BitmapText(font, false);
        inventoryText.setSize(font.getCharSet().getRenderedSize());
        guiNode.attachChild(inventoryText);
        updateInventoryText();
        positionInventoryText();

        statusText = new BitmapText(font, false);
        statusText.setSize(font.getCharSet().getRenderedSize());
        guiNode.attachChild(statusText);
        positionStatusText();

        healthText = new BitmapText(font, false);
        healthText.setSize(font.getCharSet().getRenderedSize());
        guiNode.attachChild(healthText);
        positionHealthText();
    }

    private void centerCrosshair() {
        SimpleApplication sapp = (SimpleApplication) getApplication();
        int w = sapp.getCamera().getWidth();
        int h = sapp.getCamera().getHeight();
        float x = (w - crosshair.getLineWidth()) / 2f;
        float y = (h + crosshair.getLineHeight()) / 2f;
        crosshair.setLocalTranslation(x, y, 0);
    }

    private void positionInventoryText() {
        SimpleApplication sapp = (SimpleApplication) getApplication();
        int h = sapp.getCamera().getHeight();
        float x = 10f;
        float y = h - 10f;
        inventoryText.setLocalTranslation(x, y, 0);
    }

    private void positionStatusText() {
        // Posicionar no canto inferior direito para não sobrepor o inventário
        SimpleApplication sapp = (SimpleApplication) getApplication();
        int w = sapp.getCamera().getWidth();
        float textWidth = statusText.getLineWidth();
        float x = w - textWidth - 20f; // 20 pixels da margem direita
        float y = 60f; // 60 pixels do fundo
        statusText.setLocalTranslation(x, y, 0);
    }

    private void positionHealthText() {
        // Posicionar na parte inferior da tela, acima do status
        float x = 10f;
        float y = 80f; // 80 pixels do fundo (acima do status)
        healthText.setLocalTranslation(x, y, 0);
    }

    private void updateInventoryText() {
        inventoryText.setText(inventorySummary());
    }

    private void updateHealthText() {
        if (playerAppState != null && playerAppState.getPlayer() != null) {
            int hp = playerAppState.getPlayer().getHealth();
            healthText.setText("Vida: " + hp);
            if (hp > 50)
                healthText.setColor(com.jme3.math.ColorRGBA.Green);
            else if (hp > 20)
                healthText.setColor(com.jme3.math.ColorRGBA.Orange);
            else
                healthText.setColor(com.jme3.math.ColorRGBA.Red);
        }
    }

    private String inventorySummary() {
        Player p = playerAppState != null ? playerAppState.getPlayer() : null;
        if (p == null)
            return "Inv: (Player null)";

        var inv = p.getInventory();
        int cap = inv.capacity();
        int selected = p.getSelectedSlot();

        StringBuilder sb = new StringBuilder("Inv:\n");
        for (int i = 0; i < cap; i++) {
            int type = inv.getItemTypeAt(i);
            int count = inv.getCountAt(i);

            if (i == selected)
                sb.append(">");
            else
                sb.append(" ");

            sb.append(i + 1).append(": ");
            if (type > 0 && count > 0) {
                sb.append(nameForType(type)).append(" x").append(count);
            } else {
                sb.append("---");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String nameForType(int type) {
        if (type == 100)
            return "Ferramenta";
        if (type == 200)
            return "Madeira";
        if (type == 210)
            return "Tábuas";
        if (type == 220)
            return "Pau";
        if (type == 300)
            return "Terra";
        if (type == 310)
            return "Bancada";
        if (type == 400)
            return "Machado";
        return "Item " + type;
    }

    public void showStatus(String msg, float ttl) {
        statusText.setText(msg != null ? msg : "");
        statusTTL = Math.max(0f, ttl);
        positionStatusText();
    }

    @Override
    public void update(float tpf) {
        centerCrosshair();
        updateInventoryText();
        positionInventoryText();
        updateHealthText();
        positionHealthText();
        if (statusTTL > 0f) {
            statusTTL -= tpf;
            if (statusTTL <= 0f)
                statusText.setText("");
        }
    }

    @Override
    protected void cleanup(Application app) {
        if (crosshair != null)
            crosshair.removeFromParent();
        if (inventoryText != null)
            inventoryText.removeFromParent();
        if (statusText != null)
            statusText.removeFromParent();
        if (healthText != null)
            healthText.removeFromParent();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
}
