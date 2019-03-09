package domain;


import java.util.Objects;

public class Tema implements HasID<Integer>  {
    private Integer temaID, deadLine, dataPrimire;
    private String descriere;

    public Tema(Integer temaID, String descriere, Integer deadLine, Integer dataPrimire) {
        this.temaID = temaID;
        this.descriere = descriere;
        this.deadLine = deadLine;
        this.dataPrimire = dataPrimire;
    }

    @Override
    public Integer getID() {
        return temaID;
    }

    @Override
    public void setID(Integer temaID) {
        this.temaID = temaID;
    }

    public Integer getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Integer deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getDataPrimire() {
        return dataPrimire;
    }

    public void setDataPrimire(Integer dataPrimire) {
        if(dataPrimire < deadLine)
            this.dataPrimire = dataPrimire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }


    @Override
    public String toString() {
        return  temaID +
                "," + descriere +
                "," + deadLine +
                "," + dataPrimire;
    }


    public String toCommandLine() {
        return "Tema{" +
                "temaID=" + temaID +
                ", deadLine=" + deadLine +
                ", dataPrimire=" + dataPrimire +
                ", descriere='" + descriere + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if (!(obj instanceof Tema)) {
            return false;
        }

        Tema other = (Tema)obj;
        return temaID==other.temaID && this.descriere.equals(other.descriere) && deadLine==other.deadLine && dataPrimire == other.dataPrimire;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temaID, descriere, deadLine, dataPrimire);
    }
}
