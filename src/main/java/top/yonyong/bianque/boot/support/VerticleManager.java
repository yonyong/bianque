package top.yonyong.bianque.boot.support;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import top.yonyong.bianque.boot.support.annotation.Verticle;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 17:06
 */
@Slf4j
public class VerticleManager {

  private static Vertx vertx;

  /**
   * get vertx
   *
   * @return vertx
   */
  public static Vertx getVertx() {
    if (null == vertx) {
      vertx = Vertx.vertx();
    }
    return vertx;
  }

  /**
   * deploy verticle
   *
   * @param clazz verticle clazz
   */
  public static void deploy(Class<?> clazz) {
    getVertx();
    log.info("start deploy verticle:{}", clazz.getName());
    if (!AbstractVerticle.class.isAssignableFrom(clazz)) {
      log.error("{} does not inherit class : io.vertx.core.AbstractVerticle", clazz.getName());
      return;
    }

    final Verticle verticle = clazz.getAnnotation(Verticle.class);
    if (log.isDebugEnabled()) {
      log.debug(verticle.toString());
    }
    final DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorker(verticle.isWork());
    deploymentOptions.setInstances(verticle.instance());
    if (!verticle.workPoolName().isEmpty()) {
      deploymentOptions.setWorkerPoolName(verticle.workPoolName());
    }
    if (-1 != verticle.workPoolSize()) {
      deploymentOptions.setWorkerPoolSize(verticle.workPoolSize());
    }
    try {
      vertx.deployVerticle(clazz.getName(), deploymentOptions);
      log.info("successfully deploy verticle:{}", clazz.getName());
    } catch (Exception e) {
      log.error("deploy verticle failed:", e);
    }
  }
}
