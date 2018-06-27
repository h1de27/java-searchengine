package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;
import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {

        assertTrue(true);
    }

    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=10*SECOND)
    public void testArrayHeapInputMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i <= 100000; i++) {
            heap.insert(i);
            assertEquals(i, heap.size());
        }
        assertEquals(100000, heap.size());

        for (int i = 1; i <= 100000; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(i, heap.removeMin());
        }
        assertEquals(0, heap.size());
    }

    @Test(timeout=10*SECOND)
    public void testCompareDuplcateLists() {
        List<Integer> list1 = new ArrayList<Integer>();
        IPriorityQueue<Integer> heap = this.makeInstance();
        IList<Integer> list2 = new DoubleLinkedList<Integer>();

        for (int i = 1; i <= 100000; i++) {
            list1.add(1);
        }

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

    @Test(timeout=10*SECOND)
    public void testStressTopKSearch() {
        IList<Integer> list1 = new DoubleLinkedList<>();
        List<Integer> list2 = new ArrayList<Integer>();
        List<Integer> list3 = new ArrayList<Integer>();
        for (int i = 500000; i > 0; i--) {
            list1.add(i);
            list2.add(i);
        }
        IList<Integer> kSort = Searcher.topKSort(500000, list1);
        Collections.sort(list2);
        for (int value: kSort) {
            list3.add(value);
        }
        assertEquals(list2, list3);
    }

    @Test(timeout=10*SECOND)
    public void testStresstopKSearchMoreThanK() {
        IList<Integer> list1 = new DoubleLinkedList<>();
        List<Integer> list2 = new ArrayList<Integer>();
        List<Integer> list3 = new ArrayList<Integer>();
        for (int i = 100000; i > 0; i--) {
            list1.add(i);
            list2.add(i);
        }
        IList<Integer> kSort = Searcher.topKSort(500000, list1);
        Collections.sort(list2);
        for (int value: kSort) {
            list3.add(value);
        }
        assertEquals(list2, list3);
    }

    @Test(timeout=10*SECOND)
    public void testStresstopKSearchlessThanK() {
        IList<Integer> list1 = new DoubleLinkedList<>();
        List<Integer> list2 = new ArrayList<Integer>();
        List<Integer> list3 = new ArrayList<Integer>();
        for (int i = 1000000; i > 0; i--) {
            list1.add(i);
            list2.add(i);
        }
        IList<Integer> kSort = Searcher.topKSort(500000, list1);
        Collections.sort(list2);
        for (int value: kSort) {
            list3.add(value);
        }
        assertEquals(list2.subList(500000, 1000000), list3);
    }
}
