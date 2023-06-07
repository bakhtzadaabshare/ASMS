/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentInfoTable {
    private final SimpleStringProperty name;
    String id;
    String fatherName;
    String FatherNic;
    String Address;
    String dob;
    String mobile;
    String classOfAdmissin;
    String dateOfAdmission;
    String caste;
    String currentClass;
    public StudentInfoTable(String id, String name, String fatherName, String FatherNic, String Address, String dob, String mobile, String classOfAdmissin, String dateOfAdmission, String caste, String currentClass) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.fatherName = fatherName;
        this.FatherNic = FatherNic;
        this.Address = Address;
        this.dob = dob;
        this.mobile = mobile;
        this.classOfAdmissin = classOfAdmissin;
        this.dateOfAdmission = dateOfAdmission;
        this.caste = caste;
        this.currentClass = currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getFatherNic() {
        return FatherNic;
    }

    public String getAddress() {
        return Address;
    }

    public String getDob() {
        return dob;
    }

    public String getMobile() {
        return mobile;
    }

    public String getClassOfAdmissin() {
        return classOfAdmissin;
    }

    public String getDateOfAdmission() {
        return dateOfAdmission;
    }

    public String getCaste() {
        return caste;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setFatherNic(String FatherNic) {
        this.FatherNic = FatherNic;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setClassOfAdmissin(String classOfAdmissin) {
        this.classOfAdmissin = classOfAdmissin;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }
    public StringProperty nameProperty() {
        return name;
    }
   
}
