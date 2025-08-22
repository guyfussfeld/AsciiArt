package image_char_matching;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashMap;

/**
 * A class that matches characters to image brightness values and manages character sets dynamically.
 */
public class SubImgCharMatcher {
    private static final int SUB_IMG_SIZE = 16;
    private final TreeMap<Double, TreeSet<Character>> charBrightnessSortedMap = new TreeMap<>();
    private final TreeSet<Character> currChars = new TreeSet<>();
    private final HashMap<Character, Double> brightnessCalcs = new HashMap<>();

    /**
     * Constructs a SubImgCharMatcher object with an initial set of characters.
     *
     * @param charset The initial set of characters to manage.
     */
    public SubImgCharMatcher(char[] charset) {
        for (char c : charset) {
            // Add each character to the matcher
            addChar(c);
        }
    }

    /**
     * Retrieves the closest character representation based on the given image brightness.
     *
     * @param brightness The brightness value of the image (0 to 1).
     * @return The closest matching character.
     */
    public char getCharByImageBrightness(double brightness) {
        // Determine the range of brightness values in the sorted map
        double minBrightness = charBrightnessSortedMap.firstKey();
        double maxBrightness = charBrightnessSortedMap.lastKey();

        // Reverse the equation to find the true char brightness value
        double originalCharBrightness = brightness * (maxBrightness - minBrightness) + minBrightness;

        // Find the closest brightness key in the map and return the corresponding character
        double closestBrightness = findClosestKey(charBrightnessSortedMap, originalCharBrightness);
        return charBrightnessSortedMap.get(closestBrightness).first();
    }

    /**
     * Finds the closest key in the TreeMap around the given value.
     *
     * @param map   The TreeMap containing brightness keys and character sets.
     * @param value The brightness value to find closest key for.
     * @return The closest key in the TreeMap to the given value.
     */
    private Double findClosestKey(TreeMap<Double, TreeSet<Character>> map, Double value) {
        // Find the closest keys around the given value
        Double floorKey = map.floorKey(value);
        Double ceilingKey = map.ceilingKey(value);

        // Handle edge cases where floor or ceiling key might be null
        if (floorKey == null) return ceilingKey;
        if (ceilingKey == null) return floorKey;

        // Determine which key is closer to the given value
        if (Math.abs(value - floorKey) <= Math.abs(value - ceilingKey)) {
            return floorKey;
        } else {
            return ceilingKey;
        }
    }

    /**
     * Adds a character to the matcher along with its calculated brightness.
     *
     * @param c The character to add.
     */
    public void addChar(char c) {
        // Add character to current characters set
        currChars.add(c);

        // Calculate brightness if not already calculated
        if (!brightnessCalcs.containsKey(c)) {
            brightnessCalcs.put(c, calcCharBrightness(c));
        }

        double charBrightness = brightnessCalcs.get(c);

        // Add character to the sorted map based on its brightness
        if (charBrightnessSortedMap.containsKey(charBrightness)) {
            charBrightnessSortedMap.get(charBrightness).add(c);
        } else {
            TreeSet<Character> charSet = new TreeSet<>();
            charSet.add(c);
            charBrightnessSortedMap.put(charBrightness, charSet);
        }
    }

    /**
     * Removes a character from the matcher.
     *
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        if (!brightnessCalcs.containsKey(c)) {
            return; // Character not found
        }

        Double charBrightness = brightnessCalcs.get(c);

        // Remove character from the sorted map and current characters set
        if (charBrightnessSortedMap.containsKey(charBrightness)) {
            TreeSet<Character> currSet = charBrightnessSortedMap.get(charBrightness);
            currSet.remove(c);
            currChars.remove(c);

            // Remove brightness entry if the set is now empty
            if (currSet.isEmpty()) {
                charBrightnessSortedMap.remove(charBrightness);
            }
        }
    }

    /**
     * Calculates the brightness of a character based on its representation.
     *
     * @param c The character to calculate brightness for.
     * @return The brightness value of the character (0 to 1).
     */
    private Double calcCharBrightness(char c) {
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        int counter = 0;

        // Count the number of true (black) pixels in the character representation
        for (int i = 0; i < SUB_IMG_SIZE; i++) {
            for (int j = 0; j < SUB_IMG_SIZE; j++) {
                counter += boolArray[i][j] ? 1 : 0;
            }
        }

        // Calculate and return the brightness as a ratio of black pixels to total pixels
        return (double) counter / (SUB_IMG_SIZE * SUB_IMG_SIZE);
    }

    /**
     * Retrieves an array of current characters managed by the matcher.
     *
     * @return An array of characters currently managed.
     */
    public char[] getCurrChars() {
        char[] chars = new char[currChars.size()];
        int i = 0;
        for (char c : currChars) {
            chars[i++] = c;
        }
        return chars;
    }
}