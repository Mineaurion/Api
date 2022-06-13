package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Embeddable
public class Schedule {
    @NotNull
    @Size(min = 3, max = 5)
    @Valid
    @Column(name = "reboot")
    @ElementCollection(targetClass = String.class)
    private List<
            @Pattern(regexp = "^([0-2]?\\dh)?([0-5]?\\dm)?$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "le format doit être le suivant <nombre>h<nombre>m")
                    String
            > reboot;

    public Schedule(List<String> reboot) {
        this.reboot = reboot;
    }

    public Schedule() {
    }

    public List<String> getReboot() {
        return reboot;
    }

    public void setReboot(List<String> reboot) {
        this.reboot = reboot;
    }
}
