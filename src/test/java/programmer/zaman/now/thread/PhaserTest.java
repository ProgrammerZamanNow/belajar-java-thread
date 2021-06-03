package programmer.zaman.now.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserTest {

  @Test
  void countDownLatch() throws InterruptedException {

    final var phaser = new Phaser();
    final var executor = Executors.newFixedThreadPool(15);

    phaser.bulkRegister(5);
    phaser.bulkRegister(5);

    for (int i = 0; i < 10; i++) {
      executor.execute(() -> {
        try {
          System.out.println("Start Task");
          Thread.sleep(2000);
          System.out.println("End Task");
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          phaser.arrive();
        }
      });
    }

    executor.execute(() -> {
      phaser.awaitAdvance(0);
      System.out.println("All tasks done");
    });

    executor.awaitTermination(1, TimeUnit.DAYS);

  }

  @Test
  void cyclicBarrier() throws InterruptedException {

    final var phaser = new Phaser();
    final var executor = Executors.newFixedThreadPool(15);

    phaser.bulkRegister(5);

    for (int i = 0; i < 5; i++) {
      executor.execute(() -> {
        phaser.arriveAndAwaitAdvance();
        System.out.println("DONE");
      });
    }

    executor.awaitTermination(1, TimeUnit.DAYS);

  }
}
