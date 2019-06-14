package com.master.homework.common;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MsgHandlerBase {
  private static final Map<Class<?>, MsgHandlerBase>  instances = new ConcurrentHashMap<>();
  private static final Map<String, Handler<HttpServerResponse, JsonObject>> cmdObservers = new ConcurrentHashMap<>();
  private static final Set<String> cmdMsg = new HashSet<>();

  /**
   * 注册事件
   * @param key
   * @param handler
   */
  public void register(String key, Handler<HttpServerResponse, JsonObject> handler) {
    cmdObservers.put(key, handler);
  }

  public abstract void registObservers();

  public static void init(String packageName) {
    Set<Class<?>> classSet = PackageClass.getPackegeClasses(packageName);

    try {
      for (Class<?> clazz : classSet) {
        if(!Utils.isInstanceof(clazz, MsgHandlerBase.class) || Modifier.isAbstract(clazz.getModifiers())) {
          continue;
        }
        MsgHandlerBase inst = (MsgHandlerBase) clazz.newInstance();
        inst.registObservers();
        instances.put(clazz, inst);
      }

    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
  }

  /**
   * 执行
   *
   * @param eventCmd
   * @param response
   * @param data
   */
  public static void fire(String eventCmd, HttpServerResponse response, JsonObject data) {

    Handler<HttpServerResponse, JsonObject> handler = cmdObservers.get(eventCmd);
    try {
      handler.handle(response, data);
    } catch (Exception e) {
     response.end("Unknown error");
    }
  }


}
