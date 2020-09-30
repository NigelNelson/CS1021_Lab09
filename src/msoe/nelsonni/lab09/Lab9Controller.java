/*
 * Course: CS1021 - 021
 * Winter
 * Lab 9 - Final Project Continued
 * Name: Nigel Nelson
 * Created: 02/19/20
 */
package msoe.nelsonni.lab09;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

/**
 * The class that controls the ImageManipulatorUI
 */
public class Lab9Controller {

    private Stage kernelStage;
    private Image image;
    private Image transformedImage;

    @FXML
    private ImageView imageView;
    @FXML
    private Button filterButton;

    /**
     * Method that sets the Kernel stage that is to be
     * referenced
     * @param kernelStage the instance of the Kernel stage
     */
    public void setKernelStage(Stage kernelStage) {
        this.kernelStage = kernelStage;
    }

    /**
     * The event handler for the "open" button in the
     * ImageManipulatorUI
     * @param event the Actionevent instance
     */
    @FXML
    private void open(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png",
                        "*.jpg", "*.msoe", "*.bmsoe"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                if (ImageIO.read(file.toPath()) != null) {
                    image = ImageIO.read(file.toPath());
                    imageView.setImage(image);
                    transformedImage = image;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IndexOutOfBoundsException e) {
                showReadFailureAlert();
            } catch (IOException e) {
                showReadFailureAlert();
            } catch (IllegalArgumentException e){
                showReadFailureAlert();
            } catch (InputMismatchException e){
                showReadFailureAlert();
            } catch (NegativeArraySizeException e){
                showReadFailureAlert();
            }
        }
    }

    /**
     * The event handler for the "save" button in
     * the ImageManipulator
     * @param event the ActionEvent instance
     */
    @FXML
    private void save(ActionEvent event) {
        if (imageView.getImage() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save New Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png",
                            "*.jpg", "*.msoe", "*.bmsoe"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    ImageIO.write(imageView.getImage(), file.toPath());
                    showSaveSuccessfulAlert();
                } catch (IOException e){
                    showUnsupportedFileTypeAlert();
                } catch (IllegalArgumentException e){
                    showSaveFailureAlert();
                }

            }
        } else {
            // alert saying no image is loaded yet
            showImageNotLoadedAlert();
        }
    }

    /**
     * The event handler for the "reload" button
     * in the ImageManipulatorUI
     * @param event the ActionEvent referenced
     */
    @FXML
    private void reload(ActionEvent event) {
        imageView.setImage(image);
        transformedImage = image;
    }

    /**
     * The method that is responsible for handling all of the
     * transformations of an image
     * @param image the image that is to be transformed
     * @param transform the instance of the Transformable interface
     * @return a transformed version of the Image instance
     */
    private static Image transformImage(Image image, Transformable transform) {
        WritableImage newImage;
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        newImage = new WritableImage(width, height);
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = image.getPixelReader();
        Color newPixel;
        Color pixel;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixel = reader.getColor(x, y);
                newPixel = transform.transform(x, y, pixel);
                writer.setColor(x, y, newPixel);
            }
        }
        return newImage;
    }

    /**
     * The event handler for the "grayscale" button
     * in the ImageManipulatorUI
     * @param event the ActionEvent referenced
     */
    @FXML
    private void grayscale(ActionEvent event) {
        if (imageView.getImage() != null) {
            Transformable grayTransform = ((x, y, color) -> color.grayscale());
            transformedImage = transformImage(imageView.getImage(), grayTransform);
            imageView.setImage(transformedImage);
        }
    }

    /**
     * The event handler for the "red" button
     * in the ImageManipulatorUI
     * @param event the ActionEvent referenced
     */
    @FXML
    private void red(ActionEvent event) {
        if (imageView.getImage() != null) {
            Transformable redTransform = ((x, y, color) -> new Color(color.getRed(), 0,
                    0, color.getOpacity()));
            transformedImage = transformImage(imageView.getImage(), redTransform);
            imageView.setImage(transformedImage);
        }
    }

    /**
     * The event handler for the "redGray" button
     * in the ImageManipulatorUI
     * @param event the ActionEvent referenced
     */
    @FXML
    private void redGray(ActionEvent event) {
        if (imageView.getImage() != null) {
            Transformable redGrayTransform = ((x, y, color) -> {
                Color newColor;
                if (y % 2 == 0) {
                    newColor = new Color(color.getRed(), 0,
                            0, color.getOpacity());
                } else {
                    newColor = color.grayscale();
                }
                return newColor;
            });
            transformedImage = transformImage(imageView.getImage(), redGrayTransform);
            imageView.setImage(transformedImage);
        }
    }

    /**
     * The event handler for the "negative" button
     * in the ImageManipulatorUI
     * @param event the ActionEvent referenced
     */
    @FXML
    private void negative(ActionEvent event) {
        if (imageView.getImage() != null) {
            Transformable negativeTransform = ((x, y, color) -> color.invert());
            transformedImage = transformImage(imageView.getImage(), negativeTransform);
            imageView.setImage(transformedImage);
        }
    }

    /**
     * The event handler for the "reload" button
     * in the ImageManipulatorUI
     * @param event the ActionEvent referenced
     */
    @FXML
    private void filter(ActionEvent event) {
        if (kernelStage.isShowing()) {
            kernelStage.close();
            filterButton.setText("Show Filter");
        } else {
            kernelStage.show();
            filterButton.setText("Hide Filter");
        }
    }

    /**
     * Method that displays an Alert to the user when
     * an image does not load
     */
    private void showImageNotLoadedAlert() {
        Alert imageNotLoadedAlert = new Alert(Alert.AlertType.ERROR, "Error: No image " +
                "has been loaded in yet! ");
        imageNotLoadedAlert.setTitle("Error Dialog");
        imageNotLoadedAlert.setHeaderText("Image Not found");
        imageNotLoadedAlert.showAndWait();
    }

    /**
     * Method that get the instance of the
     * ImageView that is being used in the program
     * @return the instance of the ImageView
     */
    public ImageView getImageView() {
        return this.imageView;
    }

    /**
     * Method that displays an error in the
     * form of an Alert when a file could not be read
     */
    private static void showReadFailureAlert() {
        Alert readFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "read image from specified file. File may be corrupt or empty");
        readFailureAlert.setTitle("Error Dialog");
        readFailureAlert.setHeaderText("Read Failure");
        readFailureAlert.showAndWait();
    }

    /**
     * Method that displays an error in the
     * form of an Alert when a file is believed unsupported
     */
    private static void showUnsupportedFileTypeAlert() {
        Alert unsupportedFileTypeAlert = new Alert(Alert.AlertType.ERROR, "Error: " +
                "the file entered could not be saved");
        unsupportedFileTypeAlert.setTitle("Error Dialog");
        unsupportedFileTypeAlert.setHeaderText("Invalid file type");
        unsupportedFileTypeAlert.showAndWait();
    }

    /**
     * Method that displays an error in the
     * form of an Alert when a file could not be saved
     */
    private static void showSaveFailureAlert() {
        Alert saveFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "save image to specified file. ");
        saveFailureAlert.setTitle("Error Dialog");
        saveFailureAlert.setHeaderText("Save Failure");
        saveFailureAlert.showAndWait();
    }

    /**
     * Method that displays an error in the
     * form of an Alert when a file could not be saved
     */
    private static void showSaveSuccessfulAlert() {
        Alert saveSuccessfulAlert = new Alert(Alert.AlertType.INFORMATION, "Your " +
                "image saved successfully!");
        saveSuccessfulAlert.setTitle("Message Dialog");
        saveSuccessfulAlert.setHeaderText("Save Success");
        saveSuccessfulAlert.showAndWait();
    }
}
