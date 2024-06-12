package top.yonyong.bianque.bianque.web.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import top.yonyong.bianque.bianque.util.VertxResult;

/**
 * @author yonyong
 */
@Slf4j
public class MetricRouter {
  private static Vertx vertx;

  public static Router createRouter(Vertx vertx) {
    MetricRouter.vertx = vertx;
    Router router = Router.router(vertx);
    router.route("/reset").handler(MetricRouter::handleReset);
    return router;
  }

  private static void handleReset(RoutingContext context) {
    VertxResult.writeSuc(context, "suc");
  }
}
