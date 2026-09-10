package com.ambulance.service;

import com.ambulance.exception.*;
import com.ambulance.model.*;

import java.util.*;

public class DispatchEngine {
    private final Map<String, Ambulance> ambulances = new HashMap<>();
    private final PriorityQueue<EmergencyRequest> waitingQueue = new PriorityQueue<>();
    private final List<EmergencyRequest> history = new ArrayList<>();

    public void registerAmbulance(Ambulance ambulance) {
        if (ambulance == null || ambulance.getId() == null) {
            throw new IllegalArgumentException("Invalid ambulance configuration.");
        }
        ambulances.put(ambulance.getId(), ambulance);
    }

    public synchronized void submitEmergencyRequest(EmergencyRequest request) {
        if (request == null || request.getPatientId() == null || request.getRequestId() == null) {
            throw new InvalidEmergencyRequestException("Emergency request parameters cannot be null.");
        }
        
        history.add(request);
        boolean assigned = attemptAllocation(request);
        
        if (!assigned) {
            waitingQueue.add(request);
        }
    }

    private boolean attemptAllocation(EmergencyRequest request) {
        Ambulance bestMatch = null;
        double minDistance = Double.MAX_VALUE;

        for (Ambulance amb : ambulances.values()) {
            if (amb.isAvailable() && amb.getType() == request.getRequiredType()) {
                double distance = calculateDistance(amb.getCurrentX(), amb.getCurrentY(), request.getPickupX(), request.getPickupY());
                if (distance < minDistance) {
                    minDistance = distance;
                    bestMatch = amb;
                }
            }
        }

        if (bestMatch != null) {
            bestMatch.setState(AmbulanceState.DISPATCHED);
            request.setAssignedAmbulance(bestMatch);
            request.setStatus(EmergencyStatus.ASSIGNED);
            request.setEstimatedDistance(minDistance);
            
            // Assume an average speed of 50 km/h (0.83 km per minute)
            double eta = (minDistance / 50.0) * 60.0;
            request.setEstimatedArrivalTime(eta);
            return true;
        }
        return false;
    }

    public synchronized void updateAmbulanceState(String ambulanceId, AmbulanceState newState, double currentX, double currentY) {
        Ambulance amb = ambulances.get(ambulanceId);
        if (amb == null) {
            throw new ResourceUnavailableException("Ambulance ID not found: " + ambulanceId);
        }

        amb.setState(newState);
        amb.setLocation(currentX, currentY);

        if (newState == AmbulanceState.HOSPITAL_ARRIVED) {
            // Transition back to available and trigger queue processing
            amb.setState(AmbulanceState.AVAILABLE);
            processWaitingQueue();
        }
    }

    private void processWaitingQueue() {
        List<EmergencyRequest> unassigned = new ArrayList<>();
        while (!waitingQueue.isEmpty()) {
            EmergencyRequest request = waitingQueue.poll();
            boolean success = attemptAllocation(request);
            if (!success) {
                unassigned.add(request);
            }
        }
        waitingQueue.addAll(unassigned);
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public List<EmergencyRequest> getHistory() { return new ArrayList<>(history); }
    public PriorityQueue<EmergencyRequest> getWaitingQueue() { return waitingQueue; }
    public Map<String, Ambulance> getAmbulances() { return ambulances; }
}
