package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        if (count < 500) System.out.println("Beginner");
            else if (count >= 500 && count < 1500) System.out.println("pre-intermediate");
                else if (count >= 1500 && count < 2500) System.out.println("intermediate");
                    else if (count >= 2500 && count < 3500) System.out.println("upper-intermediate");
                        else System.out.println("fluent");
    }
}
