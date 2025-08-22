package ascii_art.exceptions;

/**
 * Custom exception for resolution exceeding predefined boundaries in ASCII art generation.
 */
public class ResOutOfBoundException extends Exception {

    private static final String ERROR_RES_BOUNDS = "Did not change resolution due to exceeding boundaries.";

    /**
     * Prints the error message indicating that the resolution change exceeds boundaries.
     */
    public void print() {
        System.out.println(ERROR_RES_BOUNDS);
    }
}
