package top.yonyong.bianque.bianque.repository;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.jdbc.JDBCClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/4/28 17:51
 */
@Slf4j
public abstract class BaseRepository extends AbstractVerticle {

    protected JDBCClient client;

    @Override
    public void start() throws Exception {
        MDC.put("instanceId", vertx.getOrCreateContext().deploymentID());
        this.configureJdbcClient();
        this.configureConsumer();
    }

    /**
     * configure jdbc client
     */
    protected abstract void configureJdbcClient();

    /**
     * configure jdbc consumer
     */
    protected abstract void configureConsumer();
}
