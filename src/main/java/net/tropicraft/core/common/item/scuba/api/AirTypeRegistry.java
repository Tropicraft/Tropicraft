package net.tropicraft.core.common.item.scuba.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.tropicraft.core.common.item.scuba.api.IAirType.AirType;

public enum AirTypeRegistry {

    INSTANCE;

    private Map<String, IAirType> types = new HashMap<>();

    private AirTypeRegistry() {
        Arrays.stream(AirType.values()).forEach(this::registerType);
    }

    public void registerType(AirType type) {
        this.types.put(type.getID(), type);
    }

    public IAirType getType(String id) {
        return types.get(id);
    }
    
    public Collection<IAirType> getTypes() {
        return types.values();
    }
}
