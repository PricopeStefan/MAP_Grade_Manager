package domain;

import java.util.Objects;

public class Profesor implements HasID<Integer> {
    private Integer id;
    private String nume, email;

    public Profesor(Integer id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.id + "," + this.nume + "," + this.email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return id.equals(profesor.id) &&
                nume.equals(profesor.nume) &&
                email.equals(profesor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume, email);
    }
}
