package validators;

import domain.Tema;

public class TemaValidator implements Validator<Tema> {

    /**
     *
     * @param t Tema pe care va incerca sa o valideze
     * @throws ValidationException daca sunt gasite probleme in cel putin unul dintre atributele temei
     */
    @Override
    public void validate(Tema t) throws ValidationException {
        String errs = "";
        if(t.getID() == null || t.getID() == 0)
            errs += "ID INVALID!!!!1!\n";
        if(t.getDescriere() == null || t.getDescriere().trim().equals(""))
            errs += "DESCRIERE INVALIDA!!!!1!\n";
        if(t.getDeadLine() == null || t.getDeadLine() < 1 || t.getDeadLine() > 14)
            errs += "DEADLINE INVALID!!!!1!\n";
        if(t.getDataPrimire() == null || t.getDataPrimire() < 1 || t.getDataPrimire() > 14)
            errs += "DATA PRIMIRE INVALIDA!!!!1!\n";

        if(errs.length() > 0)
            throw new ValidationException(errs);
    }
}
