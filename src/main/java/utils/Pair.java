package utils;

import java.util.Objects;

public class Pair<X, Y> {
    private X key;
    private Y value;

    public Pair() {

    }

    public Pair(X key, Y  value) {
        this.key = key;
        this.value = value;
    }

    public X getKey() {
        return key;
    }

    public void setKey(X key) {
        this.key = key;
    }

    public Y getValue() {
        return value;
    }

    public void setValue(Y value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if (!(obj instanceof Pair)) {
            return false;
        }

        Pair other = (Pair)obj;
        return this.key == other.key && this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key.toString() + "," + value.toString();
    }


}
