package top.yonyong.bianque.bianque;

import io.vertx.core.logging.SLF4JLogDelegateFactory;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import top.yonyong.bianque.boot.VertxApplication;
import top.yonyong.bianque.boot.VertxBootApplication;

import java.sql.SQLException;

@Slf4j
@VertxBootApplication
public class BianqueApplication {
    public static void main(String[] args) {
        //config log
        System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());
        //start application
        VertxApplication.run(BianqueApplication.class);

        //start h2 web console
        startH2WebConsole();
    }

    public static void startH2WebConsole() {
        try {
            Server.createWebServer("-webPort", "8888", "-webAllowOthers").start();
            log.info("H2 Web Console started at http://localhost:8888/ (use '-webAllowOthers' to allow connections from other computers)");
        } catch (SQLException e) {
            log.error("H2 Web Console start failed", e);
        }
    }
}
