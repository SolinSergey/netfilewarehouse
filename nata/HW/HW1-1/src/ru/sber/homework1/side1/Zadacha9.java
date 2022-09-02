package ru.sber.homework1.side1;

import java.util.Scanner;

public class Zadacha9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int numberGuests = n / k;
        System.out.println(numberGuests);
    }
}
