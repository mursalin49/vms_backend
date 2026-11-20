package com.stark.vms.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity
@Table(name = "vehicles", uniqueConstraints = @UniqueConstraint(columnNames = "vehicle_number"))
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "vehicle_number", nullable = false, length = 50) private String vehicleNumber;
    @Column(nullable = false, length = 100) private String company;
    @Column(nullable = false, length = 50) private String type;
    @Column(nullable = false, length = 100) private String brand;
    @Column(nullable = false, length = 100) private String model;
    @Enumerated(EnumType.STRING) @JdbcTypeCode(SqlTypes.VARCHAR) @Column(nullable = false, length = 20) private VehicleStatus status;

    public Long getId() { return id; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String v) { this.vehicleNumber = v; }
    public String getCompany() { return company; }
    public void setCompany(String v) { this.company = v; }
    public String getType() { return type; }
    public void setType(String v) { this.type = v; }
    public String getBrand() { return brand; }
    public void setBrand(String v) { this.brand = v; }
    public String getModel() { return model; }
    public void setModel(String v) { this.model = v; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus v) { this.status = v; }
}
