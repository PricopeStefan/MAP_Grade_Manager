package validators;

import domain.Nota;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(Nota n) {
        String errs = "";

        if(n.getStudent() == null)
            errs += "STUDENT INEXISTENT!!\n";
        if(n.getTema() == null)
            errs += "TEMA INEXISTENTA!\n";
        if(n.getValoare() == null || n.getValoare() < 1 || n.getValoare() > 10)
            errs += "NOTA INVALIDA!!!!1!\n";
        if(n.getPredataPe() == null || n.getPredataPe() < 1 || n.getPredataPe() > 14)
            errs += "DATA PREDARE INVALIDA!!!!1!\n";

        if(errs.length() > 0)
            throw new ValidationException(errs);
    }
}
