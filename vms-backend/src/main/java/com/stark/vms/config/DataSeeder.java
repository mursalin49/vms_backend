package com.stark.vms.config;
import com.stark.vms.entity.AppUser;
import com.stark.vms.entity.Vehicle;
import com.stark.vms.entity.VehicleStatus;
import com.stark.vms.repository.AppUserRepository;
import com.stark.vms.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.admin.password}") private String adminPassword;
    @Value("${app.seed.sample-data:true}") private boolean seedSampleData;
    private final AppUserRepository users; private final VehicleRepository vehicles; private final PasswordEncoder enc;
    public DataSeeder(AppUserRepository u, VehicleRepository v, PasswordEncoder e) { users = u; vehicles = v; enc = e; }

    @Override public void run(String... args) {
        // Authorized user is created from the backend on first start (no public sign-up)
        String email = adminEmail.trim().toLowerCase();
        if (users.findByEmail(email).isEmpty()) users.save(new AppUser(email, enc.encode(adminPassword)));
        if (seedSampleData && vehicles.count() == 0) {
            add("DHK-METRO-1021", "Stark Logistics", "Truck", "Tata", "Prima", VehicleStatus.ACTIVE);
            add("DHK-METRO-2044", "Stark Transport", "Van", "Toyota", "HiAce", VehicleStatus.ACTIVE);
            add("CTG-KA-3310", "Stark Logistics", "Pickup", "Isuzu", "D-Max", VehicleStatus.INACTIVE);
            add("DHK-GA-4581", "Stark Motors", "Car", "Honda", "Civic", VehicleStatus.MAINTENANCE);
            add("SYL-HA-5002", "Stark Transport", "Bus", "Hino", "RK8", VehicleStatus.ACTIVE);
        }
    }
    private void add(String n, String c, String t, String b, String m, VehicleStatus s) {
        Vehicle v = new Vehicle();
        v.setVehicleNumber(n); v.setCompany(c); v.setType(t); v.setBrand(b); v.setModel(m); v.setStatus(s);
        vehicles.save(v);
    }
}
