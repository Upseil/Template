package com.upseil.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class MetaData {

    public static final String DevVersion = "dev";
    public static final String Version = DevVersion;
    
    public static MetaData get() {
        return new MetaData(Version, TimeUtils.millis());
    }
    
    private final String version;
    private final long timestamp;

    /** Only for serialization! **/
    @JsonCreator
    @Deprecated
    public MetaData(@JsonProperty("version") String version, @JsonProperty("timestamp") long timestamp) {
        this.version = version;
        this.timestamp = timestamp;
    }
    
    public String getVersion() {
        return version;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
}
