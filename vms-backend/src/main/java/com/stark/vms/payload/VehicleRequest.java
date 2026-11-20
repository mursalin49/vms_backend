package com.stark.vms.payload;
import com.stark.vms.entity.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record VehicleRequest(
    @NotBlank(message = "Vehicle Number is required") @Size(max = 50) String vehicleNumber,
    @NotBlank(message = "Company is required") String company,
    @NotBlank(message = "Type is required") String type,
    @NotBlank(message = "Brand is required") String brand,
    @NotBlank(message = "Model is required") String model,
    @NotNull(message = "Status is required") VehicleStatus status) {}
