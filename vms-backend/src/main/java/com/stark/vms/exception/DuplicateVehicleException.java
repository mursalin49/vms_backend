package com.stark.vms.exception;
public class DuplicateVehicleException extends RuntimeException {
    public DuplicateVehicleException() { super("This vehicle number already exists."); }
}
