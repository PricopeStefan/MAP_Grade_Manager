package repository;

import domain.Student;
import validators.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentFileRepository extends AbstractFileRepository<Integer, Student> {
    public StudentFileRepository(String fileName, Validator v) {
        super(fileName, v);
        readFromFile();
    }

    @Override
    protected void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("studenti.txt"))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if(fields.length == 4) {
                    super.save(new Student(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3]));
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Eroare la citire");
        }
    }
}
