package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Access {

    @NotNull
    private boolean beta;

    @NotNull
    private boolean paying;

    @NotNull
    private boolean donator;

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
