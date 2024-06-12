
package top.yonyong.bianque.boot.lib;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 16:55
 */
public class StopWatch {

  private final AtomicLong startTime;
  private final AtomicLong stopTime;
  private final AtomicLong elapsedTime;
  private final AtomicLong countDown;

  public StopWatch(long countDown, boolean start) {
    this.startTime = new AtomicLong(0);
    this.stopTime = new AtomicLong(0);
    this.elapsedTime = new AtomicLong(0);
    this.countDown = new AtomicLong(countDown);
    if (start) {
      this.start();
    }
  }

  public void start() {
    startTime.set(System.nanoTime());
  }

  public boolean stop() {
    stopTime.set(System.nanoTime());
    elapsedTime.set(stopTime.get() - startTime.get());
    return countDown.decrementAndGet() == 0;
  }

  public long getElapsedTime() {
    return elapsedTime.get();
  }
}
