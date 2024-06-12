package top.yonyong.bianque.bianque.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import top.yonyong.bianque.bianque.util.VertxResult;
import top.yonyong.bianque.bianque.web.router.MetricRouter;
import top.yonyong.bianque.bianque.web.router.ResourceRouter;
import top.yonyong.bianque.boot.support.Ordered;
import top.yonyong.bianque.boot.support.annotation.Order;
import top.yonyong.bianque.boot.support.annotation.Verticle;

/**
 * @author yonyong
 */
@Slf4j
@Verticle
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    MDC.put("instanceId", vertx.getOrCreateContext().deploymentID());
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    //resource
    Router resourceRouter = ResourceRouter.createRouter(vertx);
    router.mountSubRouter("/bianque", resourceRouter);
    //metric
    Router metricRouter = MetricRouter.createRouter(vertx);
    router.mountSubRouter("/bianque/metric", metricRouter);

    router.route().handler(BodyHandler.create());
    router.errorHandler(200, this::errorHandler);
    vertx.createHttpServer().requestHandler(router).listen(8000, this::serverCreateSuccessHandler);
  }

  /**
   * success handler
   *
   * @param result AsyncResult
   */
  private void serverCreateSuccessHandler(AsyncResult<HttpServer> result) {
    if (result.succeeded()) {
      log.info("server start successfully at port:" + result.result().actualPort());
    } else {
      log.info("server start failed:" + result.cause().toString());
    }
  }

  /**
   * error handler
   *
   * @param context context
   */
  private void errorHandler(RoutingContext context) {
    //error handler
    log.error("sever execute error:{}", context.getBodyAsString());
    VertxResult.writeFail(context, context.getBodyAsString());
  }
}
