package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha6 {
    public static void main(String[] args) {
        final double KILOMETRE_IN_MILE = 1.60934;
        Scanner sc = new Scanner(System.in);
        double count = sc.nextDouble();
        double miles = count / KILOMETRE_IN_MILE;
        System.out.println(miles);
    }
}
