package com.master.homework.verticle;

import com.master.homework.common.MsgHandlerBase;
import com.master.homework.logger.Loggers;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * MainVerticle
 */
public class GateVerticle extends AbstractVerticle {

  private static final Logger logger = Loggers.gateLogger;
  private HttpServer netServer;
  private static final String DEFAULT_HOST = "192.168.1.7";
  private static final int DEFAULT_PORT = 80;

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);
    netServer.close(result -> {
      if(result.succeeded()) {
        logger.info("http server stop complete");
        stopFuture.complete();
      } else {
        stopFuture.fail(result.cause());
      }
    });

  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    super.start();
    String host = config().getString("http.host", DEFAULT_HOST);
    int port = config().getInteger("http.port", DEFAULT_PORT);

    HttpServerOptions httpServerOptions = new HttpServerOptions().setIdleTimeout(120);
    Boolean ssl = config().getBoolean("ssl");
    if (ssl != null && ssl) {
      String path = config().getString("ssl.path");
      String password = config().getString("ssl.password");
      httpServerOptions.setSsl(true);
      httpServerOptions.setKeyStoreOptions(new JksOptions().setPath(path).setPassword(password));
    }
    netServer = vertx.createHttpServer(httpServerOptions);
    Router router = Router.router(vertx).exceptionHandler(ex -> logger.error("error"+ ex.getMessage(), ex));
    Set<HttpMethod> allowMethods = new HashSet<>();
    //allowMethods.add(HttpMethod.GET);
    allowMethods.add(HttpMethod.POST);
    Set<String> allowHeaders = new HashSet<>();
    allowHeaders.add("Access-Control-Allow-Origin");
    allowHeaders.add("origin");
    allowHeaders.add("Content-Type");
    allowHeaders.add("accept");

    router.route().handler(BodyHandler.create());
    router.route().handler(CorsHandler.create("*")
      .allowedHeaders(allowHeaders)
      .allowedMethods(allowMethods));

    router.post("/req").handler(this::handleRequest);
    router.get("/get").handler(this::handleGet);
    netServer.requestHandler(router).listen(port, host, http -> {
      if (http.succeeded()) {
        startFuture.complete();
        System.out.println("HTTP server started on port " + port + " host " + host);
      } else {
        startFuture.fail(http.cause());
      }
    });
  }

  /**
   * 处理请求
   * @param context
   */
  private void handleRequest(RoutingContext context) {
    try {
      JsonObject data = context.getBodyAsJson();
      if(data == null) {
        logger.error(" Body data is null");
        errUnknown(context);
        return;
      }

      String handler = data.getString("handle");
      JsonObject dataJson = data.getJsonObject("data");
      if(dataJson == null) {
        dataJson = new JsonObject();
      }
      inputHandler(handler, dataJson, context.response());
    } catch (Exception e) {
      errUnknown(context);
      logger.info(" parse request params exception");
    }

  }

  private void handleGet(RoutingContext context) {
  }

  /**
   * 处理请求消息
   * @param context
   */


  /**
   * 请求转发
   */
  private void inputHandler(String eventType, JsonObject data, HttpServerResponse response) {
    MsgHandlerBase.fire(eventType, response, data);

  }

  /**
   * 未知错误
   */
  private void errUnknown(RoutingContext context) {
    JsonObject errMsg = new JsonObject();
    errMsg.put("event", "error_code");
    errMsg.put("error_code", 9000);
  }


}
