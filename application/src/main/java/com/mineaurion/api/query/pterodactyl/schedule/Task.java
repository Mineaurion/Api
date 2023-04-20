package com.mineaurion.api.query.pterodactyl.schedule;

import java.util.Date;

public class Task {
    private int id;
    private Date created_at;
    private Date updated_at;
    private int sequence_id;
    private String action;
    private String payload;
    private int time_offset;
    private boolean is_queued;

    public int getId() {
        return id;
    }

    public Task setId(int id) {
        this.id = id;
        return this;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Task setCreated_at(Date created_at) {
        this.created_at = created_at;
        return this;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public Task setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
        return this;
    }

    public int getSequence_id() {
        return sequence_id;
    }

    public Task setSequence_id(int sequence_id) {
        this.sequence_id = sequence_id;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Task setAction(String action) {
        this.action = action;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public Task setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public int getTime_offset() {
        return time_offset;
    }

    public Task setTime_offset(int time_offset) {
        this.time_offset = time_offset;
        return this;
    }

    public boolean isIs_queued() {
        return is_queued;
    }

    public Task setIs_queued(boolean is_queued) {
        this.is_queued = is_queued;
        return this;
    }
}
