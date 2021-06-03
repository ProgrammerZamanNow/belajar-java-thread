package programmer.zaman.now.thread;

public class MainApplication {

  public static void main(String[] args) {
    var name = Thread.currentThread().getName();
    System.out.println(name);
  }
}
