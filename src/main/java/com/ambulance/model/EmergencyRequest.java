package com.ambulance.model;

public class EmergencyRequest implements Comparable<EmergencyRequest> {
    private final String requestId;
    private final String patientId;
    private final Priority priority;
    private final AmbulanceType requiredType;
    private final double pickupX;
    private final double pickupY;
    private final String destinationHospital;
    
    private EmergencyStatus status;
    private Ambulance assignedAmbulance;
    private double estimatedDistance;
    private double estimatedArrivalTime; // In minutes

    public EmergencyRequest(String requestId, String patientId, Priority priority, AmbulanceType requiredType, 
                            double pickupX, double pickupY, String destinationHospital) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.priority = priority;
        this.requiredType = requiredType;
        this.pickupX = pickupX;
        this.pickupY = pickupY;
        this.destinationHospital = destinationHospital;
        this.status = EmergencyStatus.PENDING;
    }

    @Override
    public int compareTo(EmergencyRequest o) {
        return Integer.compare(this.priority.getRank(), o.priority.getRank());
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public String getPatientId() { return patientId; }
    public Priority getPriority() { return priority; }
    public AmbulanceType getRequiredType() { return requiredType; }
    public double getPickupX() { return pickupX; }
    public double getPickupY() { return pickupY; }
    public String getDestinationHospital() { return destinationHospital; }
    public EmergencyStatus getStatus() { return status; }
    public void setStatus(EmergencyStatus status) { this.status = status; }
    public Ambulance getAssignedAmbulance() { return assignedAmbulance; }
    public void setAssignedAmbulance(Ambulance assignedAmbulance) { this.assignedAmbulance = assignedAmbulance; }
    public double getEstimatedDistance() { return estimatedDistance; }
    public void setEstimatedDistance(double estimatedDistance) { this.estimatedDistance = estimatedDistance; }
    public double getEstimatedArrivalTime() { return estimatedArrivalTime; }
    public void setEstimatedArrivalTime(double estimatedArrivalTime) { this.estimatedArrivalTime = estimatedArrivalTime; }
}
