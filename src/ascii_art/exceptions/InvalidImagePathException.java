package ascii_art.exceptions;

/**
 * Custom exception class for handling invalid image path errors in ASCII art.
 */
public class InvalidImagePathException extends Exception {

    private static final String ERROR_IMAGE_LOAD = "Did not execute due to problem with image file.";

    /**
     * Prints the error message indicating a problem with loading an image file.
     */
    public void print() {
        System.out.println(ERROR_IMAGE_LOAD);
    }
}
