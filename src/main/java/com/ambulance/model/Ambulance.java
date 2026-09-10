package com.ambulance.model;

public class Ambulance {
    private final String id;
    private final AmbulanceType type;
    private final String driverDetails;
    private AmbulanceState state;
    private double currentX;
    private double currentY;

    public Ambulance(String id, AmbulanceType type, String driverDetails, double currentX, double currentY) {
        this.id = id;
        this.type = type;
        this.driverDetails = driverDetails;
        this.state = AmbulanceState.AVAILABLE;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    // Getters and Setters
    public String getId() { return id; }
    public AmbulanceType getType() { return type; }
    public String getDriverDetails() { return driverDetails; }
    public AmbulanceState getState() { return state; }
    public void setState(AmbulanceState state) { this.state = state; }
    public double getCurrentX() { return currentX; }
    public double getCurrentY() { return currentY; }
    public void setLocation(double x, double y) { this.currentX = x; this.currentY = y; }
    public boolean isAvailable() { return this.state == AmbulanceState.AVAILABLE; }
}
