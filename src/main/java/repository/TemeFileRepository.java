package repository;

import domain.Tema;
import validators.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TemeFileRepository extends AbstractFileRepository<Integer, Tema> {

    public TemeFileRepository(String fileName, Validator v) {
        super(fileName, v);
        readFromFile();
    }

    @Override
    protected void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("teme.txt"))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if(fields.length == 4) {
                    super.save(new Tema(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3])));
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Eroare la citire");
        }
    }
}
