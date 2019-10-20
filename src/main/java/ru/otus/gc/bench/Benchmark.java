package ru.otus.gc.bench;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by tully.
 */
class Benchmark implements BenchmarkMBean {

    private final int loopCounter;

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    private static List<String> stringContainer = new ArrayList<>();

    public void run() throws InterruptedException {
        String stringWithPrefix = "stringWithPrefix";

        long beginTime = System.currentTimeMillis();

        while (stringContainer.size() <= loopCounter * 0.97) {
            for (double i = 0; i < loopCounter; i++) {
                String newString = stringWithPrefix + i;
                stringContainer.add(newString);
            }
            System.out.println("List size after adding: " + stringContainer.size());
            Thread.sleep(5000);

            stringContainer = stringContainer.subList(stringContainer.size() / 2, stringContainer.size());

            System.out.println("List size after removal: " + stringContainer.size());
            Thread.sleep(5000);
            System.out.println("time:" + (System.currentTimeMillis() - beginTime) +"ms");
        }
        System.out.println("Total time:" + (System.currentTimeMillis() - beginTime)+"ms");
    }
}