package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task4 {
    public static void main(String[] args) {
        final int NUMBER_OF_SATURDAY = 6;
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int daysToSat = NUMBER_OF_SATURDAY - n;
        if (daysToSat > 0) {
            System.out.println(daysToSat);
        } else {
            System.out.println("Ура, выходные!");
        }
    }
}
