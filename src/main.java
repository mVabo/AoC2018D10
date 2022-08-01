import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class
 *
 * Takes in the coordinates and its velocity.
 *
 * Has a method for appending velocity to current location - updateLocation()
 */
class Point {
    // Constructor for assigning initial variables
    public Point(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public int x;
    public int y;
    public int dx;
    public int dy;

    // Method for updating the coordinates based on its velocity
    public void updateLocation() {
        this.x += dx;
        this.y += dy;
    }
}

/**
 * Solution to Advent of Code 2018 Day 10
 *
 * Helper classes:
 * - Point
 *
 * Methods:
 * - initialize() = parses the input file d10.in and triggers a loop that progresses the sky
 * - pointLoop() = Loop that progresses all points and checks if the sky image still shrinks
 * - printArea() = Function that converts points into matrix used to print the sky to the console
 */
class AoC2018D10 {
    // A list of all our points
    List<Point> points = new ArrayList<>();

    // Max and min variables for keeping track of the sky area
    int maxX = 0;
    int minX = 0;
    int maxY = 0;
    int minY = 0;

    // Area used as comparison for the next loop
    int area = 0;

    // Total seconds used
    int seconds = 0;

    // The main function reads the input and triggers a loop
    public void initialize() {  // O(n)
        // Try-catch for catching potential read errors
        try {
            // Define the file and create a scanner/reader
            File inputFile = new File("src/d10.in");
            Scanner reader = new Scanner(inputFile);

            // A for loop reading all the lines in the file
            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                // Split the string into coordinates and velocity
                String[] firstSplit = data.split("<");
                int x = Integer.parseInt(firstSplit[1].split(",")[0].trim());
                int y = Integer.parseInt(firstSplit[1].split(",")[1].split(">")[0].trim());

                int dx = Integer.parseInt(firstSplit[2].split(",")[0].trim());
                int dy = Integer.parseInt(firstSplit[2].split(",")[1].split(">")[0].trim());

                // Check for extremal points to use for the initial area
                if (x > maxX) {
                    maxX = x;
                } else if (x < minX) {
                    minX = x;
                }

                if (y > maxY) {
                    maxY = y;
                } else if (y < minY) {
                    minY = y;
                }

                // Add the point to the list of points
                this.points.add(new Point(x, y, dx, dy));
            }

            // Assign the initial area (assuming that minimal points is negative)
            this.area = maxX + (minX * -1) * maxY + (minY * -1);
            reader.close();
        } catch (FileNotFoundException e) {
            // Print error
            System.err.println("Error potentially caused by file placement/name. The file \"d10.in\" needs to placed in the \"src\" folder");
            e.printStackTrace();
        }

        this.pointLoop();
    }

    // This is the loop that is being triggered by initialize(), it moves all the points until the area stops decreasing

    // Based on the images in the description of the task, the area will be smallest when all the points are in the correct place
    private void pointLoop() {  // O(n^2)
        while (true) {
            // Add another second for every loop (as one loop simulates one second in the skies)
            this.seconds++;

            // Reset extremal points
            this.maxX = this.points.get(0).x;
            this.minX = this.points.get(0).x;
            this.maxY = this.points.get(0).y;
            this.minY = this.points.get(0).y;
            for (Point point : this.points) {
                // Update the point's location
                point.updateLocation();

                // Check if this points is an extremal point
                if (point.x > maxX) {
                    this.maxX = point.x;
                } else if (point.x < minX) {
                    this.minX = point.x;
                }

                if (point.y > maxY) {
                    this.maxY = point.y;
                } else if (point.y < minY) {
                    this.minY = point.y;
                }
            }

            // Convert negative points to positive to use as comparison
            if (this.minX < 0) {
                this.minX = minX * -1;
            }
            if (this.minY < 0) {
                this.minY = minY * -1;
            }

            // Calculate new area and check if it still shrinks, if not, break loop and print the sky
            int newArea = (this.maxX + this.minX) * (this.maxY + this.minY);
            if (this.area > 0 && newArea > this.area) {
                break;
            } else {
                this.area = newArea;

            }
        }

        printArea();
    }

    private void printArea() {  // O(n^3)
        // Find dimensions by subtracting min from max
        int height = maxY - minY;
        int width = maxX - minY;

        // Create a visual board by nesting arrays
        boolean[][] board = new boolean[height + 1][width + 2];

        // Set all values to help build the console output (the sky)
        for (Point point : points) {
            board[point.y - minY][point.x - minX] = true;
        }

        // Print the answer
        System.out.println("[Part One] Message in the skies:");
        for (boolean[] row : board) {
            StringBuilder rowString = new StringBuilder();
            for (boolean point : row) {
                if (point) {
                    rowString.append("#");
                } else {
                    rowString.append(" ");
                }
            }
            System.out.println(rowString);
        }
        System.out.println("[Part Two] Seconds used: " + this.seconds);
    }
}

class Main {
    // Starting initialize
    public static void main(String[] args) {
        AoC2018D10 aoc = new AoC2018D10();
        aoc.initialize();
    }
};
