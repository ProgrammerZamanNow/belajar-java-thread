package programmer.zaman.now.thread;

import java.util.concurrent.atomic.AtomicLong;

public class CounterAtomic {

  private AtomicLong value = new AtomicLong(0L);

  public void increment(){
    value.incrementAndGet();
  }

  public Long getValue() {
    return value.get();
  }
}
