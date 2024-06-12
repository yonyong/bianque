package top.yonyong.bianque.bianque.repository;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import lombok.extern.slf4j.Slf4j;
import top.yonyong.bianque.bianque.dto.BaseVO;
import top.yonyong.bianque.bianque.entity.RequestEntity;
import top.yonyong.bianque.bianque.util.JsonUtil;
import top.yonyong.bianque.boot.lib.StopWatch;
import top.yonyong.bianque.boot.support.annotation.Verticle;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/4/28 17:51
 */
@Slf4j
@Verticle(isWork = true, workPoolName = "x-worker-h2", workPoolSize = 1)
public class RequestRepository extends BaseRepository {

    private static final String H2_POOL = "db-pool-h2";

    @Override
    protected void configureJdbcClient() {
        String os = System.getenv("os");
        String urlPrefix = null != os && System.getenv("os").toLowerCase().contains("windows") ? "jdbc:h2:D:\\" : "jdbc:h2:/";
        JsonObject config = new JsonObject()
                .put("url", urlPrefix + "opt/application/bianque/bianque")
                .put("driver_class", "org.h2.Driver")
                .put("name", H2_POOL)
                .put("max_pool_size", 3);
        this.client = JDBCClient.createShared(vertx, config, H2_POOL);
    }

    @Override
    protected void configureConsumer() {
        EventBus eb = vertx.eventBus();
        eb.consumer(this.getClass().getName() + "reset", this::resetTable);
        eb.consumer(this.getClass().getName() + "insert", this::insertUser);
    }

    @SuppressWarnings("all")
    private void insertUser(Message<JsonObject> tMessage) {
        RequestEntity request = JsonUtil.toBean(tMessage.body(), RequestEntity.class);
        StopWatch stopWatch = new StopWatch(1, true);
        JsonArray jsonArray = new JsonArray().add(request.getIp()).add(request.getResource()).add(request.getCreateTime());
        client.updateWithParams("INSERT INTO t_request_record (ip, resource,createTime) VALUES (?,?,?)", jsonArray, res -> {
            if (stopWatch.stop()) {
                if (res.succeeded()) {
                    BaseVO<Object> vo = BaseVO.builder().cost(stopWatch.getElapsedTime()).data(res.result().getUpdated()).build();
                    tMessage.reply(Json.encode(vo));
                } else {
                    log.error("insert fail:{}", res.cause().toString());
                    tMessage.reply("fail");
                }
            }
        });
    }

    /**
     * create h2 table
     *
     * @param tMessage tMessage
     * @param <T>      T
     */
    @SuppressWarnings("all")
    private <T> void resetTable(Message<T> tMessage) {
        client.getConnection(con -> {
            if (con.succeeded()) {
                SQLConnection connection = con.result();
                connection.execute("DROP TABLE IF EXISTS t_request_record", res -> {
                    if (res.succeeded()) {
                        log.info("drop table t_request_record suc");
                        connection.execute("create table t_request_record(ip VARCHAR(32)," +
                                "resource VARCHAR(64),createTime VARCHAR(32))", res1 -> {
                            if (res1.succeeded()) {
                                log.info("create table t_request_record suc");
                            } else {
                                log.error("create table t_request_record fail:{}", res1.cause().toString());
                            }
                        });
                    } else {
                        log.error("drop table t_request_record fail:{}", res.cause().toString());
                    }
                    connection.close();
                });
            }
        });
        tMessage.reply("done");
    }
}
