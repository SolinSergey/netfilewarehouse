package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task10 {
    public static void main(String[] args) {
        final double EPSILON = 1E-14;
        Scanner scanner = new Scanner(System.in);
        double n = scanner.nextDouble();
        double m = Math.log(Math.pow(Math.E, n));
        if (Math.abs(m - n) < EPSILON) {
            System.out.println(m);
            System.out.println(true);
        } else System.out.println(false);
        System.out.println(m);
    }
}
