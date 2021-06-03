package programmer.zaman.now.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {

  @Test
  void testExecutorService() throws InterruptedException {

    var executor = Executors.newSingleThreadExecutor();

    for (int i = 0; i < 100; i++) {
      executor.execute(() -> {
        try {
          Thread.sleep(1000);
          System.out.println("Runnable in thread : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }

    executor.awaitTermination(1, TimeUnit.DAYS);

  }

  @Test
  void testExecutorServiceFix() throws InterruptedException {

    var executor = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 100; i++) {
      executor.execute(() -> {
        try {
          Thread.sleep(1000);
          System.out.println("Runnable in thread : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.DAYS);

  }
}
