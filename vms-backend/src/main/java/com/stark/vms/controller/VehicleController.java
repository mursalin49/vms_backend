package com.stark.vms.controller;
import com.stark.vms.payload.VehicleRequest;
import com.stark.vms.payload.VehicleResponse;
import com.stark.vms.service.VehicleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {
    private final VehicleService service;
    public VehicleController(VehicleService service) { this.service = service; }

    @GetMapping public List<VehicleResponse> list() { return service.list(); }
    @GetMapping("/{id}") public VehicleResponse get(@PathVariable Long id) { return service.get(id); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody VehicleRequest r) { return service.create(r); }

    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable Long id, @Valid @RequestBody VehicleRequest r) { return service.update(id, r); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
