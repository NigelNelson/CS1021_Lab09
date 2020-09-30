/*
 * Course: CS1021 - 021
 * Winter
 * Lab 9 - Final Project Continued
 * Name: Nigel Nelson
 * Created: 02/19/20
 */
package msoe.nelsonni.lab09;

import edu.msoe.cs1021.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class responsible for the reading
 * and writing of files
 */
public class ImageIO {

    /**
     * char sequence specifying the first four bytes of a BMSOE file.
     */
    private static final char[] BMSOE_FILE_HEADER = {'B', 'M', 'S', 'O', 'E'};
    /**
     * Integer value of a color or alpha
     */
    private static final double COLOR_RANGE = 255.0;
    /**
     * Integer value associated with the format of colors stored in hex format
     */
    private static final int COLOR_FORMAT = 0x000000FF;
    /**
     * Integer value associated with the index of the green color info
     * in a .bmsoe file
     */
    private static final int GREEN_INDEX = 8;
    /**
     * Integer value associated with the index of the red color info
     * in a .bmsoe file
     */
    private static final int RED_INDEX = 16;
    /**
     * Integer value associated with the index of the alpha color info
     * in a .bmsoe file
     */
    private static final int ALPHA_INDEX = 24;

    /**
     * Reads in the specified image file and returns a javafx.scene.image.Image object containing
     * the image.
     *
     * @param path the specified image file
     * @return image from the specified file
     * @throws IOException when an invalid extension is entered
     */
    public static Image read(Path path) throws IOException {
        Image image = null;
        String pathString = path.toString();
        String extension = pathString.substring(pathString.lastIndexOf("."));
        if (extension.equals(".png") || extension.equals(".jpg")) {
            image = ImageUtil.readImage(path);
        } else if (extension.equals(".msoe")) {
            image = readMSOE(path);
        } else if (extension.equals(".bmsoe")) {
            image = readBMSOE(path);
        } else {
            throw new IllegalArgumentException();
        }
        return image;
    }

    /**
     * Writes the specified image to the specified file.
     *
     * @param image image to be saved
     * @param path  new image path to be created
     * @throws IOException when invalid extension is entered
     */
    public static void write(Image image, Path path) throws IOException {
        String stringPath = path.toString();
        String extension = stringPath.substring(stringPath.lastIndexOf("."));
        if (extension.equals(".png") || extension.equals(".jpg")) {
            ImageUtil.writeImage(path, image);
        } else if (extension.equals(".msoe")) {
            writeMSOE(image, path);
        } else if (extension.equals(".bmsoe")) {
            writeBMSOE(image, path);
        } else {
            throw new IOException();
        }
    }

