package com.upseil.game.component;

import com.artemis.Component;
import com.upseil.game.domain.Data;

public class GameState extends Component {
    
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
}
