package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testEmptySize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(3, heap.removeMin());
        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testPeekAndRemoveOnEmptyHeap() {
        // throw exception if the value is null
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //
        }

        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //
        }
    }

    @Test(timeout=SECOND)
    public void testHeapMatchesOtherHeap() {
        // use constructed arrayHeap at top and create another heap using insert. Test to make sure
        // that they're equal to each other
        IPriorityQueue<Integer> heap1 = this.makeInstance();
        IPriorityQueue<Integer> heap2 = this.makeInstance();

        heap1.insert(25);
        heap1.insert(60);
        heap1.insert(2);
        heap1.insert(15);
        heap1.insert(3);

        heap2.insert(25);
        heap2.insert(60);
        heap2.insert(2);
        heap2.insert(15);
        heap2.insert(3);

        for (int i = 0; i < 5; i++) {
            assertEquals(heap1.removeMin(), heap2.removeMin());
        }

        IPriorityQueue<String> heap3 = this.makeInstance();
        IPriorityQueue<String> heap4 = this.makeInstance();

        heap3.insert("one");
        heap3.insert("two");
        heap3.insert("three");

        heap4.insert("one");
        heap4.insert("two");
        heap4.insert("three");

        for (int i = 0; i < 3; i++) {
            assertEquals(heap3.removeMin(), heap4.removeMin());
        }

    }

    @Test(timeout=SECOND)
    public void testResizeHeap() {
        // resize the heap and make sure it is equal to another\
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i <= 15; i++) {
            heap.insert(i);
        }
        assertEquals(heap.size(), 16);
    }

    @Test(timeout=SECOND)
    public void testInsertInRandomOrder() {
        // test inserting a value into heap and make sure it matches another
        IPriorityQueue<Integer> heap1 = this.makeInstance();
        heap1.insert(1000);
        heap1.insert(74);
        heap1.insert(6);
        heap1.insert(12);
        assertEquals(heap1.peekMin(), 6);

        IPriorityQueue<String> heap2 = this.makeInstance();
        heap2.insert("cat");
        heap2.insert("apple");
        heap2.insert("dog");
        assertEquals(heap2.peekMin(), "apple");
    }

    @Test(timeout=SECOND)
    public void testDuplicateValuesInHeap() {
        // insert duplicate values and compare if the heap is equal to the expected values
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(2);
        heap.insert(1);
        heap.insert(2);
        heap.insert(1);
        assertEquals(4, heap.size());
        assertEquals(heap.peekMin(), 1);

        IPriorityQueue<String> heap2 = this.makeInstance();
        heap2.insert("apple");
        heap2.insert("APPLE");
        heap2.insert("apple");
        heap2.insert("banana");
        heap2.insert("APPLE");
        assertEquals(5, heap2.size());
        assertEquals(heap2.peekMin(), "APPLE");


    }

    @Test(timeout=SECOND)
    public void testDuplicateMany() {
        // insert duplicate values and compare if the heap is equal to the expected values
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i <= 10000; i++) {
            heap.insert(1);
        }
        assertEquals(10000, heap.size());
        assertEquals(heap.peekMin(), 1);

        for (int i = 1; i <= 10000; i++) {
            assertEquals(heap.removeMin(), 1);
        }

        IPriorityQueue<String> heap2 = this.makeInstance();
        for (int i = 1; i <= 10000; i++) {
            heap2.insert("string");
        }
        assertEquals(10000, heap2.size());
        assertEquals(heap2.peekMin(), "string");

        for (int i = 1; i <= 10000; i++) {
            assertEquals(heap2.removeMin(), "string");
        }
    }


    @Test(timeout=SECOND)
    public void testPriorityWithNegativeNumbers() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 10; i >= -6; i-=2) {
            heap.insert(i);
        }
        assertEquals(9, heap.size());
        assertEquals(heap.peekMin(), -6);
    }

    @Test(timeout=SECOND)
    public void testRemoveMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i <= 17; i+=2) {
            heap.insert(i);
        }

        assertEquals(9, heap.size());
        assertEquals(heap.peekMin(), 1);

        int j = 1;
        for (int i = 17; i >= 0; i-=2) {
            assertEquals(heap.peekMin(), j);
            j+=2;
            heap.removeMin();
        }
        assertEquals(heap.size(), 0);
    }

    @Test(timeout=SECOND)
    public void testInsertNullValue() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //
        }
    }

    @Test(timeout=SECOND)
    public void testCompareLists() {
        List<Integer> list1 = new ArrayList<Integer>();
        IPriorityQueue<Integer> heap = this.makeInstance();
        IList<Integer> list2 = new DoubleLinkedList<Integer>();

        list1.add(7);
        list1.add(3);
        list1.add(6);
        list1.add(10);
        list1.add(25);

        for (int i = 0; i < list1.size(); i++) {
            heap.insert(list1.get(i));
        }

        for (int i = 0; i < heap.size(); i++) {
            list2.add(heap.removeMin());
        }

        // sort list1

        Collections.sort(list1);
        for (int i = 0; i < list2.size(); i++) {
            assertTrue(list2.get(i) == list1.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testBlankStringHeap() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("");
        heap.insert("a");
        heap.insert("");
        heap.insert("b");

        assertEquals(heap.peekMin(), "");
        assertEquals(heap.size(), 4);

    }


}
