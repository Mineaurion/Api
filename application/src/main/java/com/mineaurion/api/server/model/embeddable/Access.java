package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Embeddable;

@Embeddable
public class Access {
    private boolean beta = false;
    private boolean paying = false;
    private boolean donator = false;

    public Access() {
    }

    public Access(boolean beta, boolean paying, boolean donator) {
        this.beta = beta;
        this.paying = paying;
        this.donator = donator;
    }


    public boolean isBeta() {
        return beta;
    }

    public void setBeta(boolean beta) {
        this.beta = beta;
    }

    public boolean isPaying() {
        return paying;
    }

    public void setPaying(boolean paying) {
        this.paying = paying;
    }

    public boolean isDonator() {
        return donator;
    }

    public void setDonator(boolean donator) {
        this.donator = donator;
    }
}
