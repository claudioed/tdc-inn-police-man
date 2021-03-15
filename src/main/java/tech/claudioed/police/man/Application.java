package tech.claudioed.police.man;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import tech.claudioed.police.man.verticles.ApplyPolicies;
import tech.claudioed.police.man.verticles.BlockUser;
import tech.claudioed.police.man.verticles.StartServer;
import tech.claudioed.police.man.verticles.RegistryPolicyViolation;

public class Application {

  public static void main(String[]args){
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new MicrometerMetricsOptions()
        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
        .setEnabled(true)));
    vertx.deployVerticle(new StartServer());
    vertx.deployVerticle(new ApplyPolicies());
    vertx.deployVerticle(new BlockUser());
    vertx.deployVerticle(new RegistryPolicyViolation());
  }

}
