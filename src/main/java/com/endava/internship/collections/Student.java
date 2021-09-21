package com.endava.internship.collections;

import java.time.LocalDate;

/**
 * The class that defines the element that will be contained by your collection
 */
public class Student implements Comparable<Student>
{
    private String name;
    private LocalDate dateOfBirth;
    private String details;

    public Student(String name, LocalDate dateOfBirth, String details) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.details = details;
    }

    public String getName() { return name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public String getDetails() { return details; }

    @Override
    public int compareTo(Student otherStudent) {
        int compareResult = name.compareTo(otherStudent.name);
        if(compareResult != 0) {
            return compareResult;
        }
        return dateOfBirth.compareTo(otherStudent.dateOfBirth);
    }

    @Override
    public String toString() {
        return name + " | " + dateOfBirth + " | " + details;
    }

}
