package top.yonyong.bianque.bianque.web.router;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import top.yonyong.bianque.bianque.entity.RequestEntity;
import top.yonyong.bianque.bianque.repository.RequestRepository;
import top.yonyong.bianque.bianque.util.DateUtil;
import top.yonyong.bianque.bianque.util.VertxResult;

import java.util.Date;

/**
 * @author yonyong
 */
@Slf4j
public class ResourceRouter {
    private static Vertx vertx;

    public static Router createRouter(Vertx vertx) {
        ResourceRouter.vertx = vertx;
        Router router = Router.router(vertx);
        router.route("/initTable").handler(ResourceRouter::handleReset);
        router.route("/setup.sh").handler(ResourceRouter::handleAcquireSetup);
        router.route("/lib.tar.gz").handler(ResourceRouter::handleAcquireResource);
        return router;
    }

    private static void handleAcquireSetup(RoutingContext context) {
        String os = System.getenv("os");
        String urlPrefix = null != os && System.getenv("os").toLowerCase().contains("windows") ? "D:\\" : "/";
        VertxResult.writeStream(context, urlPrefix + "opt/application/bianque/resource/setup.sh");
    }

    private static void handleAcquireResource(RoutingContext context) {
        String requestIP = context.request().getHeader("X-REQUEST-IP");
        if (requestIP == null) {
            VertxResult.writeFail(context, "requestIP is null");
            return;
        }
        String resource = context.currentRoute().getPath();
        RequestEntity requestEntity = RequestEntity.builder().ip(requestIP).resource(resource).createTime(DateUtil.format(new Date())).build();
        vertx.eventBus().request(RequestRepository.class.getName() + "insert", JsonObject.mapFrom(requestEntity), reply -> {
            if (reply.succeeded()) {
                String os = System.getenv("os");
                String urlPrefix = null != os && System.getenv("os").toLowerCase().contains("windows") ? "D:\\" : "/";
                VertxResult.writeStream(context, urlPrefix + "opt/application/bianque/resource" + resource);
            } else {
                log.info("No reply:{}", reply.cause().toString());
                VertxResult.writeFail(context, reply.cause().toString());
            }
        });
    }

    private static void handleReset(RoutingContext context) {
        EventBus eb = vertx.eventBus();
        String msg = context.pathParam("msg");
        eb.request(RequestRepository.class.getName() + "reset", msg, reply -> {
            if (reply.succeeded()) {
                VertxResult.writeSuc(context, reply.result().body());
            } else {
                log.info("No reply:{}", reply.cause().toString());
                VertxResult.writeFail(context, reply.cause().toString());
            }
        });
    }
}
