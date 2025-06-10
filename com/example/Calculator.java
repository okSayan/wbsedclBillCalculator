package com.example;

import java.util.*;
import java.io.*;

public class Calculator {
    int myWatt;

    public static void main(String[] args){
        Calculator calc = new Calculator();
        calc.run();
    }

    public void run() {
        myWatt = loadInput("input.txt");
        List<double[]> slabRates = loadSlabRates("slabs.txt");
        double billAmount = calculateBill(myWatt, slabRates);
        System.out.println("\n\nYour: KWh: "+myWatt);
        System.out.println("\n\nYour estimated (Energy Charge) is: \t" + billAmount);
        System.out.println("Actual bill is subject to other factors like (Meter Charge, Govt. Subsidies, Adjustments, etc.) This program is just to see if the (Energy Charge) is correct or not.");
    }

    public int loadInput(String filename) {
        int inputValue = 0;
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            if (fileScanner.hasNextInt()) {
                inputValue = fileScanner.nextInt();
            } else {
                System.err.println("Error: input.txt is empty or invalid.");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: input.txt not found.");
            System.exit(1);
        }
        return inputValue;
    }

    public List<double[]> loadSlabRates(String filename) {
        List<double[]> slabRates = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().trim().split("\\s+");
                if (parts.length == 2) {
                    double limit = Double.parseDouble(parts[0]);
                    double rate = Double.parseDouble(parts[1]);
                    slabRates.add(new double[]{limit, rate});
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: slabs.txt not found.");
            System.exit(1);
        }
        return slabRates;
    }

    public double calculateBill(int wattHour, List<double[]> slabRates) {
        double billAmount = 0;
        int unitsLeft = wattHour;

        for (double[] slab : slabRates) {
            double slabLimit = slab[0];
            double slabRate = slab[1];

            if (unitsLeft > 0) {
                double unitsInThisSlab = Math.min(unitsLeft, slabLimit);
                billAmount += unitsInThisSlab * slabRate;
                unitsLeft -= unitsInThisSlab;
            } else {
                break;
            }
        }
        return billAmount;
    }
}
