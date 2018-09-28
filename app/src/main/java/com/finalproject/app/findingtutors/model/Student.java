package com.finalproject.app.findingtutors.model;


public class Student {
    private String id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String gender;
    private String classname;
    private String institution;
    private String deptname;
    private String guardianname;
    private String guardianphone;
    private String type;
    private String thumbnail;

    public Student(){

    }

    public Student(String id, String name, String email, String password, String address, String phone,
                   String gender, String classname, String institution, String deptname, String guardianname,
                   String guardianphone, String type, String thumbnail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.classname = classname;
        this.institution = institution;
        this.deptname = deptname;
        this.guardianname = guardianname;
        this.guardianphone = guardianphone;
        this.type = type;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getGuardianname() {
        return guardianname;
    }

    public void setGuardianname(String guardianname) {
        this.guardianname = guardianname;
    }

    public String getGuardianphone() {
        return guardianphone;
    }

    public void setGuardianphone(String guardianphone) {
        this.guardianphone = guardianphone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
