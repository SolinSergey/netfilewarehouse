package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        double rootMeanSquare = Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2)) / 2.0);
        System.out.println(rootMeanSquare);
    }
}