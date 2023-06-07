/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

public class SetStaffSalaryTable {
    String id;
    String name;
    String father_name;
    String designation;
    String salary;


    public SetStaffSalaryTable(String id, String name, String father_name, String designation, String salary) {
        this.id = id;
        this.name = name;
        this.father_name = father_name;
        this.designation = designation;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFather_name() {
        return father_name;
    }

    public String getDesignation() {
        return designation;
    }
        public String getSalary() {
        return salary;
    }

     public void setSalary(String salary) {
        this.salary = salary;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
}
