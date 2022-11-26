package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class WhiteList {
    private boolean buy = false;
    private boolean donator = false;

    public WhiteList(boolean buy, boolean donator) {
        this.buy = buy;
        this.donator = donator;
    }

    public WhiteList() {
    }

    public boolean isDonator() {
        return donator;
    }

    public void setDonator(boolean donator) {
        this.donator = donator;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }
}
