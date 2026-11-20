package com.stark.vms.service;

import com.stark.vms.entity.Vehicle;
import com.stark.vms.entity.VehicleStatus;
import com.stark.vms.exception.DuplicateVehicleException;
import com.stark.vms.exception.NotFoundException;
import com.stark.vms.payload.VehicleRequest;
import com.stark.vms.payload.VehicleResponse;
import com.stark.vms.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    @Mock VehicleRepository repo;
    @InjectMocks VehicleService service;

    private VehicleRequest request(String number) {
        return new VehicleRequest(number, "Stark Logistics", "Truck", "Tata", "Prima", VehicleStatus.ACTIVE);
    }

    @Test
    void createRejectsDuplicateVehicleNumber() {
        when(repo.existsByVehicleNumberIgnoreCase("DHK-1")).thenReturn(true);
        assertThrows(DuplicateVehicleException.class, () -> service.create(request(" dhk-1 ")));
        verify(repo, never()).save(any(Vehicle.class));
    }

    @Test
    void createTrimsAndUppercasesVehicleNumber() {
        when(repo.existsByVehicleNumberIgnoreCase("DHK-1")).thenReturn(false);
        when(repo.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));
        VehicleResponse r = service.create(request(" dhk-1 "));
        assertEquals("DHK-1", r.vehicleNumber());
    }

    @Test
    void updateRejectsNumberUsedByAnotherVehicle() {
        when(repo.findById(5L)).thenReturn(Optional.of(new Vehicle()));
        when(repo.existsByVehicleNumberIgnoreCaseAndIdNot("DHK-1", 5L)).thenReturn(true);
        assertThrows(DuplicateVehicleException.class, () -> service.update(5L, request("DHK-1")));
    }

    @Test
    void getThrowsWhenVehicleMissing() {
        when(repo.findById(9L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(9L));
    }
}
