package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;


/**
 * Use IPriorityQueue as interface.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // Implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // Fild that stores heaps and heap size
    private T[] heap;
    private int heapSize;

    /**
    * This method will initialize ArrayHeap Object.
    */
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
        return (T[]) (new Comparable[size]);
    }

    /**
    * This method will remove minimum value in a heap.
    * If size of the heap is zero, it throws EmptyContainerException.
    */
    @Override
    public T removeMin() {
        if (this.heapSize == 0) {
            throw new EmptyContainerException();
        } else {
            // Get minimum value
            T minVal = this.heap[0];
            int lastNodeIdx = heapSize - 1;
            // Change root node of a tree (minimum value) to a leaf node
            this.heap[0] = this.heap[lastNodeIdx];
            // remove a node in last of heap
            this.heap[lastNodeIdx] = null;
            heapSize--;
            percolateDown(0);
            return minVal;
        }
    }

    /*
    * This method will return minimum value in a heap.
    * If size of the heap is zero, it throws EmptyContainerException.
    */
    @Override
    public T peekMin() {
        if (this.heapSize == 0) {
            throw new EmptyContainerException();
        } else {
            return this.heap[0];
        }
    }

    /*
    * This method insert the given value into the heap.
    * If the given item is null, it throws IllegalArgumentException.
    * Also, if there is no space in the heap, it will resize.
    */
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

    /*
    * This method will return the size of the heap.
    */
    @Override
    public int size() {
        return heapSize;
    }

    /*
    * This method will do percolate down.
    */
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

    /*
    * This method will do percolate up.
    */
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

    /*
    * This method will resi the heap if the heap has no space.
    */
    private void resize() {
        int oldSize = this.heap.length;
        T[] newHeap = makeArrayOfT(oldSize * 2);
        for (int i = 0; i < oldSize; i++) {
            newHeap[i] = this.heap[i];
        }
        this.heap = newHeap;
    }

}
