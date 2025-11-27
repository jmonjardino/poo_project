package jogo.crafting;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Recipe {
    private final Map<Integer, Integer> inputs;
    private final int outputType;
    private final int outputQty;
    private final String name;

    public Recipe(String name, Map<Integer, Integer> inputs, int outputType, int outputQty) {
        this.name = name;
        this.inputs = Collections.unmodifiableMap(new LinkedHashMap<>(inputs));
        this.outputType = outputType;
        this.outputQty = outputQty;
    }

    public String getName() { return name; }
    public Map<Integer, Integer> getInputs() { return inputs; }
    public int getOutputType() { return outputType; }
    public int getOutputQty() { return outputQty; }
}
