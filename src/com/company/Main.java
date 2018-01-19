package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Main {

    public static int H = 5;
    public static int WIDTH_BOARD = 60;
    static String FILENAME = "costs" + "_" + H +".dat";

    //Options
    static boolean ROUND_ENABLE = false;
    static int ROUND_SCALE = 3;
    static int SIGN_DIGITS = 2;
    static int SEED_RAND = 72;


    public static double[][] mC;
    public static Hole[] mHoles;

    public static void main(String[] args) throws IOException {

        mHoles = generateHoles();
        //printHoles(mHoles);

        mC = generateMatrixCosts(mHoles);
        //printMatrix(mC);

        generateDat(mC);
    }

    private static void printHoles(Hole[] holes){
        for(int i = 0; i < holes.length; i++){
            System.out.println("Hole_" + i + " " + holes[i].getX() + " " + holes[i].getY());
        }
    }

    private static void printMatrix(double[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println("\n ");
        }
    }

    private static double[][] generateMatrixCosts(Hole[] holes) {
        double[][] matrix = new double[H][H];
        double distance;
        double x1;
        double x2;
        double y1;
        double y2;

        for (int row = 0; row < H; row++) {
            for (int col = row + 1; col < H; col++) {
                if (row != col) {
                    x1 = holes[row].getX();
                    x2 = holes[col].getX();

                    y1 = holes[row].getY();
                    y2 = holes[col].getY();

                    distance = Math.hypot(x1-x2, y1-y2);

                    matrix[row][col] = distance;
                    matrix[col][row] = distance;
                }
            }
        }
        return matrix;
    }

    private static Hole[] generateHoles(){
        Hole[] holes = new Hole[H];
        Random generator = new Random(SEED_RAND);

        for(int i = 0; i < H; i++){
            double start = 0;
            double end = WIDTH_BOARD;
            double generatedRand_X = start + generator.nextDouble() * (end - start);
            double generatedRand_Y = start + generator.nextDouble() * (end - start);

            holes[i] = new Hole(generatedRand_X, generatedRand_Y);
        }

        return holes;
    }

    public static void generateDat(double[][] C) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(FILENAME));

        String digit;
        BigDecimal d;

        // Header num of Holes
        outputWriter.write("" + H);
        outputWriter.newLine();

        for (int i = 0; i < H; i++) {
            for(int k = 0; k < H; k++){

                if(ROUND_ENABLE) {
                    d = BigDecimal.valueOf(C[i][k]).setScale(ROUND_SCALE, BigDecimal.ROUND_HALF_UP);
                }
                else {
                    d = BigDecimal.valueOf(C[i][k]);
                }
                digit = String.format(Locale.US, "%."+ SIGN_DIGITS + "f" , d) ; // %.2f
                outputWriter.write(digit +"\t");
            }
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }
}
