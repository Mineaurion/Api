package com.mineaurion.api.server.model;


import com.mineaurion.api.server.model.embeddable.Access;
import com.mineaurion.api.server.model.embeddable.Administration;
import com.mineaurion.api.server.model.embeddable.Schedule;
import com.mineaurion.api.server.model.embeddable.Version;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
// TODO: ajouter des options pour la supervision prometheus cf: https://prometheus.io/docs/prometheus/latest/configuration/configuration/#http_sd_config
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z\\d ]+$", message = "ne correspond pas à une chaine de caractère")
    @Schema(example = "Ultimate 1.4.7")
    private String name;

    @NotNull
    @Embedded
    @Valid
    private Version version;

    @NotNull
    @Pattern(regexp = "^(overworld|skyblock)$", message = "Le type doit être soit overworld soit skyblock")
    private String type;

    @NotNull
    @Embedded
    @Valid
    private Access access;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z .-]+$", message = "ne correspond pas à un nom de domaine")
    @Schema(example = "ultimate.mineaurion.com")
    private String dns;

    @NotNull
    @Embedded
    @Valid
    private Administration administration;

    @NotNull
    @Embedded
    @Valid
    private Schedule schedule;

    public Server(Long id, String name, Version version, String type, Access access, String dns, Administration administration, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.type = type;
        this.access = access;
        this.dns = dns;
        this.administration = administration;
        this.schedule = schedule;
    }

    public Server() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public Administration getAdministration() {
        return administration;
    }

    public void setAdministration(Administration administration) {
        this.administration = administration;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Server updateWith(Server server) {
        return new Server(
                this.id,
                server.name,
                server.version,
                server.type,
                server.access,
                server.dns,
                server.administration,
                server.schedule
        );
    }
}