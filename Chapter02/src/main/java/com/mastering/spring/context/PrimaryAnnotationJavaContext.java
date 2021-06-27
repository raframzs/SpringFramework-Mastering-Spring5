package com.mastering.spring.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

interface SortingAlgorithm {
    void quickSort(int[] A, int left, int right);

    void mergeSort(int[] A, int n);
}

@Configuration
@ComponentScan(basePackages = {"com.mastering.spring"})
class PrimaryAnnotationSpringContext {

}

@Component
@Qualifier("mergesort")
class MergeSort implements SortingAlgorithm {
    @Override
    public void quickSort(int[] A, int left, int right) {

    }

    @Override
    public void mergeSort(int[] a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        int[] l = new int[mid];
        int[] r = new int[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
    }

    public static void merge(
            int[] a, int[] l, int[] r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i] <= r[j]) {
                a[k++] = l[i++];
            } else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }
}

@Component
@Primary
class QuickSort implements SortingAlgorithm {
    @Override
    public void quickSort(int[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(int arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    @Override
    public void mergeSort(int[] A, int n) {

    }
}

@Component
class SomeService {
    @Autowired
    @Qualifier("mergesort")
    SortingAlgorithm algorithm;

}

public class PrimaryAnnotationJavaContext {

    public static Logger logger = Logger
            .getLogger(PrimaryAnnotationJavaContext.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                PrimaryAnnotationSpringContext.class);

        SortingAlgorithm algorithm = context.getBean(SortingAlgorithm.class);
        logger.debug(algorithm);

        /**
         * For this example, SomeService is a concrete class that
         * could have some kind of logic. service.algorithm would be the
         * action to be performed. But with the learned, this is not good at all
         * the best move here would be:
         *
         * 1. SomeService as a interface that defines methods to MergeSort and QuickSort
         * 2. SomeServiceImpl would be the class that implements this interface
         * 3. Run!
         *
         */
        SomeService service = context.getBean(SomeService.class);
        logger.debug(service.algorithm);

    }
}
