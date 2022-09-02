package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        if ((b * b - 4 * a * c) > 0) {
            System.out.println("Решение есть");
        } else {
            System.out.println("Решения нет");
        }
    }
}
