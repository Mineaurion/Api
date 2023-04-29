package com.mineaurion.api.query.pterodactyl.schedule;

import java.util.Date;

public class Attributes {
    private int id;
    private String name;
    private Object cron; // Child keys but not for use now
    private boolean is_active;
    private boolean is_processing;
    private Date last_run_at;
    private Date next_run_at;
    private Date created_at;
    private Date updated_at;

    private Object relationships; // Child keys but not for use now

    public int getId() {
        return id;
    }

    public Attributes setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Attributes setName(String name) {
        this.name = name;
        return this;
    }

    public Object getCron() {
        return cron;
    }

    public Attributes setCron(Object cron) {
        this.cron = cron;
        return this;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public Attributes setIs_active(boolean is_active) {
        this.is_active = is_active;
        return this;
    }

    public boolean isIs_processing() {
        return is_processing;
    }

    public Attributes setIs_processing(boolean is_processing) {
        this.is_processing = is_processing;
        return this;
    }

    public Date getLast_run_at() {
        return last_run_at;
    }

    public Attributes setLast_run_at(Date last_run_at) {
        this.last_run_at = last_run_at;
        return this;
    }

    public Date getNext_run_at() {
        return next_run_at;
    }

    public Attributes setNext_run_at(Date next_run_at) {
        this.next_run_at = next_run_at;
        return this;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Attributes setCreated_at(Date created_at) {
        this.created_at = created_at;
        return this;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public Attributes setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
        return this;
    }

    public Object getRelationships() {
        return relationships;
    }

    public Attributes setRelationships(Object relationships) {
        this.relationships = relationships;
        return this;
    }
}
