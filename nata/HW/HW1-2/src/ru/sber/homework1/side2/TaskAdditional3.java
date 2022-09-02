package ru.sber.homework1.side2;

import java.util.Scanner;

public class TaskAdditional3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nameAndModel = scanner.nextLine();
        int cost = scanner.nextInt();
        if ((nameAndModel.contains("samsung") || nameAndModel.contains("iphone")) && (cost >= 50000 && cost <= 120000))
            System.out.println("Можно купить");
        else System.out.println("Не подходит");
    }
}
