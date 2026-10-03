package org.example;

import org.example.calculation.CalculationElement;
import org.example.calculation.accural.Salary;
import org.example.common.date_intervals.MonthInterval;
import org.example.demo.DemoData;
import org.example.employee.*;
import org.example.payroll.PayrollPeriod;
import org.example.payroll.Payslip;

import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static void printEmployees(List<Employee> employees) {
        System.out.println();
        System.out.println("СОТРУДНИКИ");
        System.out.println("----------------------------------------");

        for (Employee employee : employees) {
            System.out.printf(
                    "%d. %s%n",
                    employee.employeeId(),
                    employee.getFullName()
            );
        }
    }

    private static void calculatePayslipForMonth(
            Scanner scanner,
            List<Employee> employees
    ) {
        printEmployees(employees);

        System.out.print("Введите табельный номер: ");
        int employeeId = scanner.nextInt();

        Employee selectedEmployee = null;

        for (Employee employee : employees) {
            if (employee.employeeId() == employeeId) {
                selectedEmployee = employee;
                break;
            }
        }

        if (selectedEmployee == null) {
            System.out.println("Сотрудник не найден.");
            return;
        }

        System.out.print("Введите год: ");
        int year = scanner.nextInt();

        System.out.print("Введите месяц (1-12): ");
        int month = scanner.nextInt();

        if (month < 1 || month > 12) {
            System.out.println("Некорректный месяц.");
            return;
        }

        YearMonth yearMonth = YearMonth.of(year, month);

        PayrollPeriod payrollPeriod =
                new PayrollPeriod(
                        new MonthInterval(yearMonth, yearMonth)
                );

        Payslip payslip =
                new Payslip(selectedEmployee, payrollPeriod);

        payslip.printPayslip();
    }

    private static void calculatePayslipForPeriod(
            Scanner scanner,
            List<Employee> employees
    ) {
        printEmployees(employees);

        System.out.print("Введите табельный номер: ");
        int employeeId = scanner.nextInt();

        Employee selectedEmployee = null;

        for (Employee employee : employees) {
            if (employee.employeeId() == employeeId) {
                selectedEmployee = employee;
                break;
            }
        }

        if (selectedEmployee == null) {
            System.out.println("Сотрудник не найден.");
            return;
        }

        System.out.print("Введите начальный год: ");
        int startYear = scanner.nextInt();

        System.out.print("Введите начальный месяц (1-12): ");
        int startMonth = scanner.nextInt();

        System.out.print("Введите конечный год: ");
        int endYear = scanner.nextInt();

        System.out.print("Введите конечный месяц (1-12): ");
        int endMonth = scanner.nextInt();

        YearMonth start = YearMonth.of(startYear, startMonth);
        YearMonth end = YearMonth.of(endYear, endMonth);

        if (start.isAfter(end)) {
            System.out.println("Начальный месяц не может быть позже конечного.");
            return;
        }

        PayrollPeriod payrollPeriod =
                new PayrollPeriod(
                        new MonthInterval(start, end)
                );

        Payslip payslip =
                new Payslip(selectedEmployee, payrollPeriod);

        payslip.printPayslip();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Employee> employees = DemoData.createEmployees();

        while (true) {
            System.out.println();
            System.out.println("========================================");
            System.out.println("       РАСЧЁТ ЗАРАБОТНОЙ ПЛАТЫ");
            System.out.println("========================================");
            System.out.println("1. Показать сотрудников");
            System.out.println("2. Рассчитать зарплату за месяц");
            System.out.println("3. Рассчитать зарплату за несколько месяцев");
            System.out.println("0. Выход");
            System.out.println("----------------------------------------");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> printEmployees(employees);
                case 2 -> calculatePayslipForMonth(scanner, employees);
                case 3 -> calculatePayslipForPeriod(scanner, employees);
                case 0 -> {
                    System.out.println("Завершение работы...");
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }

    }
}