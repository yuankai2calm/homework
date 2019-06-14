package com.master.homework.common;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Globals {

  private static Vertx vertx;

  private static JsonObject config;

  public static Vertx getVertx() {
    return vertx;
  }

  public static JsonObject getConfig() {
    return config;
  }

  public static void init(Vertx tmpVertx, JsonObject tmpConfig) {

     vertx = tmpVertx;
     config = tmpConfig;
     MsgHandlerBase.init(config.getString("HandlerPackage", "com.master.homework"));
  }
}
