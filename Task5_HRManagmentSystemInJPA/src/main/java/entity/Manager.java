package entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Manager extends Employee{

    private String managedDepartment;
    @Enumerated(EnumType.STRING)
    private ManagmentLevel managmentLevel;

    public Manager(){
    }

    public String getManagedDepartment() {
        return managedDepartment;
    }

    public void setManagedDepartment(String managedDepartment) {
        this.managedDepartment = managedDepartment;
    }

    public ManagmentLevel getManagmentLevel() {
        return managmentLevel;
    }

    public void setManagmentLevel(ManagmentLevel managmentLevel) {
        this.managmentLevel = managmentLevel;
    }
}
