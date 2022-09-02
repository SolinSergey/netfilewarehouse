package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        int a, b, c;
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        b = scanner.nextInt();
        c = scanner.nextInt();
        System.out.println((a > b && b > c) ? "Петя, пора трудиться" : "Петя молодец!");
    }
}
