package ru.sber.homework1.side2;

import java.util.Scanner;

public class TaskAdditional2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inData = scanner.nextLine();
        boolean result1 = false, result2 = false;
        String result = "��� ��";
        String s1 = "� �������";
        String s2 = "�����";
        String s3 = "����������� ���������";
        if (inData.startsWith("�����!") || inData.endsWith("�����!")) result1 = true;
        if (inData.startsWith("����������� ���������") || inData.endsWith("����������� ���������")) result2 = true;
        if (result1) result = "����� " + s1;
        if (result2) result = s1 + " " + s3;
        if (result1 && result2) result = s1 + " " + s2 + " � " + s3;
        System.out.println(result);
    }
}