    /**
     * Reads a specified MSOE image
     *
     * @param path the path associated with the image that is to be displayed
     * @return The image created from the path object
     * @throws IOException when invalid file is entered
     * @throws InputMismatchException when invalid color is used
     * @throws IndexOutOfBoundsException when scanner tries to read invalid location
     * @throws IllegalArgumentException when the header is invalid
     */
    private static Image readMSOE(Path path) throws IOException,
            InputMismatchException, IndexOutOfBoundsException, IllegalArgumentException {
        String header;
        String[] dimensions;
        int width;
        int height;
        String[] pixels;
        Color color;
        WritableImage image;
        PixelWriter writer;


        try (Scanner scanner = new Scanner(path)) {
            header = scanner.nextLine();
            if (!header.equals("MSOE")) {
                throw new IllegalArgumentException();
            }
            dimensions = scanner.nextLine().split("\\s+");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);


            image = new WritableImage(width, height);
            writer = image.getPixelWriter();

            if (width < 0 || height < 0) {
                throw new IllegalArgumentException();
            }
            for (int row = 0; row < height; row++) {
                pixels = scanner.nextLine().split("\\s+");
                for (int column = 0; column < width; column++) {
                    color = Color.web(pixels[column]);
                    writer.setColor(column, row, color);
                }
            }
        }
        return image;
    }

    /**
     * Writes a specified .msoe image to a certain path.
     *
     * @param image the image that is to be written to a path
     * @param path  the path that the image is to be written to
     * @throws IOException when PrintWriter cannot be created with
     * the path instance
     * @throws IllegalArgumentException when pixel reader cannot read
     * the image
     */
    private static void writeMSOE(Image image, Path path) throws IOException,
            IllegalArgumentException {
        int width;
        int height;
        String pixel;
        File file = path.toFile();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            width = (int) image.getWidth();
            height = (int) image.getHeight();
            writer.println("MSOE");
            writer.println(width + " " + height);
            PixelReader reader = image.getPixelReader();

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    pixel = (reader.getColor(column, row)).toString();
                    writer.print(pixel + " ");
                }
                writer.println();
            }
        }
    }

    /**
     * Reads a specified .bmsoe image
     *
     * @param path the path associated with the image that is to be displayed
     * @return The image created from the path object
     * @throws IOException when InputStream cannot be created from the path
     * @throws IndexOutOfBoundsException when InputStream tries to access
     * an invalid data point
     * @throws NegativeArraySizeException when there is a negative array
     * size discovered
     * @throws InputMismatchException when the path header doesnt equal
     * "bmsoe"
     */
    private static Image readBMSOE(Path path) throws IOException,
            InputMismatchException, IndexOutOfBoundsException, NegativeArraySizeException {
        WritableImage image = null;
        PixelWriter writer;

        try (DataInputStream data = new DataInputStream(new BufferedInputStream(
                new FileInputStream(path.toFile())))) {
            char[] header = new char[BMSOE_FILE_HEADER.length];
            int width;
            int height;
            Color color;
            double r;
            double g;
            double b;
            double a;

            for (int i = 0; i < BMSOE_FILE_HEADER.length; i++) {
                header[i] = (char) data.readUnsignedByte();
            }
            if (!Arrays.equals(header, BMSOE_FILE_HEADER)) {
                throw new InputMismatchException(); // bmsoe file not formatted properly
            }
            width = data.readInt();
            height = data.readInt();
            Color[] pixelsInRow = new Color[width];
            image = new WritableImage(width, height);
            writer = image.getPixelWriter();
            for (int row = 0; row < height; row++) {
                for (int i = 0; i < width; i++) {
                    pixelsInRow[i] = intToColor(data.readInt());

                }
                for (int column = 0; column < width; column++) {
                    color = pixelsInRow[column];
                    writer.setColor(column, row, color);
                }
            }
        }
        return image;
    }

    /**
     * Writes a specified .bmsoe image to a certain path.
     *
     * @param image the image that is to be written to a path
     * @param path  the path that the image is to be written to
     * @throws IOException when the Output stream encounters an error
     */
    private static void writeBMSOE(Image image, Path path) throws IOException {
        int width;
        int height;
        Color pixel;

        try (DataOutputStream data = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(path.toFile())))) {
            width = (int) image.getWidth();
            height = (int) image.getHeight();
            for (int i = 0; i < BMSOE_FILE_HEADER.length; i++) {
                data.write(BMSOE_FILE_HEADER[i]);
            }
            data.writeInt(width);
            data.writeInt(height);
            PixelReader reader = image.getPixelReader();
            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    pixel = reader.getColor(column, row);
                    data.writeInt(colorToInt(pixel));
                }
            }
        }
    }


    /**
     * Method that converts a give int associated with a color,
     * and creates a color from that
     *
     * @param color int in the form of a hex code color.
     * @return a color object that represents the color
     * string that was input
     */
    private static Color intToColor(int color) {
        double red = ((color >> RED_INDEX) & COLOR_FORMAT) / COLOR_RANGE;
        double green = ((color >> GREEN_INDEX) & COLOR_FORMAT) / COLOR_RANGE;
        double blue = (color & COLOR_FORMAT) / COLOR_RANGE;
        double alpha = ((color >> ALPHA_INDEX) & COLOR_FORMAT) / COLOR_RANGE;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Method that converts a specified color to a int
     * representing the hex code of that color
     *
     * @param color the specified color that is to be
     *              converted to an int
     * @return the converted color to an int in hex code
     * format
     */
    private static int colorToInt(Color color) {
        int red = ((int) (color.getRed() * COLOR_RANGE)) & COLOR_FORMAT;
        int green = ((int) (color.getGreen() * COLOR_RANGE)) & COLOR_FORMAT;
        int blue = ((int) (color.getBlue() * COLOR_RANGE)) & COLOR_FORMAT;
        int alpha = ((int) (color.getOpacity() * COLOR_RANGE)) & COLOR_FORMAT;
        return (alpha << ALPHA_INDEX) + (red << RED_INDEX) + (green << GREEN_INDEX) + blue;
    }
}
