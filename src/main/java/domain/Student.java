package domain;


import java.util.Objects;

public class Student implements HasID<Integer> {
    private String nume, grupa, email;
    private Integer idstudent;

    public Student(Integer idstudent, String nume, String grupa, String email) {
        this.idstudent = idstudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Integer getID() {
        return idstudent;
    }

    @Override
    public void setID(Integer idstudent) {
        this.idstudent = idstudent;
    }

    /**
     * Override la metoda ToString a unui obiect
     * @return Atributele studentului sub un string CSV
     */
    @Override
    public String toString() {
        return  idstudent.toString() + ',' +
                nume + ',' +
                grupa + ',' +
                email;
    }


    public String toCommandLine() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", grupa='" + grupa + '\'' +
                ", email='" + email + '\'' +
                ", idStudent=" + idstudent +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if (!(obj instanceof Student)) {
            return false;
        }

        Student other = (Student)obj;
        return this.idstudent == other.idstudent && this.nume.equals(other.nume) && this.grupa.equals(other.grupa) && this.email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idstudent, nume, grupa, email);
    }
}
