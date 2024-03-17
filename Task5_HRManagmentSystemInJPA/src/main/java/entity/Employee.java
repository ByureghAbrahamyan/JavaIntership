package entity;

import javax.persistence.Entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.util.*;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String name;
    private String email;
    private String phoneNumber;
    private String hireDate;
    private String jobTitle;


    //RelationShips
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new HashSet<>();

    public Employee() {

    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name=" + name +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", hireDate=" + hireDate +
                ", jobTitle=" + jobTitle +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Employee employee = (Employee) object;
        return java.util.Objects.equals(employeeId, employee.employeeId) && java.util.Objects.equals(name, employee.name) && java.util.Objects.equals(email, employee.email) && java.util.Objects.equals(phoneNumber, employee.phoneNumber) && java.util.Objects.equals(hireDate, employee.hireDate) && java.util.Objects.equals(jobTitle, employee.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId, name, email, phoneNumber, hireDate, jobTitle);
    }
}
