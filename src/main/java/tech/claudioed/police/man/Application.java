package tech.claudioed.police.man;

import io.vertx.core.Vertx;
import tech.claudioed.police.man.verticles.ApplyPolicies;
import tech.claudioed.police.man.verticles.BlockUser;
import tech.claudioed.police.man.verticles.StartServer;
import tech.claudioed.police.man.verticles.RegistryPolicyViolation;

public class Application {

  public static void main(String[]args){
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new StartServer());
    vertx.deployVerticle(new ApplyPolicies());
    vertx.deployVerticle(new BlockUser());
    vertx.deployVerticle(new RegistryPolicyViolation());
  }

}
