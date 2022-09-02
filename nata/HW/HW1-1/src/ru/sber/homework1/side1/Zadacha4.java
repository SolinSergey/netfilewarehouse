package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha4 {
    public static void main(String[] args) {
        final int SECOND_IN_HOUR = 60;
        final int MINUTE_IN_HOUR = 60;
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        int hours = count / SECOND_IN_HOUR / MINUTE_IN_HOUR;
        int minutes = count / SECOND_IN_HOUR % MINUTE_IN_HOUR;
        System.out.println(hours + " " + minutes);
    }
}
