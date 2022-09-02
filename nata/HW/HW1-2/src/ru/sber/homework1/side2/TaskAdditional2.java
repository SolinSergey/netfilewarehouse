package ru.sber.homework1.side2;

import java.util.Scanner;

public class TaskAdditional2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inData = scanner.nextLine();
        boolean result1 = false, result2 = false;
        String result = "все ок";
        String s1 = "в посылке";
        String s2 = "камни";
        String s3 = "запрещенная продукция";
        if (inData.startsWith("камни!") || inData.endsWith("камни!")) result1 = true;
        if (inData.startsWith("запрещенная продукция") || inData.endsWith("запрещенная продукция")) result2 = true;
        if (result1) result = "камни " + s1;
        if (result2) result = s1 + " " + s3;
        if (result1 && result2) result = s1 + " " + s2 + " и " + s3;
        System.out.println(result);
    }
}
