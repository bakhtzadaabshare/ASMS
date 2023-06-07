/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

public class UnPiadTableClass {
    String id;
    String name;
    String fatherName;
    String Address;
    String mobile;
    String paidFee;
    String totalFee;
    String month;
    public UnPiadTableClass(String id, String name, String fatherName, String Address, String mobile, String paidFee, String totalFee, String month) {
        this.id = id;
        this.name = name;
        this.fatherName = fatherName;
        this.Address = Address;
        this.mobile = mobile;
        this.paidFee = paidFee;
        this.totalFee = totalFee;
        this.month = month;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPaidFee(String paidFee) {
        this.paidFee = paidFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getAddress() {
        return Address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPaidFee() {
        return paidFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getMonth() {
        return month;
    }
    
}
