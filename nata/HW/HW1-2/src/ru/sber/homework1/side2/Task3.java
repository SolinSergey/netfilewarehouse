package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        byte n = scanner.nextByte();
        System.out.println(n > 12 ? "Пора" : "Рано");
    }
}
