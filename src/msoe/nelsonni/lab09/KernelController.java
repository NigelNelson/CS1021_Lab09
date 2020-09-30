/*
 * Course: CS1021 - 021
 * Winter
 * Lab 9 - Final Project Continued
 * Name: Nigel Nelson
 * Created: 02/19/20
 */
package msoe.nelsonni.lab09;

import edu.msoe.cs1021.ImageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.text.DecimalFormat;

/**
 * Class that is responsible for controlling
 * the KernelUI
 */
public class KernelController {
    /**
     * The topRight textField in the
     * KernelUI
     */
    public TextField topRight;
    /**
     * The topMiddle textField in the
     * KernelUI
     */
    public TextField topMiddle;
    /**
     * The topLeft textField in the
     * KernelUI
     */
    public TextField topLeft;
    /**
     * The middleRight textField in the
     * KernelUI
     */
    public TextField middleRight;
    /**
     * The middleMiddle textField in the
     * KernelUI
     */
    public TextField middleMiddle;
    /**
     * The middleLeft textField in the
     * KernelUI
     */
    public TextField middleLeft;
    /**
     * The bottomRight textField in the
     * KernelUI
     */
    public TextField bottomRight;
    /**
     * The bottomMiddle textField in the
     * KernelUI
     */
    public TextField bottomMiddle;
    /**
     * The bottomLeft textField in the
     * KernelUI
     */
    public TextField bottomLeft;
    /**
     * Reference to the kernelStage
     */
    private Stage kernelStage;
    /**
     * Reference to the Primary Controller
     */
    private Lab9Controller lab9Controller;

    /**
     * Method that sets the instance of the
     * stage that is being used
     *
     * @param kernelStage the instance of the kernelStage being
     *                    Used
     */
    public void setStage(Stage kernelStage) {
        this.kernelStage = kernelStage;
    }

    /**
     * Default kernel values for a blur filter
     */
    public static final double[] DEFAULT_BLUR = {
            0, 1, 0,
            1, 5, 1,
            0, 1, 0};

    /**
     * Default kernel values for a sharpen filter
     */
    public static final double[] DEFAULT_SHARPEN = {
            0, -1, 0,
            -1, 5, -1,
            0, -1, 0};
    /**
     * Integer representing the index of five
     */
    private static final int INDEX_FIVE = 5;
    /**
     * Integer representing the index of six
     */
    private static final int INDEX_SIX = 6;
    /**
     * Integer representing the index of seven
     */
    private static final int INDEX_SEVEN = 7;
    /**
     * Integer representing the index of eight
     */
    private static final int INDEX_EIGHT = 8;
    /**
     * Integer representing a list size of nine
     */
    private static final int SIZE_NINE = 9;


    private DecimalFormat format = new DecimalFormat("0.#");

    /**
     * Event handler for the "blur" button
     * in the KernelUI
     *
     * @param event the ActionEvent instance
     */
    @FXML
    private void blur(ActionEvent event) {
        // accessing DEFAULT_BLUR array to set text in appropriate fields
        topLeft.setText(format.format(DEFAULT_BLUR[0]));
        topMiddle.setText(format.format(DEFAULT_BLUR[1]));
        topRight.setText(format.format(DEFAULT_BLUR[2]));
        middleLeft.setText(format.format(DEFAULT_BLUR[3]));
        middleMiddle.setText(format.format(DEFAULT_BLUR[4]));
        middleRight.setText(format.format(DEFAULT_BLUR[INDEX_FIVE]));
        bottomLeft.setText(format.format(DEFAULT_BLUR[INDEX_SIX]));
        bottomMiddle.setText(format.format(DEFAULT_BLUR[INDEX_SEVEN]));
        bottomRight.setText(format.format(DEFAULT_BLUR[INDEX_EIGHT]));
    }

