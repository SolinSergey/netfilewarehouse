package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha5 {
    public static void main(String[] args) {
        final double CENTIMETER_IN_INCH = 2.54;
        Scanner sc = new Scanner(System.in);
        double inch = sc.nextDouble();
        double centimeter = inch * CENTIMETER_IN_INCH;
        System.out.println(centimeter);
    }
}
