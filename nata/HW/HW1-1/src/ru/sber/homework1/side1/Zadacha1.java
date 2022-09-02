package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double radius = sc.nextDouble();
        double volume = 4 / 3.0 * Math.PI * Math.pow(radius, 3);
        System.out.println(volume);
    }
}
