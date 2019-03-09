package domain;

import utils.Pair;

import java.util.Objects;


public class Nota implements HasID<Pair<Integer, Integer>> {
    private Student student;
    private Tema tema;
    private Integer valoare, predataPe;
    private Profesor profesor;

    public Nota(Student student, Tema tema,Integer valoare, Integer predataPe, Profesor profesor) {
        this.student = student;
        this.tema = tema;
        this.valoare = valoare;
        this.predataPe = predataPe;
        this.profesor = profesor;
    }

    @Override
    public Pair<Integer, Integer> getID() {
        return new Pair<>(student.getID(), tema.getID());
    }

    @Override
    public void setID(Pair<Integer, Integer> ids) {
        student.setID(ids.getKey());
        tema.setID(ids.getValue());
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Integer getPredataPe() {
        return predataPe;
    }

    public void setPredataPe(Integer predataPe) {
        this.predataPe = predataPe;
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
    @Override
    public String toString() {
        return  student +
                "," + tema +
                "," + valoare +
                "," + predataPe +
                "," + profesor;
    }

    public String toCommandLine() {
        return "Nota{" +
                student.toCommandLine() +
                "," + tema.toCommandLine() +
                ", valoare=" + valoare +
                ", predataPe=" + predataPe +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if (!(obj instanceof Nota)) {
            return false;
        }

        Nota other = (Nota)obj;
        return this.student.equals(other.student) && this.tema.equals(other.tema) && this.valoare == other.valoare && this.predataPe == other.predataPe && this.profesor == other.profesor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, tema, valoare, predataPe, profesor);
    }
}
