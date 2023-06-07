/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstscenebuilderapplication;

/**
 *
 * @author DIBB E6430
 */
public class AddSubjectModelClass {
    private String subject_id, subject_name, marks;


    public AddSubjectModelClass(String subject_id, String subject_name, String marks) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.marks = marks;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }
    
}
