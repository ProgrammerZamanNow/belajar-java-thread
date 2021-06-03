package programmer.zaman.now.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadpoolTest {

  @Test
  void create() {

    var minThread = 10;
    var maxThread = 100;
    var alive = 1;
    var aliveTime = TimeUnit.MINUTES;
    var queue = new ArrayBlockingQueue<Runnable>(100);

    var threadpool = new ThreadPoolExecutor(minThread, maxThread, alive, aliveTime, queue);

  }

  @Test
  void executeRunnable() throws InterruptedException {

    var minThread = 10;
    var maxThread = 100;
    var alive = 1;
    var aliveTime = TimeUnit.MINUTES;
    var queue = new ArrayBlockingQueue<Runnable>(100);

    var executor = new ThreadPoolExecutor(minThread, maxThread, alive, aliveTime, queue);

    Runnable runnable = () -> {
      try {
        Thread.sleep(5000);
        System.out.println("Runnable from thread : " + Thread.currentThread().getName());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
    executor.execute(runnable);

    Thread.sleep(6000);
  }

  @Test
  void shutdown() throws InterruptedException {

    var minThread = 10;
    var maxThread = 100;
    var alive = 1;
    var aliveTime = TimeUnit.MINUTES;
    var queue = new ArrayBlockingQueue<Runnable>(1000);

    var executor = new ThreadPoolExecutor(minThread, maxThread, alive, aliveTime, queue);

    for (int i = 0; i < 1000; i++) {
      final var task = i;
      Runnable runnable = () -> {
        try {
          Thread.sleep(1000);
          System.out.println("Task " + task + " from thread : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      };
      executor.execute(runnable);
    }

    // executor.shutdownNow();
    executor.awaitTermination(1, TimeUnit.DAYS);
  }

  @Test
  void rejected() throws InterruptedException {

    var minThread = 10;
    var maxThread = 100;
    var alive = 1;
    var aliveTime = TimeUnit.MINUTES;
    var queue = new ArrayBlockingQueue<Runnable>(10);
    var rejectedHandler = new LogRejectedExecutionHandler();

    var executor = new ThreadPoolExecutor(minThread, maxThread, alive, aliveTime, queue, rejectedHandler);

    for (int i = 0; i < 1000; i++) {
      final var task = i;
      Runnable runnable = () -> {
        try {
          Thread.sleep(1000);
          System.out.println("Task " + task + " from thread : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      };
      executor.execute(runnable);
    }

    // executor.shutdownNow();
    executor.awaitTermination(1, TimeUnit.DAYS);
  }

  public static class LogRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      System.out.println("Task " + r + " is rejected");
    }
  }
}
