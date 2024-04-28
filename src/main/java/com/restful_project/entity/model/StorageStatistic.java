package com.restful_project.entity.model;

import java.time.LocalDate;

public class StorageStatistic {
    private LocalDate date;
    private int incoming;
    private int outgoing;
    private int currentRest;

    public StorageStatistic() {}

    public StorageStatistic(LocalDate date, int receipt, int consumption, int currentRest) {
        this.date = date;
        this.incoming = receipt;
        this.outgoing = consumption;
        this.currentRest = currentRest;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getIncoming() {
        return incoming;
    }

    public void setIncoming(int incoming) {
        this.incoming = incoming;
    }

    public int getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(int outgoing) {
        this.outgoing = outgoing;
    }

    public int getCurrentRest() {
        return currentRest;
    }

    public void setCurrentRest(int currentRest) {
        this.currentRest = currentRest;
    }
}
