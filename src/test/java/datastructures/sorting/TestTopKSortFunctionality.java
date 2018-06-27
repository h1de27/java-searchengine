package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testkisInvalid() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        try {
            Searcher.topKSort(-10, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //
        }
    }

    @Test(timeout=SECOND)
    public void testkIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> kSort = Searcher.topKSort(0, list);
        assertTrue(kSort.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testListEmpty() {
            IList<Integer> list = new DoubleLinkedList<>();
            IList<Integer> kSort = Searcher.topKSort(5, list);
            assertTrue(kSort.isEmpty());
    }

    public void testListLessThanK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(2);
        list.add(4);
        IList<Integer> kSort = Searcher.topKSort(5, list);
        assertTrue(kSort.indexOf(2) == 1);
        assertTrue(kSort.indexOf(4) == 2);
        assertEquals(kSort.size(), 2);

        IList<String> list2 = new DoubleLinkedList<>();
        list2.add("foo");
        list2.add("ah");
        IList<String> kSort2 = Searcher.topKSort(5, list2);
        assertTrue(kSort2.indexOf("ah") == 1);
        assertTrue(kSort2.indexOf("foo") == 2);
        assertEquals(kSort2.size(), 2);
    }

    @Test(timeout=SECOND)
    public void testSmallK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> kSort = Searcher.topKSort(3, list);
        for (int i = 0; i < kSort.size(); i++) {
            assertEquals(7 + i, kSort.get(i));
        }
        assertEquals(3, kSort.size());
    }

    @Test(timeout=SECOND)
    public void testKEqualsInputLength() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> kSort = Searcher.topKSort(10, list);
        for (int i = 0; i < 10; i++) {
            assertEquals(i, kSort.get(i));
        }
    }



}
