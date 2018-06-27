package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;


/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int heapSize;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heapSize = 0;
        this.heap = makeArrayOfT(1);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (this.heapSize == 0) {
            throw new EmptyContainerException();
        } else {
            T minVal = this.heap[0];
            int lastNodeIdx = heapSize - 1;
            this.heap[0] = this.heap[lastNodeIdx];
            this.heap[lastNodeIdx] = null;
            heapSize--;
            percolateDown(0);
            return minVal;
        }
    }

    @Override
    public T peekMin() {
        if (this.heapSize == 0) {
            throw new EmptyContainerException();
        } else {
            return this.heap[0];
        }
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        } else {
            if (heapSize == this.heap.length) {
                resize();
            }
            this.heap[heapSize] = item;
            percolateUp(heapSize);
            heapSize++;
        }
    }

    @Override
    public int size() {
        return heapSize;
    }

    private void percolateDown(int index) {
        if (index < heapSize && this.heap[index] != null) {
            int topIdx = index;
            int count = 1;
            int leftChild = NUM_CHILDREN * index;
            while (leftChild <= NUM_CHILDREN * index + 4) {
                leftChild = NUM_CHILDREN * index + count;
                if (leftChild < heapSize && this.heap[leftChild].compareTo(heap[topIdx]) < 0) {
                    topIdx = leftChild;
                }
                count++;
            }
            if (topIdx != index) {
                T temp = this.heap[index];
                this.heap[index] = this.heap[topIdx];
                this.heap[topIdx] = temp;
                percolateDown(topIdx);
            }
        }

    }

    private void percolateUp(int index) {
        if (this.heap[index] != null) {
            int parentNodeIdx = (index - 1)/4;
            if (this.heap[parentNodeIdx].compareTo(this.heap[index]) > 0) {
                T temp = this.heap[index];
                this.heap[index] = this.heap[parentNodeIdx];
                this.heap[parentNodeIdx] = temp;
                percolateUp(parentNodeIdx);
            }
        }


    }

    private void resize() {
        int oldSize = this.heap.length;
        T[] newHeap = makeArrayOfT(oldSize * 2);
        for (int i = 0; i < oldSize; i++) {
            newHeap[i] = this.heap[i];
        }
        this.heap = newHeap;
    }

}
