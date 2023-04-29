package com.mineaurion.api.server.model;


import com.mineaurion.api.server.model.embeddable.Access;
import com.mineaurion.api.server.model.embeddable.Administration;
import com.mineaurion.api.server.model.embeddable.Version;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Schema(example = "Ultimate 1.4.7")
    private String name;

    @NotNull
    @Embedded
    @Valid
    private Version version;

    @NotEmpty
    @Pattern(regexp = "^(overworld|skyblock)$", message = "Le type doit être soit overworld soit skyblock")
    private String type;

    @NotNull
    @Embedded
    @Valid
    private Access access;

    @NotEmpty
    @Pattern(regexp = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$", message = "ne correspond pas à un nom de domaine")
    @Schema(example = "ultimate.mineaurion.com")
    private String dns;

    @NotNull
    @Embedded
    @Valid
    private Administration administration;

    @Schema(defaultValue = "false")
    private boolean hidden = false;

    public Server(Long id, String name, Version version, String type, Access access, String dns, Administration administration, boolean hidden) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.type = type;
        this.access = access;
        this.dns = dns;
        this.administration = administration;
        this.hidden = hidden;
    }

    public Server() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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
                server.hidden
        );
    }

    public com.mineaurion.api.library.model.query.Server toQueryServer(){
        return new com.mineaurion.api.library.model.query.Server(
                getName(),
                new com.mineaurion.api.library.model.query.Version(getVersion().getMinecraft(), getVersion().getModpack()),
                getType(),
                new com.mineaurion.api.library.model.query.Access(getAccess().isBeta(), getAccess().isPaying(), getAccess().isDonator()),
                getDns()
        );
    }
}