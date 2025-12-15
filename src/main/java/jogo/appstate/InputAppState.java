package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class InputAppState extends BaseAppState implements ActionListener, AnalogListener {

    private boolean forward, backward, left, right;
    private boolean sprint;
    private volatile boolean jumpRequested;
    private volatile boolean breakRequested;
    private volatile boolean placeRequested;
    private volatile boolean toggleShadingRequested;
    private volatile boolean respawnRequested;
    private volatile boolean interactRequested;
    private volatile boolean printCoordsRequested;
    private volatile boolean craftRequested;
    private volatile boolean craftRecipe1Requested;
    private volatile boolean craftRecipe2Requested;
    private volatile boolean craftRecipe3Requested;
    private volatile boolean craftRecipe4Requested;
    private volatile boolean saveRequested;
    private volatile boolean loadRequested;
    private volatile boolean highscoresRequested;
    private float mouseDX, mouseDY;
    private boolean mouseCaptured = true;

    @Override
    protected void initialize(Application app) {
        var im = app.getInputManager();
        // Teclas de movimento
        im.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        im.addMapping("MoveBackward", new KeyTrigger(KeyInput.KEY_S));
        im.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_A));
        im.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_D));
        im.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        im.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        // Olhar com o rato
        im.addMapping("MouseX+", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        im.addMapping("MouseX-", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        im.addMapping("MouseY+", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        im.addMapping("MouseY-", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        // Alternar captura (usar TAB, ESC sai da aplicação por omissão)
        im.addMapping("ToggleMouse", new KeyTrigger(KeyInput.KEY_TAB));
        // Partir vóxel (rato esquerdo)
        im.addMapping("Break", new MouseButtonTrigger(com.jme3.input.MouseInput.BUTTON_LEFT));
        im.addMapping("Place", new MouseButtonTrigger(com.jme3.input.MouseInput.BUTTON_RIGHT));
        // Alternar sombreamento (L)
        im.addMapping("ToggleShading", new KeyTrigger(KeyInput.KEY_L));
        // Renascer (R)
        im.addMapping("Respawn", new KeyTrigger(KeyInput.KEY_R));
        // Interagir (E)
        im.addMapping("Interact", new KeyTrigger(KeyInput.KEY_E));
        // Imprimir coordenadas (O)
        im.addMapping("PrintCoords", new KeyTrigger(KeyInput.KEY_O));
        im.addMapping("Craft", new KeyTrigger(KeyInput.KEY_C));
        im.addMapping("CraftRecipe1", new KeyTrigger(KeyInput.KEY_1));
        im.addMapping("CraftRecipe2", new KeyTrigger(KeyInput.KEY_2));
        im.addMapping("CraftRecipe3", new KeyTrigger(KeyInput.KEY_3));
        im.addMapping("CraftRecipe4", new KeyTrigger(KeyInput.KEY_4));
        im.addMapping("SaveGame", new KeyTrigger(KeyInput.KEY_F5));
        im.addMapping("LoadGame", new KeyTrigger(KeyInput.KEY_F9));
        im.addMapping("Highscores", new KeyTrigger(KeyInput.KEY_H));

        im.addListener(this, "MoveForward", "MoveBackward", "MoveLeft", "MoveRight", "Jump", "Sprint", "ToggleMouse",
                "Break", "Place", "ToggleShading", "Respawn", "Interact", "PrintCoords", "Craft", "CraftRecipe1",
                "CraftRecipe2", "CraftRecipe3", "CraftRecipe4", "SaveGame", "LoadGame", "Highscores");
        im.addListener(this, "MouseX+", "MouseX-", "MouseY+", "MouseY-");
    }

    @Override
    protected void cleanup(Application app) {
        var im = app.getInputManager();
        im.deleteMapping("MoveForward");
        im.deleteMapping("MoveBackward");
        im.deleteMapping("MoveLeft");
        im.deleteMapping("MoveRight");
        im.deleteMapping("Jump");
        im.deleteMapping("Sprint");
        im.deleteMapping("MouseX+");
        im.deleteMapping("MouseX-");
        im.deleteMapping("MouseY+");
        im.deleteMapping("MouseY-");
        im.deleteMapping("ToggleMouse");
        im.deleteMapping("Break");
        im.deleteMapping("Place");
        im.deleteMapping("ToggleShading");
        im.deleteMapping("Respawn");
        im.deleteMapping("Interact");
        im.deleteMapping("Craft");
        im.deleteMapping("CraftRecipe1");
        im.deleteMapping("CraftRecipe2");
        im.deleteMapping("CraftRecipe3");
        im.deleteMapping("CraftRecipe4");
        im.deleteMapping("SaveGame");
        im.deleteMapping("LoadGame");
        im.deleteMapping("Highscores");
        im.removeListener(this);
    }

    @Override
    protected void onEnable() {
        setMouseCaptured(true);
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "MoveForward" -> forward = isPressed;
            case "MoveBackward" -> backward = isPressed;
            case "MoveLeft" -> left = isPressed;
            case "MoveRight" -> right = isPressed;
            case "Sprint" -> sprint = isPressed;
            case "Jump" -> {
                if (isPressed)
                    jumpRequested = true;
            }
            case "ToggleMouse" -> {
                if (isPressed)
                    setMouseCaptured(!mouseCaptured);
            }
            case "Break" -> {
                if (isPressed && mouseCaptured)
                    breakRequested = true;
            }
            case "Place" -> {
                if (isPressed && mouseCaptured)
                    placeRequested = true;
            }
            case "ToggleShading" -> {
                if (isPressed)
                    toggleShadingRequested = true;
            }
            case "Respawn" -> {
                if (isPressed)
                    respawnRequested = true;
            }
            case "Interact" -> {
                if (isPressed && mouseCaptured)
                    interactRequested = true;
            }
            case "PrintCoords" -> {
                if (isPressed)
                    printCoordsRequested = true;
            }
            case "Craft" -> {
                if (isPressed)
                    craftRequested = true;
            }
            case "CraftRecipe1" -> {
                if (isPressed)
                    craftRecipe1Requested = true;
            }
            case "CraftRecipe2" -> {
                if (isPressed)
                    craftRecipe2Requested = true;
            }
            case "CraftRecipe3" -> {
                if (isPressed)
                    craftRecipe3Requested = true;
            }
            case "CraftRecipe4" -> {
                if (isPressed)
                    craftRecipe4Requested = true;
            }
            case "SaveGame" -> {
                if (isPressed)
                    saveRequested = true;
            }
            case "LoadGame" -> {
                if (isPressed)
                    loadRequested = true;
            }
            case "Highscores" -> {
                if (isPressed)
                    highscoresRequested = true;
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (!mouseCaptured)
            return;
        switch (name) {
            case "MouseX+" -> mouseDX += value;
            case "MouseX-" -> mouseDX -= value;
            case "MouseY+" -> mouseDY += value;
            case "MouseY-" -> mouseDY -= value;
        }
    }

    public Vector3f getMovementXZ() {
        float fb = (forward ? 1f : 0f) + (backward ? -1f : 0f);
        float lr = (right ? 1f : 0f) + (left ? -1f : 0f);
        return new Vector3f(lr, 0f, -fb); // -fb para que frente mapeie para -Z no padrão JME
    }

    public boolean isSprinting() {
        return sprint;
    }

    public boolean consumeJumpRequested() {
        boolean jr = jumpRequested;
        jumpRequested = false;
        return jr;
    }

    public boolean consumeBreakRequested() {
        boolean r = breakRequested;
        breakRequested = false;
        return r;
    }

    public boolean consumePlaceRequested() {
        boolean r = placeRequested;
        placeRequested = false;
        return r;
    }

    public boolean consumeToggleShadingRequested() {
        boolean r = toggleShadingRequested;
        toggleShadingRequested = false;
        return r;
    }

    public boolean consumeRespawnRequested() {
        boolean r = respawnRequested;
        respawnRequested = false;
        return r;
    }

    public boolean consumeInteractRequested() {
        boolean r = interactRequested;
        interactRequested = false;
        return r;
    }

    public Vector2f consumeMouseDelta() {
        Vector2f d = new Vector2f(mouseDX, mouseDY);
        mouseDX = 0f;
        mouseDY = 0f;
        return d;
    }

    public void setMouseCaptured(boolean captured) {
        this.mouseCaptured = captured;
        var im = getApplication().getInputManager();
        im.setCursorVisible(!captured);
        // Limpar deltas acumulados ao trocar de estado
        mouseDX = 0f;
        mouseDY = 0f;
    }

    public boolean isMouseCaptured() {
        return mouseCaptured;
    }

    public boolean consumePrintCoordsRequested() {
        boolean r = printCoordsRequested;
        printCoordsRequested = false;
        return r;
    }

    public boolean consumeCraftRequested() {
        boolean r = craftRequested;
        craftRequested = false;
        return r;
    }

    public boolean consumeCraftRecipe1Requested() {
        boolean r = craftRecipe1Requested;
        craftRecipe1Requested = false;
        return r;
    }

    public boolean consumeCraftRecipe2Requested() {
        boolean r = craftRecipe2Requested;
        craftRecipe2Requested = false;
        return r;
    }

    public boolean consumeCraftRecipe3Requested() {
        boolean r = craftRecipe3Requested;
        craftRecipe3Requested = false;
        return r;
    }

    public boolean consumeCraftRecipe4Requested() {
        boolean r = craftRecipe4Requested;
        craftRecipe4Requested = false;
        return r;
    }

    public boolean consumeSaveRequested() {
        boolean r = saveRequested;
        saveRequested = false;
        return r;
    }

    public boolean consumeLoadRequested() {
        boolean r = loadRequested;
        loadRequested = false;
        return r;
    }

    public boolean consumeHighscoresRequested() {
        boolean r = highscoresRequested;
        highscoresRequested = false;
        return r;
    }
}
