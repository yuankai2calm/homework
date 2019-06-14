package com.master.homework.student.handler;

import com.master.homework.common.MsgHandlerBase;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

public class StudentMsgHandler extends MsgHandlerBase {
  private static final String ALL = "all";
  private static final String ADD = "add";
  private static final String REMOVE = "remove";
  private static final String UPDATE = "update";

  @Override
  public void registObservers() {
    register(ALL, this::handleAll);
    register(ADD, this::handleAdd);
    register(REMOVE, this::handleRemove);
    register(UPDATE, this::handleUpdate);
  }

  /**
   * 查询所有信息
   * @param response
   * @param params
   */
  private void handleAll(HttpServerResponse response, JsonObject params) {

    System.out.println("======> " + params);
    response.end(" response: " + params);

  }

  /**
   * 增加
   * @param response
   * @param params
   */
  private void handleAdd(HttpServerResponse response, JsonObject params) {



  }

  /**
   * 删除
   * @param response
   * @param params
   */
  private void handleRemove(HttpServerResponse response, JsonObject params) {



  }

  /**
   * 更新
   * @param response
   * @param params
   */
  private void handleUpdate(HttpServerResponse response, JsonObject params) {



  }




}
