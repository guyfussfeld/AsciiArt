package ascii_art.exceptions;

/**
 * Custom exception class for handling cases where the charset for ASCII art generation is too small.
 */
public class MinCharSetException extends Exception {

    private static final String ERROR_ASCIIART_CHARSET = "Did not execute. Charset is too small.";

    /**
     * Prints the error message indicating that the charset is too small for ASCII art generation.
     */
    public void print() {
        System.out.println(ERROR_ASCIIART_CHARSET);
    }
}
