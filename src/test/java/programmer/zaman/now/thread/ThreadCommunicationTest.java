package programmer.zaman.now.thread;

import org.junit.jupiter.api.Test;

public class ThreadCommunicationTest {

  private String message = null;

  @Test
  void manual() throws InterruptedException {

    var thread1 = new Thread(() -> {
      while (message == null){
        // wait
      }
      System.out.println(message);
    });

    var thread2 = new Thread(() -> {
      message = "EKo Kurniawan Khannedy";
    });

    thread2.start();
    thread1.start();

    thread2.join();
    thread1.join();

  }

  @Test
  void waitNotify() throws InterruptedException {

    final var lock = new Object();

    var thread1 = new Thread(() -> {
      synchronized (lock){
        try {
          lock.wait();
          System.out.println(message);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    var thread2 = new Thread(() -> {
      synchronized (lock){
        message = "EKo Kurniawan Khannedy";
        lock.notify();
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

  }

  @Test
  void waitNotifyAll() throws InterruptedException {

    final var lock = new Object();

    var thread1 = new Thread(() -> {
      synchronized (lock){
        try {
          lock.wait();
          System.out.println(message);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    var thread3 = new Thread(() -> {
      synchronized (lock){
        try {
          lock.wait();
          System.out.println(message);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    var thread2 = new Thread(() -> {
      synchronized (lock){
        message = "EKo Kurniawan Khannedy";
        lock.notifyAll();
      }
    });

    thread1.start();
    thread3.start();

    thread2.start();

    thread1.join();
    thread3.join();

    thread2.join();

  }
}
