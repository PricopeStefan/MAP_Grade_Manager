package repository;

import domain.HasID;
import validators.ValidationException;
import validators.Validator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public abstract class AbstractFileRepository<ID, E extends HasID<ID>> extends ConcreteCrudRepository<ID, E> {
    protected String filename;

    public AbstractFileRepository(String filename, Validator vali) {
        super(vali);
        this.filename = filename;
    }

    protected abstract void readFromFile() throws IOException;

    private void writeToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        Iterable<E> elems = super.findAll();

        elems.forEach(elem -> {
            try {
                writer.write(elem.toString());
                writer.write('\n');
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        writer.close();
    }

    @Override
    public E save(E entity) throws ValidationException {
        E returnat = super.save(entity);
        try {
            writeToFile();
            return returnat;
        }
        catch (IOException ex) {
            System.out.println("Eroare la scrierea in fisier");
        }
        return null;
    }

    @Override
    public E delete(ID id) {
        E returnat = super.delete(id);
        try {
            writeToFile();
            return returnat;
        }
        catch (IOException ex) {
            System.out.println("Eroare la scrierea in fisier");
        }
        return null;
    }

    @Override
    public E update(E entity) {
        E rez = super.update(entity);
        try {
            writeToFile();
            return rez;
        }
        catch (IOException ex) {
            System.out.println("Eroare la scrierea in fisier");
        }
        return null;
    }
}
