package validators;

import domain.Profesor;

public class ProfesorValidator implements Validator<Profesor> {
    public void validate(Profesor s) throws ValidationException {
        String errs = "";
        if(s.getID() == null || s.getID() == 0)
            errs += "ID INVALID!!!!1!\n";
        if(s.getNume() == null || s.getNume().trim().equals(""))
            errs += "NUME INVALID!!!!1!\n";
        if(s.getEmail() == null || s.getEmail().trim().equals(""))
            errs += "EMAIL INVALID!!!!1!\n";

        if(errs.length() > 0)
            throw new ValidationException(errs);
    }

}
