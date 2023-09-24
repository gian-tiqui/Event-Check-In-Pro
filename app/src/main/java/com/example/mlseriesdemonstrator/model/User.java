package com.example.mlseriesdemonstrator.model;

public class User {

    private String lastName;
    private String firstName;
    private String middleName;
    private String role;
    private String studentID;
    private String course;
    private String UID;
    private String password;
    private double[][] faceVector;

    public User() {

    }

    public User(
            String lastName,
            String firstName,
            String middleName,
            String role,
            String studentID,
            String course,
            String UID,
            String password
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.role = role;
        this.studentID = studentID;
        this.course = course;
        this.UID = UID;
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double[][] getFaceVector() {
        return faceVector;
    }

    public void setFaceVector(double[][] faceVector) {
        this.faceVector = faceVector;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
