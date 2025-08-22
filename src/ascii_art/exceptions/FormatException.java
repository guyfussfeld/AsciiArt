package ascii_art.exceptions;

/**
 * Custom exception class for handling format-related errors in ASCII art commands.
 */
public class FormatException extends Exception {
    // Error messages for specific command formats
    private static final String ERROR_IMAGE_FORMAT = "Did not change image method due to incorrect format.";
    private static final String ERROR_OUTPUT_FORMAT = "Did not change output method due to incorrect format.";
    private static final String ERROR_ADD_FORMAT = "Did not add due to incorrect format.";
    private static final String ERROR_REMOVE_FORMAT = "Did not remove due to incorrect format.";
    private static final String ERROR_RES_FORMAT = "Did not change resolution due to incorrect format.";
    private static final String ERROR_UNKNOWN_COMMAND_FORMAT = "Unknown command format error.";


    // Command strings corresponding to specific error messages
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_RES = "res";
    private static final String COMMAND_IMAGE = "image";
    private static final String COMMAND_OUTPUT = "output";

    /**
     * Holds the command string that caused the exception
     */
    private final String string;

    /**
     * Constructs a FormatException with the specific command string that caused the error.
     *
     * @param string The command string that caused the format error.
     */
    public FormatException(String string) {
        this.string = string;
    }

    /**
     * Prints the appropriate error message based on the command string provided.
     */
    public void print() {
        switch (string) {
            case COMMAND_ADD:
                System.out.println(ERROR_ADD_FORMAT);
                break;
            case COMMAND_REMOVE:
                System.out.println(ERROR_REMOVE_FORMAT);
                break;
            case COMMAND_RES:
                System.out.println(ERROR_RES_FORMAT);
                break;
            case COMMAND_IMAGE:
                System.out.println(ERROR_IMAGE_FORMAT);
                break;
            case COMMAND_OUTPUT:
                System.out.println(ERROR_OUTPUT_FORMAT);
                break;
            default:
                // Default case to handle any unexpected command strings
                System.out.println(ERROR_UNKNOWN_COMMAND_FORMAT);
                break;
        }
    }
}