    /**
     * Event handler for the "sharpen" button
     * in the KernelUI
     *
     * @param event the ActionEvent instance
     */
    @FXML
    private void sharpen(ActionEvent event) {
        // accessing DEFAULT_BLUR array to set text in appropriate fields
        topLeft.setText(format.format(DEFAULT_SHARPEN[0]));
        topMiddle.setText(format.format(DEFAULT_SHARPEN[1]));
        topRight.setText(format.format(DEFAULT_SHARPEN[2]));
        middleLeft.setText(format.format(DEFAULT_SHARPEN[3]));
        middleMiddle.setText(format.format(DEFAULT_SHARPEN[4]));
        middleRight.setText(format.format(DEFAULT_SHARPEN[INDEX_FIVE]));
        bottomLeft.setText(format.format(DEFAULT_SHARPEN[INDEX_SIX]));
        bottomMiddle.setText(format.format(DEFAULT_SHARPEN[INDEX_SEVEN]));
        bottomRight.setText(format.format(DEFAULT_SHARPEN[INDEX_EIGHT]));
    }

    /**
     * Event handler for the "apply" button
     * in the KernelUI
     *
     * @param event the ActionEvent instance
     * @throws IllegalArgumentException when an invalid sum is found in the
     * kernel
     */
    @FXML
    public void apply(ActionEvent event) throws IllegalArgumentException {
        if(lab9Controller.getImageView().getImage() != null) {
            try {
                Double[] kernelValues = new Double[SIZE_NINE];
                kernelValues[0] = Double.valueOf(topLeft.getText());
                kernelValues[1] = Double.valueOf(topMiddle.getText());
                kernelValues[2] = Double.valueOf(topRight.getText());
                kernelValues[3] = Double.valueOf(middleLeft.getText());
                kernelValues[4] = Double.valueOf(middleMiddle.getText());
                kernelValues[INDEX_FIVE] = Double.valueOf(middleRight.getText());
                kernelValues[INDEX_SIX] = Double.valueOf(bottomLeft.getText());
                kernelValues[INDEX_SEVEN] = Double.valueOf(bottomMiddle.getText());
                kernelValues[INDEX_EIGHT] = Double.valueOf(bottomRight.getText());

                Double divisor = 0.0;
                for (Double f : kernelValues) {
                    divisor += f;
                }

                if (divisor > 0) {
                    double[] kernel = new double[SIZE_NINE];
                    kernel[0] = kernelValues[0] / divisor;
                    kernel[1] = kernelValues[1] / divisor;
                    kernel[2] = kernelValues[2] / divisor;
                    kernel[3] = kernelValues[3] / divisor;
                    kernel[4] = kernelValues[4] / divisor;
                    kernel[INDEX_FIVE] = kernelValues[INDEX_FIVE] / divisor;
                    kernel[INDEX_SIX] = kernelValues[INDEX_SIX] / divisor;
                    kernel[INDEX_SEVEN] = kernelValues[INDEX_SEVEN] / divisor;
                    kernel[INDEX_EIGHT] = kernelValues[INDEX_EIGHT] / divisor;

                    Image currentImage = lab9Controller.getImageView().getImage();
                    Image newImage = ImageUtil.convolve(currentImage, kernel);
                    lab9Controller.getImageView().setImage(newImage);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                showInvalidKernelValues();
            }
        } else {
            showImageNotLoadedAlert();
        }
    }

    /**
     * Method that sets the proper instance of the
     * primarycontroller that is being used
     *
     * @param lab9Controller the instance of the primaryController
     */
    public void setController(Lab9Controller lab9Controller) {
        this.lab9Controller = lab9Controller;
    }

    /**
     * Method that displays an Alert to the user when
     * there are invalid kernel values entered
     */
    private static void showInvalidKernelValues() {
        Alert readFailureAlert = new Alert(Alert.AlertType.ERROR,
                "Error: Invalid values entered into " +
                "Kernel");
        readFailureAlert.setTitle("Error Dialog");
        readFailureAlert.setHeaderText("kernel error");
        readFailureAlert.showAndWait();
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
}
