package com.mineaurion.api.library.model.query;

public class Access {
    private boolean beta;
    private boolean paying;

    private boolean donator;

    public Access(boolean beta, boolean paying, boolean donator) {
        this.beta = beta;
        this.paying = paying;
        this.donator = donator;
    }

    public boolean isBeta() {
        return beta;
    }

    public Access setBeta(boolean beta) {
        this.beta = beta;
        return this;
    }

    public boolean isPaying() {
        return paying;
    }

    public Access setPaying(boolean paying) {
        this.paying = paying;
        return this;
    }

    public boolean isDonator() {
        return donator;
    }

    public Access setDonator(boolean donator) {
        this.donator = donator;
        return this;
    }
}
