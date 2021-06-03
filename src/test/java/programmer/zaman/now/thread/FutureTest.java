package programmer.zaman.now.thread;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FutureTest {

  @Test
  void testFuture() throws ExecutionException, InterruptedException {

    var executor = Executors.newSingleThreadExecutor();

    Callable<String> callable = () -> {
      Thread.sleep(5000);
      return "hi";
    };

    Future<String> future = executor.submit(callable);
    System.out.println("Selesai Future");

    while (!future.isDone()) {
      System.out.println("Waiting future");
      Thread.sleep(1000);
    }

    String value = future.get();
    System.out.println(value);

  }

  @Test
  void testFutureCancel() throws ExecutionException, InterruptedException {

    var executor = Executors.newSingleThreadExecutor();

    Callable<String> callable = () -> {
      Thread.sleep(5000);
      return "hi";
    };

    Future<String> future = executor.submit(callable);
    System.out.println("Selesai Future");

    Thread.sleep(2000);
    future.cancel(true);

    System.out.println(future.isCancelled());
    String value = future.get();
    System.out.println(value);

  }

  @Test
  void invokeAll() throws InterruptedException, ExecutionException {
    var executor = Executors.newFixedThreadPool(10);

    List<Callable<String>> callables = IntStream.range(1, 11).mapToObj(value -> (Callable<String>) () -> {
      Thread.sleep(value * 500L);
      return String.valueOf(value);
    }).collect(Collectors.toList());

    var futures = executor.invokeAll(callables);

    for (Future<String> stringFuture : futures) {
      System.out.println(stringFuture.get());
    }
  }

  @Test
  void invokeAny() throws InterruptedException, ExecutionException {
    var executor = Executors.newFixedThreadPool(10);

    List<Callable<String>> callables = IntStream.range(1, 11).mapToObj(value -> (Callable<String>) () -> {
      Thread.sleep(value * 500L);
      return String.valueOf(value);
    }).collect(Collectors.toList());

    var value = executor.invokeAny(callables);
    System.out.println(value);
  }
}
