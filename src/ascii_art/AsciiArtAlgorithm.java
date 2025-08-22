package ascii_art;

import image.Image;
import image.ImageUtils;
import image_char_matching.SubImgCharMatcher;

/**
 * Generates ASCII art from an image using a specified character set and resolution.
 */
public class AsciiArtAlgorithm {

    // save the last image partition rightness
    private static double[][] lastImagePartitionBrightness;

    private final Image image;
    private final int res;
    private final SubImgCharMatcher matcher;
    private final boolean use;

    /**
     * Constructor for AsciiArtAlgorithm class.
     *
     * @param image   The image to convert to ASCII art.
     * @param res     The resolution for ASCII art generation.
     * @param matcher The character set used for ASCII art representation.
     */
    public AsciiArtAlgorithm(Image image, int res, SubImgCharMatcher matcher, boolean use) {
        this.image = image;
        this.res = res;
        this.matcher = matcher;
        this.use = use;
    }

    /**
     * Runs the ASCII art generation algorithm.
     *
     * @return A 2D array representing the ASCII art of the image.
     */
    public char[][] run() {
        // Pad the image and partition it into smaller images
        Image paddedImage = ImageUtils.padImage(image);
        Image[][] imagePartition = ImageUtils.imagePartition(paddedImage, res);

        // Initialize a 2D array to store the generated ASCII art
        char[][] ASCIIArt = new char[imagePartition.length][imagePartition[0].length];

        // first run on this image and res
        if (!use) {
            lastImagePartitionBrightness = new double[imagePartition.length][imagePartition[0].length];

            // Generate ASCII art for each partitioned image
            for (int i = 0; i < imagePartition.length; i++) {
                for (int j = 0; j < imagePartition[0].length; j++) {
                    // Calculate brightness of the image partition and map it to a character from the charset
                    double partitionBrightness = ImageUtils.calcImageBrightness(imagePartition[i][j]);
                    lastImagePartitionBrightness[i][j] = partitionBrightness;
                    ASCIIArt[i][j] = matcher.getCharByImageBrightness(partitionBrightness);
                }
            }
        }
        // not the first time with this image and res
        else {
            // Generate ASCII art for each partitioned image
            for (int i = 0; i < imagePartition.length; i++) {
                for (int j = 0; j < imagePartition[0].length; j++) {
                    // Use the calculation from previous instance run
                    ASCIIArt[i][j] = matcher.getCharByImageBrightness(lastImagePartitionBrightness[i][j]);
                }
            }
        }

        return ASCIIArt;
    }
}