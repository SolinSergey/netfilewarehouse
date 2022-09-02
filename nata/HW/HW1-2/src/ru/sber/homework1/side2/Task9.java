package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task9 {
    public static void main(String[] args) {
        final double EPSILON = 1E-14;
        Scanner scanner = new Scanner(System.in);
        double x = scanner.nextDouble();
        double y = Math.pow(Math.sin(x * Math.PI / 180), 2) + Math.pow(Math.cos(x * Math.PI / 180), 2);
        if (Math.abs(y-1.0) < EPSILON) {
            System.out.println(true);
        }
        else System.out.println(false);
    }
}
