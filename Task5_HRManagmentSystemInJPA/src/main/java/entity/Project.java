package entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
// @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long projectId;
    private String projectName;
    private String startDate;
    private String endDate;
    private Double budget;

    // RelationShip
    @ManyToMany
    @JoinTable(name = "employee_project",
               joinColumns = @JoinColumn(name = "project_id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id")
    )

    private Set<Employee> employees = new HashSet<>();


    public Project() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectId, project.projectId) && Objects.equals(projectName, project.projectName) && Objects.equals(startDate, project.startDate) && Objects.equals(endDate, project.endDate) && Objects.equals(budget, project.budget) && Objects.equals(employees, project.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, startDate, endDate, budget, employees);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", budget=" + budget +
                ", employees=" + employees +
                '}';
    }
}
