package ru.sber.homework1.side2;

import java.util.Scanner;

public class Task8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        int spacePosition1 = s.indexOf(' ');
        int spacePosition2 = s.indexOf(' ', spacePosition1 + 1);
        String s1, s2;
        if (spacePosition2 > spacePosition1) {
            s1 = s.substring(0, spacePosition2);
            s2 = s.substring(spacePosition2 + 1);
        } else {
            s1 = s.substring(0, spacePosition1);
            s2 = s.substring(spacePosition1 + 1);
        }
        System.out.println(s1);
        System.out.println(s2);
    }
}
