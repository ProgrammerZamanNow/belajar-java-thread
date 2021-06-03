package programmer.zaman.now.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ReactiveStreamTest {

  @Test
  void publish() throws InterruptedException {
    var publisher = new SubmissionPublisher<String>();
    var subscriber1 = new PrintSubscriber("A", 1000L);
    var subscriber2 = new PrintSubscriber("B", 500L);
    publisher.subscribe(subscriber1);
    publisher.subscribe(subscriber2);

    var executor = Executors.newFixedThreadPool(10);
    executor.execute(() -> {
      for (int i = 0; i < 100; i++) {
        publisher.submit("Eko-" + i);
        System.out.println(Thread.currentThread().getName() + " : Send Eko-" + i);
      }
    });

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.DAYS);

    Thread.sleep(100 * 1000);
  }

  @Test
  void buffer() throws InterruptedException {
    var publisher = new SubmissionPublisher<String>(Executors.newWorkStealingPool(), 10);
    var subscriber1 = new PrintSubscriber("A", 1000L);
    var subscriber2 = new PrintSubscriber("B", 500L);
    publisher.subscribe(subscriber1);
    publisher.subscribe(subscriber2);

    var executor = Executors.newFixedThreadPool(10);
    executor.execute(() -> {
      for (int i = 0; i < 100; i++) {
        publisher.submit("Eko-" + i);
        System.out.println(Thread.currentThread().getName() + " : Send Eko-" + i);
      }
    });

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.DAYS);

    Thread.sleep(100 * 1000);
  }

  @Test
  void processor() throws InterruptedException {

    var publisher = new SubmissionPublisher<String>();

    var processor = new HelloProcessor();
    publisher.subscribe(processor);

    var subscriber = new PrintSubscriber("A", 1000L);
    processor.subscribe(subscriber);

    var executor = Executors.newFixedThreadPool(10);
    executor.execute(() -> {
      for (int i = 0; i < 100; i++) {
        publisher.submit("Eko-" + i);
        System.out.println(Thread.currentThread().getName() + " : Send Eko-" + i);
      }
    });

    Thread.sleep(100 * 1000);
  }

  public static class PrintSubscriber implements Flow.Subscriber<String> {

    private Flow.Subscription subscription;

    private String name;

    private Long sleep;

    public PrintSubscriber(String name, Long sleep) {
      this.name = name;
      this.sleep = sleep;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      this.subscription = subscription;
      this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
      try {
        Thread.sleep(sleep);
        System.out.println(Thread.currentThread().getName() + " : " + name + " : " + item);
        this.subscription.request(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onError(Throwable throwable) {
      throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
      System.out.println(Thread.currentThread().getName() + " : DONE");
    }
  }

  public static class HelloProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      this.subscription = subscription;
      this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
      var value = "Hello " + item;
      submit(value);
      this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
      throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
      close();
    }
  }

}
