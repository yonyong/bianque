package top.yonyong.bianque.bianque.util;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.io.Serializable;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/4/28 17:29
 */
public class VertxResult<T> implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6095433538316185017L;

    /**
     * The Code.
     */
    protected int code = HttpResponseStatus.OK.code();
    /**
     * The Message.
     */
    protected String message = HttpResponseStatus.OK.reasonPhrase();
    /**
     * The Data.
     */
    protected T data;

    /**
     * write stream.
     */
    public static void writeStream(RoutingContext context, String filePath) {
        context.response().sendFile(filePath);
    }

    /**
     * Of success rest result.
     *
     * @param <T> the type parameter
     */
    public static <T> void writeSuc(RoutingContext context) {
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        context.response().end(Json.encode(new VertxResult<T>()));
    }

    /**
     * Of success rest result.
     *
     * @param <T>  the type parameter
     * @param data the data
     */
    public static <T> void writeSuc(RoutingContext context, T data) {
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        context.response().end(Json.encode(new VertxResult<T>(data)));
    }

    /**
     * Of fail rest result.
     *
     * @param <T> the type parameter
     */
    public static <T> void writeFail(RoutingContext context) {
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        context.response().end(Json.encode(new VertxResult<T>(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase())));
    }

    /**
     * Of fail rest result.
     *
     * @param <T> the type parameter
     */
    public static <T> void writeFail(RoutingContext context, String msg) {
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        context.response().end(Json.encode(new VertxResult<T>(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), msg)));
    }

    /**
     * Of fail rest result.
     *
     * @param <T>  the type parameter
     * @param code the code
     * @param msg  the msg
     */
    public static <T> void writeFail(RoutingContext context, int code, String msg) {
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        context.response().end(Json.encode(new VertxResult<T>(code, msg)));
    }


    /**
     * Instantiates a new Rest result.
     */
    public VertxResult() {
    }

    /**
     * Instantiates a new Rest result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public VertxResult(int code, String message, T data) {
        this.code = code;
        this.setMessage(message);
        this.data = data;
    }

    /**
     * Instantiates a new Rest result.
     *
     * @param code the code
     * @param data the data
     */
    public VertxResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * Instantiates a new Rest result.
     *
     * @param data the data
     */
    public VertxResult(T data) {
        this.data = data;
    }

    /**
     * Instantiates a new Rest result.
     *
     * @param code    the code
     * @param message the message
     */
    public VertxResult(int code, String message) {
        this.code = code;
        this.setMessage(message);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * check code.
     *
     * @return check code result
     */
    public boolean isSuccess() {
        return code == HttpResponseStatus.OK.code();
    }
}
