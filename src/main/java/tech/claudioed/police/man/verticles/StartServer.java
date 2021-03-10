package tech.claudioed.police.man.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.grpc.VertxServerBuilder;
import tech.claudioed.police.man.data.MessageContent;
import tech.claudioed.police.man.grpc.MessageData;
import tech.claudioed.police.man.grpc.RegistryID;
import tech.claudioed.police.man.grpc.VertxPoliceOfficerGrpc;

import java.util.UUID;

public class StartServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    var server = VertxServerBuilder.forPort(7777).addService(new VertxPoliceOfficerGrpc.PoliceOfficerVertxImplBase() {
      @Override
      public Future<RegistryID> registry(MessageData request) {
        var message = new MessageContent(request.getThreadId(), request.getUserId(), request.getMessageId(), request.getContent());
        vertx.eventBus().send("request.apply.policies", message.toJson());
        return Future.succeededFuture(RegistryID.newBuilder().setId(UUID.randomUUID().toString()).build());
      }
    }).build();
    server.start();
  }

}
