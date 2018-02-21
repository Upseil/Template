package com.upseil.game.domain;

import com.upseil.gdx.pool.AbstractPooled;

public class Data extends AbstractPooled<Data>{
    
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public void reset() {
        super.reset();
        value = 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Data other = (Data) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Data.class.getSimpleName()).append("[value=").append(value).append("]");
        return builder.toString();
    }
    
}
