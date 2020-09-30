/*
 * Course: CS1021 - 021
 * Winter
 * Lab 9 - Final Project Continued
 * Name: Nigel Nelson
 * Created: 02/19/20
 */
package msoe.nelsonni.lab09;

import javafx.scene.paint.Color;

/**
 * Functional interface in which the method, named transform, accepts three arguments: the x
 * and y location of the pixel and its color. The method must return the color for the pixel
 * after the applying the transformation.
 */
@FunctionalInterface
interface Transformable {

    /**
     * Abstract method that is called by the
     * transformImage method
     * @param x coordinate of the pixel
     * @param y coordinate of the pixel
     * @param color the color of the pixel
     * @return the transformed color
     */
    Color transform(int x, int y, Color color);

}
