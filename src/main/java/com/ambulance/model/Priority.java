package com.ambulance.model;

public enum Priority {
    CRITICAL(1), HIGH(2), MODERATE(3), NORMAL(4);

    private final int rank;
    Priority(int rank) { this.rank = rank; }
    public int getRank() { return rank; }
}
