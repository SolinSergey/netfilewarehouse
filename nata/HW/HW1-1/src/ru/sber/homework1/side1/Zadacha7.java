package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int number = sc.nextInt();
        System.out.println(number % 10 + "" + number / 10);
    }
}
