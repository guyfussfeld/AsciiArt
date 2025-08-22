package main;

import ascii_art.exceptions.FormatException;
import ascii_art.exceptions.InvalidImagePathException;
import ascii_art.exceptions.MinCharSetException;
import ascii_art.exceptions.ResOutOfBoundException;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import ascii_art.AsciiArtAlgorithm;
import ascii_art.KeyboardInput;

import java.io.IOException;

/**
 * The Shell class manages ASCII art generation and configuration via command-line interface.
 * Handles image loading, character set management, resolution adjustment, and output method selection.
 */
public class Shell {

    // Default values
    private static final String DEFAULT_IMAGE_PATH = "images/lemur.jpeg";
    private static final char[] DEFAULT_CHARSET = {'1', '0', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int DEFAULT_RESOLUTION = 128;
    private static final String DEFAULT_OUTPUT_FILE = "out.html";
    private static final String DEFAULT_OUTPUT_FONT = "Courier New";

    // Miscellaneous constants
    private static final String INPUT_INDICATOR = ">>> ";
    private static final int MIN_CHARSET_SIZE = 2;
    private static final int MIN_RESOLUTION = 2;
    private static final int RES_MULTIPLIER = 2;
    private static final int MIN_COMMAND_PARTS = 2;
    private static final String SPACE = " ";
    private static final int MIN_ASCII_VAL = 32;
    private static final int MAX_ASCII_VAL = 126;
    private static final char HYPHEN = '-';
    private static final char DOT = '.';
    private static final int CHAR_RANGE = 3;

    // Error messages
    private static final String ERROR_INCORRECT_COMMAND = "Did not execute due to incorrect command.";

    // Command strings
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_CHARS = "chars";
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_RES = "res";
    private static final String COMMAND_IMAGE = "image";
    private static final String COMMAND_OUTPUT = "output";
    private static final String COMMAND_ASCIIART = "asciiArt";
    private static final String COMMAND_UP = "up";
    private static final String COMMAND_DOWN = "down";
    private static final String COMMAND_CONSOLE = "console";
    private static final String COMMAND_HTML = "html";
    private static final String ADD_ARGUMENT_SPACE = "space";
    private static final String ADD_ARGUMENT_ALL = "all";

    // Prompt messages
    private static final String MESSAGE_RESOLUTION_SET = "Resolution set to ";

    // Instance variables
    private final SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
    private int resolution = DEFAULT_RESOLUTION;
    private Image image;
    private int maxResolution;
    private int minResolution;
    private final AsciiOutput consoleOutput = new ConsoleAsciiOutput();
    private final AsciiOutput htmlOutput = new HtmlAsciiOutput(DEFAULT_OUTPUT_FILE, DEFAULT_OUTPUT_FONT);
    private AsciiOutput output = consoleOutput;
    private Boolean imageChanged = true;
    private Boolean resChanged = true;

    /**
     * Constructor for the Shell class.
     * Initializes the shell with a default image.
     * Catches FormatException and InvalidImagePathException if encountered during initialization.
     */
    public Shell() {
        try {
            // Set default image path
            setImage(new String[]{COMMAND_IMAGE, DEFAULT_IMAGE_PATH});
        } catch (FormatException e) {
            e.print();
        } catch (InvalidImagePathException e) {
            e.print();
        }
    }

    /**
     * Main method for the Shell class.
     * Creates a new Shell instance and runs it.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        new Shell().run();
    }

    /**
     * Runs the shell, reading commands from the user and executing them.
     * The shell will continue to run until the user enters the "exit" command.
     * If an exception is thrown during the execution of a command, the shell will print an error message.
     */
    public void run() {
        System.out.print(INPUT_INDICATOR);
        String command = KeyboardInput.readLine();

        while (!command.equals(COMMAND_EXIT)) {
            try {
                // Split command into parts
                String[] commandParts = command.split(SPACE);
                String commandName = commandParts[0];

                switch (commandName) {
                    case COMMAND_CHARS:
                        printAllowedChars();
                        break;
                    case COMMAND_ADD:
                        addChars(commandParts);
                        break;
                    case COMMAND_REMOVE:
                        removeChars(commandParts);
                        break;
                    case COMMAND_RES:
                        setResolution(commandParts);
                        break;
                    case COMMAND_IMAGE:
                        setImage(commandParts);
                        break;
                    case COMMAND_OUTPUT:
                        setOutput(commandParts);
                        break;
                    case COMMAND_ASCIIART:
                        runAsciiArt();
                        break;
                    default:
                        System.out.println(ERROR_INCORRECT_COMMAND);
                        break;
                }
            } catch (FormatException e) {
                e.print();
            } catch (ResOutOfBoundException e) {
                e.print();
            } catch (MinCharSetException e) {
                e.print();
            } catch (InvalidImagePathException e) {
                e.print();
            }

            System.out.print(INPUT_INDICATOR);
            command = KeyboardInput.readLine();
        }
    }

    /**
     * Prints the characters that are allowed to be used in the ASCII art.
     */
    private void printAllowedChars() {
        char[] chars = subImgCharMatcher.getCurrChars();
        for (char c : chars) {
            System.out.print(c + SPACE);
        }
        System.out.println();
    }

    /**
     * Adds characters to the list of characters that are allowed to be used in the ASCII art.
     *
     * @param commandParts The parts of the command that was entered by the user.
     * @throws FormatException If the command format is incorrect.
     */
    private void addChars(String[] commandParts) throws FormatException {
        if (commandParts.length < MIN_COMMAND_PARTS) {
            throw new FormatException(COMMAND_ADD);
        }

        String argument = commandParts[1];

        // Add single char
        if (argument.length() == 1
                && MAX_ASCII_VAL >= argument.charAt(0)
                && argument.charAt(0) >= MIN_ASCII_VAL) {
            subImgCharMatcher.addChar(argument.charAt(0));
            return;
        }
        // Add all chars in range
        if (argument.equals(ADD_ARGUMENT_ALL)) {
            for (char c = MIN_ASCII_VAL; c <= MAX_ASCII_VAL; c++) {
                subImgCharMatcher.addChar(c);
            }
            return;
        }
        // Add space
        if (argument.equals(ADD_ARGUMENT_SPACE)) {
            subImgCharMatcher.addChar(SPACE.charAt(0));
            return;
        }
        // Add certain range
        if (argument.length() == CHAR_RANGE
                && MAX_ASCII_VAL >= argument.charAt(0)
                && argument.charAt(0) >= MIN_ASCII_VAL
                && MAX_ASCII_VAL >= argument.charAt(CHAR_RANGE-1)
                && argument.charAt(CHAR_RANGE-1) >= MIN_ASCII_VAL
                && argument.charAt(1) == HYPHEN) {
            char max = (char) Math.max(argument.charAt(0), argument.charAt(CHAR_RANGE-1));
            char min = (char) Math.min(argument.charAt(0), argument.charAt(CHAR_RANGE-1));
            for (char c = min; c <= max; c++) {
                subImgCharMatcher.addChar(c);
            }
            return;
        }
        throw new FormatException(COMMAND_ADD);
    }

    /**
     * Removes characters from the list of characters that are allowed to be used in the ASCII art.
     *
     * @param commandParts The parts of the command that was entered by the user.
     * @throws FormatException If the command format is incorrect.
     */
    private void removeChars(String[] commandParts) throws FormatException {
        if (commandParts.length < MIN_COMMAND_PARTS) {
            throw new FormatException(COMMAND_REMOVE);
        }

        String argument = commandParts[1];

        // Remove single char
        if (argument.length() == 1) {
            subImgCharMatcher.removeChar(argument.charAt(0));
            return;
        }
        // Remove all chars in range
        if (argument.equals(ADD_ARGUMENT_ALL)) {
            for (char c = MIN_ASCII_VAL; c <= MAX_ASCII_VAL; c++) {
                subImgCharMatcher.removeChar(c);
            }
            return;
        }
        // Remove space
        if (argument.equals(ADD_ARGUMENT_SPACE)) {
            subImgCharMatcher.removeChar(SPACE.charAt(0));
            return;
        }
        // Remove certain range
        if (argument.length() == CHAR_RANGE && argument.charAt(1) == HYPHEN) {
            char max = (char) Math.max(argument.charAt(0), argument.charAt(CHAR_RANGE-1));
            char min = (char) Math.min(argument.charAt(0), argument.charAt(CHAR_RANGE-1));
            for (char c = min; c <= max; c++) {
                subImgCharMatcher.removeChar(c);
            }
            return;
        }
        throw new FormatException(COMMAND_REMOVE);
    }

    /**
     * Sets the resolution for ASCII art generation.
     *
     * @param commandParts The parts of the command that was entered by the user.
     * @throws FormatException        If the command format is incorrect.
     * @throws ResOutOfBoundException If the new resolution exceeds boundaries.
     */
    private void setResolution(String[] commandParts) throws FormatException, ResOutOfBoundException {
        if (commandParts.length > 1) {

            String option = commandParts[1];
            if (option.equals(COMMAND_UP)) {
                if (resolution * RES_MULTIPLIER > maxResolution) {
                    throw new ResOutOfBoundException();
                }
                resolution *= RES_MULTIPLIER;
                resChanged = true;
            } else if (option.equals(COMMAND_DOWN)) {
                if (resolution / RES_MULTIPLIER < minResolution) {
                    throw new ResOutOfBoundException();
                }
                resolution /= RES_MULTIPLIER;
                resChanged = true;
            } else {
                throw new FormatException(COMMAND_RES);
            }
        }

        System.out.println(MESSAGE_RESOLUTION_SET + resolution + DOT);
    }

    /**
     * Sets the image to the file path specified by the user.
     * Updates resolution and related properties based on the loaded image.
     *
     * @param commandParts The parts of the command that was entered by the user.
     * @throws FormatException If the command format is incorrect.
     * @throws InvalidImagePathException If the image path is invalid or the image cannot be loaded.
     */
    private void setImage(String[] commandParts) throws FormatException, InvalidImagePathException {
        if (commandParts.length < MIN_COMMAND_PARTS) {
            throw new FormatException(COMMAND_IMAGE);
        }

        String filePath = commandParts[1];

        // Attempt to load the image and set the resolution, min resolution, and max resolution
        try {
            image = new Image(filePath);
        } catch (IOException e) {
            throw new InvalidImagePathException();
        }
        maxResolution = image.getWidth();
        minResolution = Math.max(1, image.getWidth() / image.getHeight());

        // Check if resolution exceeds for the new image
        if (resolution > maxResolution) {
            resolution = MIN_RESOLUTION;
        }
        imageChanged = true;
    }

    /**
     * Sets the output method to either console or HTML based on user input.
     *
     * @param commandParts The parts of the command that was entered by the user.
     * @throws FormatException If the command format is incorrect.
     */
    private void setOutput(String[] commandParts) throws FormatException {
        if (commandParts.length < MIN_COMMAND_PARTS) {
            throw new FormatException(COMMAND_OUTPUT);
        }

        String option = commandParts[1];
        // Set the output method or print an error message if the format is incorrect
        switch (option) {
            case COMMAND_CONSOLE:
                output = consoleOutput;
                break;
            case COMMAND_HTML:
                output = htmlOutput;
                break;
            default:
                throw new FormatException(COMMAND_OUTPUT);
        }
    }

    /**
     * Generates ASCII art based on the current settings and outputs it using the selected output method.
     *
     * @throws MinCharSetException If the character set is too small to generate ASCII art.
     */
    private void runAsciiArt() throws MinCharSetException {
        // If charset is too small, print error message and return
        if (subImgCharMatcher.getCurrChars().length < MIN_CHARSET_SIZE) {
            throw new MinCharSetException();
        }

        AsciiArtAlgorithm asciiArtAlgorithm;
        // not using the save image partition Brightnesses
        if (imageChanged || resChanged) {
            asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, subImgCharMatcher, false);
        }
        // using the save image partition Brightnesses
        else{
            asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, subImgCharMatcher, true);
        }
        // Run the algorithm and output the result
        char[][] art = asciiArtAlgorithm.run();
        output.out(art);

        // Update last run variables
        imageChanged = false;
        resChanged = false;
    }
}