package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.HashMap;

/**
 * class that matches an image to a char representation
 */
public class BrightnessImgCharMatcher implements ChoosenChars {
    private final Image image;
    private final String font;
    private static final int FONT_SIZE = 16;
    private final HashMap<Image, Double> cache = new HashMap<>();


    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     * gets the average brightness of the image
     *
     * @return returns the average brightness of the class image
     */
    public static double getAverageBrightness(Image image) {
        double sum = 0;
        for (Color pixel : image.pixels()) {
            sum += pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152 + pixel.getBlue() * 0.0722;
        }
        return (sum / (image.getWidth() * image.getHeight())) / 255;
    }

    /**
     * displays the given image in char format based on the brightness levels of each sub image
     *
     * @param numCharsInRow number of characters in the row
     * @param charSet       the array of chars to represent the image
     * @return a matrix representation of the image made up of chars from the given charset based on there brightness levels
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        double[] brightnesses = calculateBrightnesses(charSet);
        sortBrightnesses(brightnesses, charSet, 0, charSet.length - 1);
        linearStretch(brightnesses);
        return convertImageToAscii(numCharsInRow, brightnesses, charSet);
    }

    /**
     * preforms a linear stretch on the array of brightnesses
     *
     * @param brightnesses array of brightness representations of the charset
     */
    private static void linearStretch(double[] brightnesses) {
        double maxBrightness = getMaxOrMinBrightness(brightnesses, false);
        double minBrightness = getMaxOrMinBrightness(brightnesses, true);
        if (minBrightness >= maxBrightness) {
            return;
        }
        for (int i = 0; i < brightnesses.length; i++) {
            brightnesses[i] = (brightnesses[i] - minBrightness) / (maxBrightness - minBrightness);
        }
    }

    /**
     * converts the array of characters into an ascii representation based on the size and brightness of the image
     *
     * @param numCharsInRow number of characters in the row
     * @param brightnesses  array of brightness representations of the charset
     * @param charSet       the array of chars to represent the image
     * @return a matrix representation of the image made up of chars from the given charset based on there brightness levels
     */
    private char[][] convertImageToAscii(int numCharsInRow, double[] brightnesses, Character[] charSet) {
        double subImgAvg;
        int j = 0, i = 0;
        int pixels = image.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[image.getHeight() / pixels][image.getWidth() / pixels];
        for (Image subImage : image.squareSubImagesOfSize(pixels)) {
            if(!cache.containsKey(subImage)){
                subImgAvg = getAverageBrightness(subImage);
                cache.put(subImage,subImgAvg);
            }
            asciiArt[i][j] = getClosestChar(cache.get(subImage), brightnesses, charSet);
//            System.out.println("form cache");
            j++;
            if (j >= asciiArt[i].length) {
                j = 0;
                i++;
            }
        }
        return asciiArt;
    }

    /**
     * gets the closest letter to the subImg brightness avg
     *
     * @param subImgAvg average brightness of the subImg
     * @return the closest letter to the avg value
     */
    private char getClosestChar(Double subImgAvg, double[] brightnesses, Character[] charSet) {
        return charSet[closestIndex(brightnesses, subImgAvg)];
    }

    /**
     * binary search implementation for to get the closest char for the average brightness
     *
     * @param brightnesses array of brightness representations of the charset
     * @param subImgAvg    average brightness of the subImg
     * @return index of the value closest to the subImgAvg
     */
    public static int closestIndex(double[] brightnesses, double subImgAvg) {
        int mid;
        int low = 0;
        int high = brightnesses.length - 1;

        while (low <= high) {
            mid = low + ((high - low) / 2);
            if (brightnesses[mid] < subImgAvg) {
                low = mid + 1;
            } else if (brightnesses[mid] > subImgAvg) {
                high = mid-1;
            } else {
                return mid;
            }
        }
        if (low >= brightnesses.length) {
            return brightnesses.length - 1;
        }
        if (Math.abs(subImgAvg - brightnesses[low]) < Math.abs(brightnesses[high] - subImgAvg)) {
            return low;
        }
        return high;
    }

    /**
     * gets the min or max brightness from the list of brightnesses
     *
     * @param brightnesses array of float values representing the char brightnesses
     * @param getMin       boolean value indicating if the desired output is min or max value
     * @return float of the min or max value of in the array
     */
    private static double getMaxOrMinBrightness(double[] brightnesses, Boolean getMin) {
        double max = brightnesses[0];
        double min = brightnesses[0];
        for (double brightness : brightnesses) {
            if (!getMin && brightness > max) {
                max = brightness;
            } else if (getMin && brightness < min) {
                min = brightness;
            }
        }
        if (getMin) {
            return min;
        }
        return max;
    }

    /**
     * calculates the brightness of the array of characters
     *
     * @param charSet array of characters
     * @return and array of floats representing the brightness values of each character
     */
    private double[] calculateBrightnesses(Character[] charSet) {
        boolean[][] image;
        double[] values = new double[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            image = CharRenderer.getImg(charSet[i], FONT_SIZE, font);
            values[i] =  (double)countTrues(image) / (image.length * image[0].length);
        }
        return values;
    }

    /**
     * counts the amount of ture boolean values in the image
     *
     * @param image boolean representation of the image
     * @return the numer of trues in the image
     */
    private static int countTrues(boolean[][] image) {
        int trues = 0;
        for (boolean[] row : image) {
            for (int col = 0; col < image[0].length; col++) {
                if (row[col]) {
                    trues++;
                }
            }
        }
        return trues;
    }

    /**
     * quicksort implementation for sorting the brightnesses
     *
     * @param brightnesses array of float values representing the char brightnesses
     * @param charSet      array of characters
     * @param low          lower index to sort from
     * @param high         upper index to sort up to
     */
    public static void sortBrightnesses(double[] brightnesses, Character[] charSet, int low, int high) {
        if (low < high) {
            int partIndex = partition(brightnesses, charSet, low, high);

            sortBrightnesses(brightnesses, charSet, low, partIndex - 1);
            sortBrightnesses(brightnesses, charSet, partIndex + 1, high);
        }
    }

    /**
     * partition implementation for the sortBrightnesses
     *
     * @param brightnesses array of float values representing the char brightnesses
     * @param charSet      array of characters
     * @param low          lower index to sort from
     * @param high         upper index to sort up to
     * @return the current partition index that is in place
     */
    private static int partition(double[] brightnesses, Character[] charSet, int low, int high) {
        double pivot = brightnesses[high];
        int index = low - 1;

        for (int j = low; j < high; j++) {
            if (brightnesses[j] < pivot) {
                index++;
                swap(brightnesses, charSet, index, j);
            }
        }
        swap(brightnesses, charSet, index + 1, high);
        return index + 1;
    }

    /**
     * swaps the elements in both arrays symmetrically
     *
     * @param brightnesses array of float values representing the char brightnesses
     * @param charSet      array of characters
     * @param index        the index to swap
     * @param j            the index to swap
     */
    private static void swap(double[] brightnesses, Character[] charSet, int index, int j) {
        double saver = brightnesses[index];
        char saveChar = charSet[index];
        brightnesses[index] = brightnesses[j];
        charSet[index] = charSet[j];
        brightnesses[j] = saver;
        charSet[j] = saveChar;
    }
}
