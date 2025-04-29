package org.example;

import org.example.TestCase;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TestCase tester = new TestCase();
        tester.setUp();
        tester.addProductToCartTest();
        tester.tearDown();
    }
}
