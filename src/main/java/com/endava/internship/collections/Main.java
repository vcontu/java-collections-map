package com.endava.internship.collections;

import java.time.LocalDate;
import java.util.Comparator;

public class Main {
    public static void main(String args[]) {
        Comparator<Student> reverseOrderComparator = new Comparator<Student>() {

            @Override
            public int compare(Student student1, Student student2) {
                int compare = -student1.getName().compareTo(student2.getName());

                if(compare != 0) {
                    return compare;
                }

                return student1.getDateOfBirth().compareTo(student2.getDateOfBirth());
            }
        };

        StudentMap<Student, Integer> students = new StudentMap<>();

        Student student1 = new Student("Ivanov Victor", LocalDate.of(1994, 4, 3), "UTM");

        students.put(new Student("Neiculov Alexei", LocalDate.of(2002, 2, 18), "CEITI"), 1);
        students.put(new Student("Terzi Kirill", LocalDate.of(2002, 12, 11), "CEITI"), 2);
        students.put(new Student("Ponomar Dmitri", LocalDate.of(2003, 7, 8), "ASEM"), 3);
        students.put(new Student("Rotaru Timon", LocalDate.of(2004, 5, 16), "UTM"), 4);
        students.put(student1, 5);
        students.put(new Student("Boblic Stefan", LocalDate.of(2001, 5, 9), "ASEM"), 6);
        students.put(new Student("Boblic Stefan", LocalDate.of(2000, 3, 6), "ASEM"), 7);
        students.put(new Student("Karaman Anatolii", LocalDate.of(2002, 8, 7), "USM"), 8);
        students.put(new Student("Simlih Ilia", LocalDate.of(2002, 8, 2), "CEITI"), 9);
        System.out.println(students);
        System.out.println(students.size());
        System.out.println(student1.getName() + " " + students.get(null));
        System.out.println(students + "\n" + students.size());
        students.clear();
        System.out.println(students + "\n" + students.size());
        System.out.println(students.containsValue(3));
    }
}
