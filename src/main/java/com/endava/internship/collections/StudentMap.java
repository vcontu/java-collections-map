package com.endava.internship.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class StudentMap implements Map<Student, Integer>, Serializable {

    private Entry<Student, Integer>[] entryTable;
    private final float loadFactor = 0;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private int threshold;
    private int size;

    static class Entry<Student, Integer> {

        private Student k;
        private Integer v;
        final int hash;
        private Entry<Student, Integer> next;

        public Entry(int hash, Student k, Integer v, Entry<Student, Integer> next) {
            this.k = k;
            this.v = v;
            this.next = next;
            this.hash = hash;
        }

        public Student getKey() {
            return k;
        }

        public Integer getValue() {
            return v;
        }
    }

    @Override
    public int size() {
        if (size != 0) {
            return size;
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    private static boolean eq(Object x, Object y) {
        return x == y || x.equals(y);
    }

    final int hash(Object key) {
        return Math.abs(key.hashCode()) % entryTable.length;
    }

    private Entry<Student, Integer> getEntry(Object key) {
        int h = hash(key);
        Entry<Student, Integer>[] tab = getTable();
        Entry<Student, Integer> e = tab[h];
        while (e != null && !(e.hash == h && eq(key, e.k))) {
            e = e.next;
        }
        return e;
    }

    @Override
    public boolean containsKey(Object o) {
        return getEntry(o) != null;
    }

    @Override
    public boolean containsValue(Object o) {
        if (o == null) {
            return containsNullValue();
        }
        Entry<Student, Integer>[] tab = getTable();
        for (int i = tab.length; i-- > 0;) {
            for (Entry<Student, Integer> e = tab[i]; e != null; e = e.next) {
                if (o.equals(e.v)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Integer get(Object key) {
        if (key == null) {
            return null;
        }
        int h = hash(key);
        Entry<Student, Integer>[] tab = getTable();
        Entry<Student, Integer> e = tab[h];
        while (e != null) {
            if (e.hash == h && eq(key, e.k)) {
                return e.v;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public Integer put(Student student, Integer integer) {
        if (student == null) {
            throw new RuntimeException("Illegal key: " + student);
        }
        int h = hash(student);
        Entry<Student, Integer>[] tab = getTable();
        for (Entry<Student, Integer> e = tab[h]; e != null; e = e.next) {
            if (h == e.hash && eq(student, e.k)) {
                Integer oldValue = e.v;
                if (!Objects.equals(integer, oldValue)) {
                    e.v = integer;
                }
                return oldValue;
            }
        }
        Entry<Student, Integer> e = tab[h];
        tab[h] = new Entry<>(h, student, integer, e);
        if (++size >= threshold) {
            resize(tab.length * 2);
        }
        return null;
    }

    @Override
    public Integer remove(Object o) {
        int h = hash(o);
        Entry<Student, Integer>[] tab = getTable();
        Entry<Student, Integer> prev = tab[h];
        Entry<Student, Integer> e = prev;
        while (e != null) {
            Entry<Student, Integer> next = e.next;
            if (h == e.hash && eq(o, e.k)) {
                size--;
                if (prev == e) {
                    tab[h] = next;
                } else {
                    prev.next = next;
                }
                return e.v;
            }
            prev = e;
            e = next;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends Student, ? extends Integer> map) {
        Entry<Student, Integer>[] table = getTable();
        for (Entry<Student, Integer> entry : table) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        Entry<Student, Integer>[] entry;
        if ((entry = entryTable) != null && size > 0) {
            size = 0;
            IntStream.range(0, entry.length).forEach(i -> entry[i] = null);
        }
    }

    @Override
    public Set<Student> keySet() {
        Set<Student> ks = new HashSet<>();
        Entry<Student, Integer>[] table = getTable();
        for (Entry<Student, Integer> studentIntegerEntry : table) {
            if (studentIntegerEntry != null) {
                ks.add(studentIntegerEntry.getKey());
            }
        }
        return ks;
    }

    @Override
    public Collection<Integer> values() {
        Set<Integer> values = new HashSet<>();
        Entry<Student, Integer>[] table = getTable();
        for (Entry<Student, Integer> studentIntegerEntry : table) {
            if (studentIntegerEntry != null) {
                values.add(studentIntegerEntry.getValue());
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<Student, Integer>> entrySet() {
        Set<Map.Entry<Student, Integer>> entrySet = new HashSet<>();

        Entry<Student, Integer>[] table = getTable();

        for (Entry<Student, Integer> studentIntegerEntry : table) {
            if (studentIntegerEntry != null) {
                entrySet.add((Map.Entry<Student, Integer>) studentIntegerEntry);
            }
        }
        return entrySet;
    }

    private Entry<Student, Integer>[] getTable() {
        return entryTable;
    }

    private boolean containsNullValue() {
        Entry<Student, Integer>[] tab = getTable();
        for (int i = tab.length; i-- > 0;) {
            for (Entry<Student, Integer> e = tab[i]; e != null; e = e.next) {
                if (e.v == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void resize(int newCapacity) {
        Entry<Student, Integer>[] oldTable = getTable();
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        Entry<Student, Integer>[] newTable = newTable(newCapacity);
        transfer(oldTable, newTable);
        entryTable = newTable;

        if (size >= threshold / 2) {
            threshold = (int) (newCapacity * loadFactor);
        } else {
            transfer(newTable, oldTable);
            entryTable = oldTable;
        }
    }

    private void transfer(Entry<Student, Integer>[] src, Entry<Student, Integer>[] dest) {
        for (int j = 0; j < src.length; ++j) {
            Entry<Student, Integer> e = src[j];
            src[j] = null;
            while (e != null) {
                Entry<Student, Integer> next = e.next;
                if (e.k == null) {
                    e.next = null;
                    e.v = null;
                    size--;
                } else {
                    e.next = dest[e.hash];
                    dest[e.hash] = e;
                }
                e = next;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Entry<Student, Integer>[] newTable(int n) {
        return (Entry<Student, Integer>[]) new Entry<?, ?>[n];
    }

}

