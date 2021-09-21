package com.endava.internship.collections;

import java.util.*;
import java.util.stream.Collectors;

public class StudentMap<K extends Comparable<K>, V> implements Map<K, V> {
    static class Entry<K, V> implements Map.Entry<K, V> {

        K key;
        V value;

        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return "{" + key + " = " + value + "}\n";
        }
    }

    private Entry<K, V> root;
    private int size;
    private final Comparator<? super K> comparator;

    public StudentMap() {
        size = 0;
        comparator = null;
    }

    public StudentMap(Comparator<? super  K> comparator) {
        size = 0;
        this.comparator = comparator;
    }

    private Entry<K, V> search(Object key) {
        Entry<K, V> t = root;
        if(comparator == null) {
            try {
                Comparable<? super K> k = (Comparable<? super K>) key;
                if(k.compareTo(t.key) != 0) {
                    do {
                        //System.out.println(t);
                        if(k.compareTo(t.key) > 0) {
                            if(t.right == null) return null;
                            t = t.right;
                        } else
                        if(k.compareTo(t.key) < 0) {
                            if(t.left == null) return null;
                            t = t.left;
                        }
                    } while(k.compareTo(t.key) != 0);
                }
            } catch (ClassCastException e) {
                System.out.println(e);
            }
            //System.out.println(t);
        } else {
            try {
                if(comparator.compare((K)key, t.key) != 0) {
                    do {
                        //System.out.println(t);
                        if(comparator.compare((K)key, t.key) > 0) {
                            if(t.right == null) return null;
                            t = t.right;
                        } else
                        if(comparator.compare((K)key, t.key) < 0) {
                            if(t.left == null) return null;
                            t = t.left;
                        }
                    } while(comparator.compare((K)key, t.key) != 0);
                }
            } catch (ClassCastException e) {
                System.out.println(e);
            }
        }
        return t;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return search(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        return search(key).value;
    }

    @Override
    public V put(K key, V value) {
        if(root == null) {
            root = new Entry<>(key, value);
            size++;
        } else {
            Entry<K, V> tRoot = root;
            if(comparator == null) {
                do {
                    Comparable<? super K> k = (Comparable<? super K>) tRoot.key;
                    int cr = k.compareTo(key);
                    if (cr > 0) {
                        if (tRoot.left == null) {
                            tRoot.left = new Entry<>(key, value);
                            tRoot.left.parent = tRoot;
                            size++;
                            return tRoot.left.value;
                        }
                        tRoot = tRoot.left;
                    } else {
                        if (cr < 0) {
                            if (tRoot.right == null) {
                                tRoot.right = new Entry<>(key, value);
                                tRoot.right.parent = tRoot;
                                size++;
                                return tRoot.right.value;
                            }
                            tRoot = tRoot.right;
                        } else {
                            V v = tRoot.value;
                            tRoot.value = value;
                            return v;
                        }
                    }
                } while (tRoot != null);
            } else {
                do {
                    int cr = comparator.compare(tRoot.key, key);
                    if (cr > 0) {
                        if (tRoot.left == null) {
                            tRoot.left = new Entry<>(key, value);
                            tRoot.left.parent = tRoot;
                            size++;
                            return tRoot.left.value;
                        }
                        tRoot = tRoot.left;
                    } else {
                        if (cr < 0) {
                            if (tRoot.right == null) {
                                tRoot.right = new Entry<>(key, value);
                                tRoot.right.parent = tRoot;
                                size++;
                                return tRoot.right.value;
                            }
                            tRoot = tRoot.right;
                        } else {
                            V v = tRoot.value;
                            tRoot.value = value;
                            return v;
                        }
                    }
                } while (tRoot != null);
            }

        }
        return null;
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> t = search(key);

        if(t == null) return null;

        V oldValue = t.value;

        Entry<K, V> tl = t.left;
        Entry<K, V> tr = t.right;

        int lSize = 0, rSize= 0;
        if(tl != null) {
            while (tl.right != null) {
                tl = tl.right;
                lSize++;
            }
        }
        if(tr != null) {
            while (tr.left != null) {
                tr = tr.left;
                rSize++;
            }
        }
        //System.out.println(tl + " | " + tr);
        if(tr == null && tl == null) {
            if(t.parent.right == t)
                t.parent.right = null;
            else
                t.parent.left = null;
            size--;
            return oldValue;
        }

        if(lSize > rSize && tl != null) {
            if(tl.parent.left == tl)
                tl.parent.left = tl.right;
            tl.parent.right = tl.left;
            if(tl.left != null) {
                System.out.println(tl);
                tl.left.parent = tl.parent;
            }
        }
        else {
            if(lSize < rSize || tl == null) {
                if(tr.parent.right == tr)
                    tr.parent.right = tr.right;
                tr.parent.left = tr.right;
                if(tr.right != null) {
                    tr.right.parent = tr.parent;
                }
                tl = tr;
            }
        }

        tl.left = t.left;
        tl.right = t.right;
        tl.parent = t.parent;
        if(t.left != null)
            t.left.parent = tl;
        if(t.right != null)
            t.right.parent = tl;

        if(t.parent != null) {
            if(t.parent.right == t)
                t.parent.right = tl;
            else
                t.parent.left = tl;
        }
        if(t == root) root = tl;
        size--;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> e: m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return entrySet().stream().map(t->t.getKey()).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return entrySet().stream().map(t->t.getValue()).collect(Collectors.toList());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new LinkedHashSet<>();
        Stack<Entry<K, V>> stack = new Stack<>();
        Entry<K, V> t = root;
        stack.push(t);
        while(!stack.isEmpty()) {
            if(t != null) {
                stack.push(t);
                t = t.left;
            } else {
                t = stack.pop();
                set.add(t);
                t = t.right;
            }
        }
        return set;
    }

    @Override
    public String toString() {
        return toString(root);
    }

    public String toString(Entry<K, V> tRoot) {
        if(tRoot == null) return null;
        if(tRoot.left == null && tRoot.right == null)
            return tRoot.toString();
        if(tRoot.left == null)
            return tRoot.toString() + toString(tRoot.right);
        if(tRoot.right == null)
            return toString(tRoot.left) + tRoot.toString();
        return toString(tRoot.left) + tRoot.toString() + toString(tRoot.right);
    }
}

