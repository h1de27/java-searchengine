package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.size == 0) {
            Node<T> current = new Node<T>(item);
            this.front = current;
            this.back = current;
        } else {
            Node<T> current = new Node<T>(this.back, item, null);
            this.back.next = current;
            this.back = current;
        }
        size++;
    }

    @Override
    public T remove() {
        T data;
        if (this.size == 0) {
            throw new EmptyContainerException();
        } else {
            if (this.size == 1){
                data = this.back.data;
                this.front = null;
                this.back = null;
            } else {
                data = this.back.data;
                this.back = this.back.prev;
                this.back.next = null;
            }
        }
        size--;
        return data;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> current = getNode(index);
            return current.data;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> temp = getNode(index);
            Node<T> newNode = new Node<T>(temp.prev, item, temp.next);
            if (index == 0) {
                this.front = newNode;
            } else {
                temp.prev.next = newNode;
            }
            if (index == this.size() - 1) {
                this.back = newNode;
            } else {
                temp.next.prev = newNode;
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                Node<T> newNode = new Node<T>(null, item, this.front);
                if (this.size() == 0) {
                    this.back = newNode;
                } else {
                    this.front.prev = newNode;
                }
                this.front = newNode;
            } else if (index == this.size()){
                Node<T> newNode = new Node<T>(this.back, item, null);
                this.back.next = newNode;
                this.back = newNode;
            } else {
                Node<T> temp = getNode(index);
                Node<T> nextNode = temp;
                Node<T> prevNode = temp.prev;
                Node<T> newNode = new Node<T>(prevNode, item, nextNode);
                prevNode.next = newNode;
                nextNode.prev = newNode;
            }
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = getNode(index);
        if (current == this.front && current == this.back) {
            this.front = null;
            this.back = null;
        } else if (index == 0) {
            current.next.prev = null;
            this.front = current.next;
        } else if (index == this.size() - 1) {
            current.prev.next = null;
            this.back = current.prev;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }
        size--;
        return current.data;
    }

    @Override
    public int indexOf(T item) {
        Node<T> temp = this.front;
        int index = 0;
        while (temp != null) {
            if (temp.data == item || temp.data.equals(item)) {
                return index;
            } else {
                temp = temp.next;
                index++;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> temp = this.front;
        while (temp != null) {
            if (temp.data == other || temp.data.equals(other)) {
                return true;
            } else {
                temp = temp.next;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private Node<T> getNode(int index) {
        Node<T> temp;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index <= this.size() / 2) {
            temp = this.front;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = this.back;
            for (int i = this.size-1; i > index; i--) {
                temp = temp.prev;
            }
        }
        return temp;
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return this.current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (this.current == null) {
                throw new NoSuchElementException();
            } else {
                T data = this.current.data;
                this.current = this.current.next;
                return data;
            }
        }
    }
}
