/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

public class AssignSubjectModelClass {
    private String class_name, subject;

    public AssignSubjectModelClass(String class_name, String subject) {
        this.class_name = class_name;
        this.subject = subject;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
}
