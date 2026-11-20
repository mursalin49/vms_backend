package com.stark.vms.service;
import com.stark.vms.entity.Vehicle;
import com.stark.vms.payload.VehicleRequest;
import com.stark.vms.payload.VehicleResponse;
import com.stark.vms.repository.VehicleRepository;
import com.stark.vms.exception.DuplicateVehicleException;
import com.stark.vms.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository repo;
    public VehicleService(VehicleRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<VehicleResponse> list() {
        return repo.findAll(Sort.by("id")).stream().map(VehicleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public VehicleResponse get(Long id) { return VehicleResponse.from(find(id)); }

    @Transactional
    public VehicleResponse create(VehicleRequest r) {
        String number = normalize(r.vehicleNumber());
        if (repo.existsByVehicleNumberIgnoreCase(number)) throw new DuplicateVehicleException();
        Vehicle v = new Vehicle();
        apply(v, r, number);
        return VehicleResponse.from(repo.save(v));
    }

    @Transactional
    public VehicleResponse update(Long id, VehicleRequest r) {
        Vehicle v = find(id);
        String number = normalize(r.vehicleNumber());
        if (repo.existsByVehicleNumberIgnoreCaseAndIdNot(number, id)) throw new DuplicateVehicleException();
        apply(v, r, number);
        return VehicleResponse.from(repo.save(v));
    }

    @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    private Vehicle find(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Vehicle not found: " + id));
    }
    private String normalize(String s) { return s.trim().toUpperCase(); }
    private void apply(Vehicle v, VehicleRequest r, String number) {
        v.setVehicleNumber(number);
        v.setCompany(r.company().trim());
        v.setType(r.type().trim());
        v.setBrand(r.brand().trim());
        v.setModel(r.model().trim());
        v.setStatus(r.status());
    }
}
