package com.example.pfe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PayloadMessage {
    private long restaurantId;
    private String action;

    public PayloadMessage(@JsonProperty("restaurantId") long restaurantId,
                          @JsonProperty("action") String action) {
        this.restaurantId = restaurantId;
        this.action = action;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}