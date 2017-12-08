package com.upseil.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upseil.gdx.math.Random;

public final class MetaData {

    public static final String DevVersion = "dev";
    public static final String Version = DevVersion;
    
    public static MetaData get() {
        return new MetaData(Version, TimeUtils.millis(), Random.getState(0), Random.getState(1));
    }
    
    private final String version;
    private final long timestamp;
    private final long seed0;
    private final long seed1;

    /** Only for serialization! **/
    @JsonCreator
    @Deprecated
    public MetaData(@JsonProperty("version") String version, @JsonProperty("timestamp") long timestamp,
                    @JsonProperty("seed0") long seed0, @JsonProperty("seed1") long seed1) {
        this.version = version;
        this.timestamp = timestamp;
        this.seed0 = seed0;
        this.seed1 = seed1;
    }
    
    public String getVersion() {
        return version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getSeed0() {
        return seed0;
    }

    public long getSeed1() {
        return seed1;
    }
    
}
