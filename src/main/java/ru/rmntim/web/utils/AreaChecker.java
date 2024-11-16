package ru.rmntim.web.utils;

/**
 * Utility class to check if a point lies within a defined area.
 */
public class AreaChecker {

    /**
     * Checks if a given point (x, y) lies within the blue area defined by radius r.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param r the radius defining the area
     * @return true if the point is within the blue area, false otherwise
     */
    public static boolean isInArea(double x, double y, double r) {
        // Check for the rectangle in the lower-left quadrant
        if (x <= 0 && y <= 0) {
            return x >= -r && y >= -r;
        }
        // Check for the semicircle in the upper-right quadrant
        else if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= r * r;
        }
        // Check for the triangle in the lower-right quadrant
        else if (x >= 0 && y <= 0) {
            return y >= (2 * x - r / 2);
        }
        return false;
    }
}

