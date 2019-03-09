package validators;

public class ValidationException extends RuntimeException {
    /**
     *
     * @param s Stringul cu care va fi instantiata exceptia
     */
    public ValidationException(String s){
        super(s);
    }
}
