package com.endava.internship.collections;

import java.time.LocalDate;
import java.util.Comparator;

public class Main {
    public static void main(String args[]) {
        Comparator<Student> comparator = new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int compare = -o1.getName().compareTo(o2.getName());
                if(compare != 0)
                    return compare;
                return o1.getDateOfBirth().compareTo(o2.getDateOfBirth());
            }
        };
        Student ivanov = new Student("Ivanov Victor", LocalDate.of(1994, 4, 3), "UTM");
        StudentMap<Student, Integer> sm = new StudentMap<>(comparator);
        sm.put(new Student("Neiculov Alexei", LocalDate.of(2002, 2, 18), "CEITI"), 1);
        sm.put(new Student("Terzi Kirill", LocalDate.of(2002, 12, 11), "CEITI"), 2);
        sm.put(new Student("Ponomar Dmitri", LocalDate.of(2003, 7, 8), "ASEM"), 3);
        sm.put(new Student("Rotaru Timon", LocalDate.of(2004, 5, 16), "UTM"), 4);
        sm.put(ivanov, 5);
        sm.put(new Student("Boblic Stefan", LocalDate.of(2001, 5, 9), "ASEM"), 6);
        sm.put(new Student("Boblic Stefan", LocalDate.of(2000, 3, 6), "ASEM"), 7);
        sm.put(new Student("Karaman Anatolii", LocalDate.of(2002, 8, 7), "USM"), 8);
        sm.put(new Student("Simlih Ilia", LocalDate.of(2002, 8, 2), "CEITI"), 9);
        System.out.println(sm);
        System.out.println(sm.size());
        sm.remove(ivanov);
        System.out.println(sm + "\n" + sm.size());
        sm.clear();
        System.out.println(sm + "\n" + sm.size());
    }
}
