package validators;


public interface Validator<E> {
    /**
     *
     * @param entity Entitatea ce se va valida
     * @throws ValidationException daca sunt gasite neregularitati in interiorul entitatii
     */
    void validate(E entity) throws ValidationException;
}
