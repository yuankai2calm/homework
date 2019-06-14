package com.master.homework;


import com.master.homework.common.Globals;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class MainLaunch extends Launcher {

  private JsonObject config;

  public static void main(String[] args) {
    new MainLaunch().dispatch(args);

  }

  @Override
  protected String getDefaultCommand() {
    return super.getDefaultCommand();
  }

  @Override
  protected String getMainVerticle() {
    return "com.master.homework.verticle.GateVerticle";
  }

  @Override
  public void afterConfigParsed(JsonObject config) {
    super.afterConfigParsed(config);
    this.config = config;
  }

  @Override
  public void beforeStartingVertx(VertxOptions options) {
    super.beforeStartingVertx(options);
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    super.afterStartingVertx(vertx);
    //初始化配置参数
    Globals.init(vertx, config);

  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    super.beforeDeployingVerticle(deploymentOptions);
  }

  @Override
  public void beforeStoppingVertx(Vertx vertx) {
    super.beforeStoppingVertx(vertx);

  }

  @Override
  public void afterStoppingVertx() {
    super.afterStoppingVertx();
  }

  @Override
  public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
    super.handleDeployFailed(vertx, mainVerticle, deploymentOptions, cause);
  }
}

