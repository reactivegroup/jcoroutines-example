package com.reactive.jcoroutines;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;

import java.util.concurrent.ExecutionException;

/**
 * Increasing-Echo Quasar Example
 */
public class QuasarIncreasingEchoApp {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        doAll();
    }

    public static Integer doAll() throws ExecutionException, InterruptedException {
        // Synchronizing channel (buffer = 0)
        final IntChannel increasingToEcho = Channels.newIntChannel(0);
        // Synchronizing channel (buffer = 0)
        final IntChannel echoToIncreasing = Channels.newIntChannel(0);

        Fiber<Integer> increasing = new Fiber<>("INCREASER", () -> {
            ////// The following is enough to test instrumentation of synchronizing methods
            int curr = 0;
            for (int i = 0; i < 10; i++) {
                Fiber.sleep(10);
                System.out.println("INCREASER sending: " + curr);
                increasingToEcho.send(curr);
                curr = echoToIncreasing.receive();
                System.out.println("INCREASER received: " + curr);
                curr++;
                System.out.println("INCREASER now: " + curr);
            }
            System.out.println("INCREASER closing channel and exiting");
            increasingToEcho.close();
            return curr;
        }).start();

        Fiber<Void> echo = new Fiber<Void>("ECHO", () -> {
            Integer curr;
            while (true) {
                Fiber.sleep(1000);
                curr = increasingToEcho.receive();
                System.out.println("ECHO received: " + curr);

                if (curr != null) {
                    System.out.println("ECHO sending: " + curr);
                    echoToIncreasing.send(curr);
                } else {
                    System.out.println("ECHO detected closed channel, closing and exiting");
                    echoToIncreasing.close();
                    return;
                }
            }
        }).start();

        try {
            increasing.join();
            echo.join();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return increasing.get();
    }
}