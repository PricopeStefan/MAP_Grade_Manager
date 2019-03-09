package validators;

import domain.Student;

public class StudentValidator implements Validator<Student> {

    /**
     *
     * @param s Studentul care se incearca sa se valideze
     * @throws ValidationException daca sunt gasite probleme in cel putin unul dintre atributele unui student
     */
    public void validate(Student s) throws ValidationException {
        String errs = "";
        if(s.getID() == null || s.getID() == 0)
            errs += "ID INVALID!!!!1!\n";
        if(s.getNume() == null || s.getNume().trim().equals(""))
            errs += "NUME INVALID!!!!1!\n";
        if(s.getEmail() == null || s.getEmail().trim().equals(""))
            errs += "EMAIL INVALID!!!!1!\n";
        if(s.getGrupa() == null || s.getGrupa().trim().equals(""))
            errs += "GRUPA INVALIDA!!!!1!\n";

        if(errs.length() > 0)
            throw new ValidationException(errs);
    }

}
