package image;

import java.awt.*;

/**
 * Utility class for image operations such as padding, partitioning, and calculating brightness.
 */
public class ImageUtils {

    private static final int RGB_MAX_VALUE = 255;
    private static final double RED_WEIGHT = 0.2126;
    private static final double GREEN_WEIGHT = 0.7152;
    private static final double BLUE_WEIGHT = 0.0722;

    /**
     * Pads the input image to the nearest power of two dimensions.
     *
     * @param image The input image to pad.
     * @return A new padded Image object.
     */
    public static Image padImage(Image image) {
        // Calculate the nearest power of two dimensions
        int heightUpperPowerOf2 = (int) Math.pow(2, Math.ceil(Math.log(image.getHeight()) / Math.log(2)));
        int widthUpperPowerOf2 = (int) Math.pow(2, Math.ceil(Math.log(image.getWidth()) / Math.log(2)));

        int heightDiff = heightUpperPowerOf2 - image.getHeight();
        int widthDiff = widthUpperPowerOf2 - image.getWidth();

        // check if padding is needed - image dimensions are power of 2
        if (heightDiff == 0 && widthDiff == 0) {
            return image; // No padding needed
        }

        // Create a new padded image array
        Color[][] padImage = new Color[heightUpperPowerOf2][widthUpperPowerOf2];

        // Fill the padded image array
        for (int i = 0; i < heightUpperPowerOf2; i++) {
            for (int j = 0; j < widthUpperPowerOf2; j++) {
                if (i < heightDiff / 2 ||
                        i >= image.getHeight() + (heightDiff / 2) ||
                        j < widthDiff / 2 ||
                        j >= image.getWidth() + (widthDiff / 2)) {
                    padImage[i][j] = Color.WHITE; // Padding with white color
                } else {
                    padImage[i][j] = image.getPixel(i - (heightDiff / 2), j - (widthDiff / 2));
                }
            }
        }

        return new Image(padImage, widthUpperPowerOf2, heightUpperPowerOf2);
    }

    /**
     * Partitions the input image into blocks of specified resolution.
     *
     * @param image The input image to partition.
     * @param res   The resolution (block size) for partitioning.
     * @return A 2D array of Image objects representing the partitioned image.
     */
    public static Image[][] imagePartition(Image image, int res) {
        int block = image.getWidth() / res;

        Image[][] partition2D = new Image[image.getHeight() / block][res];

        // Iterate through the image and create partitions
        for (int i = 0; i < image.getHeight(); i += block) {
            for (int j = 0; j < image.getWidth(); j += block) {
                Color[][] pixelArray = new Color[block][block];

                // Populate pixelArray with pixels from the original image
                for (int x = 0; x < block; x++) {
                    for (int y = 0; y < block; y++) {
                        pixelArray[x][y] = image.getPixel(i + x, j + y);
                    }
                }

                // Create Image object for each partition and store in partition2D array
                partition2D[i / block][j / block] = new Image(pixelArray, block, block);
            }
        }

        return partition2D;

    }

    /**
     * Calculates the average brightness of the input image.
     *
     * @param image The input image to calculate brightness.
     * @return The average brightness value of the image (0 to 1).
     */
    public static double calcImageBrightness(Image image) {
        double greyPixelSum = 0;

        // Iterate through the image and calculate brightness based on RGB values
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color pixel = image.getPixel(i, j);
                greyPixelSum += pixel.getRed() * RED_WEIGHT
                        + pixel.getGreen() * GREEN_WEIGHT
                        + pixel.getBlue() * BLUE_WEIGHT;
            }
        }

        // Calculate and return average brightness
        return greyPixelSum / (image.getHeight() * image.getWidth() * RGB_MAX_VALUE);
    }
}