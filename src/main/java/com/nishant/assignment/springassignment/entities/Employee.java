package com.nishant.assignment.springassignment.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "phone"})})
public class Employee {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Id
    private Integer empId;
    @NotNull
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String dob;
    private Long phone;
    private Boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deptId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Department department;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Task> tasks = new HashSet<>();

    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;

        for (Task t : tasks) {
            t.setEmployee(this);
        }
    }

    public enum Gender {
        male, female, other, doNotWantToSpecify;
    }
}
