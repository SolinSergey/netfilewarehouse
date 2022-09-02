package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha8 {
    public static void main(String[] args) {
        final int AMOUNT_OF_DAYS = 30;
        Scanner sc = new Scanner(System.in);
        double n = sc.nextDouble();
        double budget = n / AMOUNT_OF_DAYS;
        System.out.println(budget);
    }
}
