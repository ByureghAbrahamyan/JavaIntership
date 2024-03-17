package entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    private String departmentName;
    private String location;
    private String departmentHead;

    // RelationShip
    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();
    @OneToOne
    private Manager manager;


    public Department(){

    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(departmentId, that.departmentId) && Objects.equals(departmentName, that.departmentName) && Objects.equals(location, that.location) && Objects.equals(departmentHead, that.departmentHead) && Objects.equals(employees, that.employees) && Objects.equals(manager, that.manager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, departmentName, location, departmentHead, employees, manager);
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", location='" + location + '\'' +
                ", departmentHead='" + departmentHead + '\'' +
                ", employees=" + employees +
                ", manager=" + manager +
                '}';
    }
}
