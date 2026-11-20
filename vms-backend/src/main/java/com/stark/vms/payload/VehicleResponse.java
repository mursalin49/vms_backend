package com.stark.vms.payload;
import com.stark.vms.entity.Vehicle;
import com.stark.vms.entity.VehicleStatus;
public record VehicleResponse(Long id, String vehicleNumber, String company, String type,
                              String brand, String model, VehicleStatus status) {
    public static VehicleResponse from(Vehicle v) {
        return new VehicleResponse(v.getId(), v.getVehicleNumber(), v.getCompany(), v.getType(),
                v.getBrand(), v.getModel(), v.getStatus());
    }
}
