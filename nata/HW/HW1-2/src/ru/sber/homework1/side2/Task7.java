package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        int spacePosition = s.indexOf(' ');
        String s1 = s.substring(0, spacePosition);
        String s2 = s.substring(spacePosition + 1);
        System.out.println(s1);
        System.out.println(s2);
    }
}
