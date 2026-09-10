package com.ambulance.service;

import com.ambulance.exception.*;
import com.ambulance.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DispatchEngineTest {
    private DispatchEngine engine;

    @BeforeEach
    public void setup() {
        engine = new DispatchEngine();
    }

    @Test
    public void testSuccessfulAllocationBasedOnDistanceAndType() {
        Ambulance amb1 = new Ambulance("AMB-01", AmbulanceType.ICU_AMBULANCE, "Driver A", 0, 0);
        Ambulance amb2 = new Ambulance("AMB-02", AmbulanceType.ICU_AMBULANCE, "Driver B", 10, 10);
        engine.registerAmbulance(amb1);
        engine.registerAmbulance(amb2);

        EmergencyRequest request = new EmergencyRequest("REQ-01", "P-100", Priority.CRITICAL, AmbulanceType.ICU_AMBULANCE, 1, 1, "City Hospital");
        engine.submitEmergencyRequest(request);

        assertEquals(EmergencyStatus.ASSIGNED, request.getStatus());
        assertEquals("AMB-01", request.getAssignedAmbulance().getId()); // Closes to (1,1)
        assertFalse(amb1.isAvailable());
    }

    @Test
    public void testQueueingAndAutomaticReallocation() {
        Ambulance amb = new Ambulance("AMB-01", AmbulanceType.BASIC, "Driver A", 0, 0);
        engine.registerAmbulance(amb);

        EmergencyRequest req1 = new EmergencyRequest("REQ-01", "P-01", Priority.HIGH, AmbulanceType.BASIC, 2, 2, "Hosp A");
        EmergencyRequest req2 = new EmergencyRequest("REQ-02", "P-02", Priority.CRITICAL, AmbulanceType.BASIC, 5, 5, "Hosp B");

        engine.submitEmergencyRequest(req1); // Takes up the only ambulance
        engine.submitEmergencyRequest(req2); // Moves to waiting queue

        assertEquals(1, engine.getWaitingQueue().size());
        assertEquals("REQ-02", engine.getWaitingQueue().peek().getRequestId()); // Critical first in queue

        // Free up the ambulance
        engine.updateAmbulanceState("AMB-01", AmbulanceState.HOSPITAL_ARRIVED, 10, 10);

        // Queue must have processed automatically
        assertEquals(0, engine.getWaitingQueue().size());
        assertEquals(EmergencyStatus.ASSIGNED, req2.getStatus());
        assertEquals("AMB-01", req2.getAssignedAmbulance().getId());
    }

    @Test
    public void testInvalidRequestException() {
        assertThrows(InvalidEmergencyRequestException.class, () -> {
            engine.submitEmergencyRequest(new EmergencyRequest("REQ-99", null, Priority.NORMAL, AmbulanceType.BASIC, 0, 0, "Hospital"));
        });
    }
}
