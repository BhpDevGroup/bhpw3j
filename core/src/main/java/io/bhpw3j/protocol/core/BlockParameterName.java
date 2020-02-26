package io.bhpw3j.protocol.core;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BlockParameterName implements BlockParameter {

    EARLIEST("earliest"),
    LATEST("latest");

    private String name;

    BlockParameterName(String name) {
        this.name = name;
    }

    @JsonValue
    @Override
    public String getValue() {
        return name;
    }

    public static BlockParameterName fromString(String name) {
        if (name != null) {
            for (BlockParameterName blockParameterName :
                    BlockParameterName.values()) {
                if (name.equalsIgnoreCase(blockParameterName.name)) {
                    return blockParameterName;
                }
            }
        }
        return valueOf(name);
    }
}
