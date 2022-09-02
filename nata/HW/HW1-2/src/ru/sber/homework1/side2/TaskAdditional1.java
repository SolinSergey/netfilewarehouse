package ru.sber.homework1.side2;

import java.util.Scanner;

public class TaskAdditional1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        if (password.length() >= 8) {
            int titles = 0, lowercases = 0, numbers = 0, signs = 0;
            for (int i = 0; i < password.length(); i++) {
                if ((password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') || (password.charAt(i) >= 'А' && password.charAt(i) <= 'Я'))
                    ++titles;
                if ((password.charAt(i) >= 'a' && password.charAt(i) <= 'z') || (password.charAt(i) >= 'а' && password.charAt(i) <= 'я'))
                    ++lowercases;
                if (password.charAt(i) >= '0' && password.charAt(i) <= '9') ++numbers;
                if (password.charAt(i) == '_' || password.charAt(i) == '*' || password.charAt(i) == '-') ++signs;
            }
            if (titles > 0 && lowercases > 0 && numbers > 0 && signs > 0) System.out.println("пароль надежный");else System.out.println("пароль не прошел проверку");
        } else System.out.println("пароль не прошел проверку");
    }
}
