package com.endava.internship.collections;

import java.util.*;
import java.util.stream.Collectors;

public class StudentMap<K extends Comparable<K>, V> implements Map<K, V> {
    
    static class Entry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private V value;

        private Entry<K, V> left;
        private Entry<K, V> right;
        private Entry<K, V> parent;

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

    private int compare(K key1, K key2) {
        if(key1 == null && key2 == null) {
            return 0;
        } else {
            if (key1 == null) {
                return -1;
            } else if (key2 == null) {
                return 1;
            }
        }

        if(comparator == null) {
            Comparable<? super K> comparableKey = (Comparable<? super K>) key1;
            return comparableKey.compareTo(key2);
        } else {
            return comparator.compare(key1, key2);
        }
    }

    private Entry<K, V> search(Object key) {
        Entry<K, V> currentNode = root;
        int compareResult = compare((K) key, currentNode.key);
        try {
            if(compareResult != 0) {
                do {
                    compareResult = compare((K) key, currentNode.key);
                    if(compareResult > 0) {
                        if(currentNode.right == null) {
                            return null;
                        }
                        currentNode = currentNode.right;
                    } else
                    if(compareResult < 0) {
                        if(currentNode.left == null) {
                            return null;
                        }
                        currentNode = currentNode.left;
                    }
                } while(compareResult != 0);
            }
        } catch (ClassCastException e) {
            System.out.println(e);
        }
        return currentNode;
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
        Entry<K, V> foundEntry = search(key);
        if(foundEntry != null) {
            return foundEntry.value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if(root == null) {
            root = new Entry<>(key, value);
            size++;
        } else {
            Entry<K, V> currentNode = root;
                do {
                    int compareResult = compare(currentNode.key, key);
                    if (compareResult > 0) {
                        if (currentNode.left == null) {
                            currentNode.left = new Entry<>(key, value);
                            currentNode.left.parent = currentNode;
                            size++;
                            return currentNode.left.value;
                        }
                        currentNode = currentNode.left;
                    } else {
                        if (compareResult < 0) {
                            if (currentNode.right == null) {
                                currentNode.right = new Entry<>(key, value);
                                currentNode.right.parent = currentNode;
                                size++;
                                return currentNode.right.value;
                            }
                            currentNode = currentNode.right;
                        } else {
                            V v = currentNode.value;
                            currentNode.value = value;
                            return v;
                        }
                    }
                } while (true);
            }
        return null;
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> removingNode = search(key);

        if(removingNode == null) return null;

        V oldValue = removingNode.value;

        Entry<K, V> leftNode = removingNode.left;
        Entry<K, V> rightNode = removingNode.right;

        int leftSubTreeSize = 0;
        int rightSubTreeSize = 0;

        if(leftNode != null) {
            while (leftNode.right != null) {
                leftNode = leftNode.right;
                leftSubTreeSize++;
            }
        }
        if(rightNode != null) {
            while (rightNode.left != null) {
                rightNode = rightNode.left;
                rightSubTreeSize++;
            }
        }
        if(rightNode == null && leftNode == null) {
            if(removingNode.parent.right == removingNode) {
                removingNode.parent.right = null;
            } else {
                removingNode.parent.left = null;
            }
            size--;
            return oldValue;
        }

        if(leftSubTreeSize > rightSubTreeSize && leftNode != null) {
            if(leftNode.parent.left == leftNode) {
                leftNode.parent.left = leftNode.right;
            }

            leftNode.parent.right = leftNode.left;

            if(leftNode.left != null) {
                System.out.println(leftNode);
                leftNode.left.parent = leftNode.parent;
            }
        } else {
            if(leftSubTreeSize < rightSubTreeSize || leftNode == null) {
                if(rightNode.parent.right == rightNode) {
                    rightNode.parent.right = rightNode.right;
                }

                rightNode.parent.left = rightNode.right;

                if(rightNode.right != null) {
                    rightNode.right.parent = rightNode.parent;
                }

                leftNode = rightNode;
            }
        }

        leftNode.left = removingNode.left;
        leftNode.right = removingNode.right;
        leftNode.parent = removingNode.parent;

        if(removingNode.left != null) {
            removingNode.left.parent = leftNode;
        }

        if(removingNode.right != null) {
            removingNode.right.parent = leftNode;
        }

        if(removingNode.parent != null) {
            if(removingNode.parent.right == removingNode) {
                removingNode.parent.right = leftNode;
            } else {
                removingNode.parent.left = leftNode;
            }
        }

        if(removingNode == root)  {
            root = leftNode;
        }

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
        return entrySet()
                .stream()
                .map(currentNode->currentNode.getKey())
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return entrySet()
                .stream()
                .map(currentNode->currentNode.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entriesSet = new LinkedHashSet<>();
        Stack<Entry<K, V>> helperStack = new Stack<>();

        Entry<K, V> currentNode = root;

        if(currentNode == null) {
            return entriesSet; //fixed
        }

        helperStack.push(currentNode);

        while(!helperStack.isEmpty()) {
            if(currentNode != null) {
                helperStack.push(currentNode);
                currentNode = currentNode.left;
            } else {
                currentNode = helperStack.pop();
                entriesSet.add(currentNode);
                currentNode = currentNode.right;
            }
        }

        return entriesSet;
    }

    @Override
    public String toString() {
        return toString(root);
    }

    private String toString(Entry<K, V> tRoot) {
        if(tRoot == null) {
            return null;
        }

        if(tRoot.left == null && tRoot.right == null) {
            return tRoot.toString();
        }

        if(tRoot.left == null) {
            return tRoot + toString(tRoot.right);
        }

        if(tRoot.right == null) {
            return toString(tRoot.left) + tRoot;
        }

        return toString(tRoot.left) + tRoot + toString(tRoot.right);
    }
}

