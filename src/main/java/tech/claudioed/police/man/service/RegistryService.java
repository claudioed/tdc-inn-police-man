package tech.claudioed.police.man.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import tech.claudioed.police.man.data.MessageContent;
import tech.claudioed.police.man.grpc.MessageData;
import tech.claudioed.police.man.grpc.RegistryID;
import tech.claudioed.police.man.grpc.VertxPoliceOfficerGrpc;

import java.util.UUID;

public class RegistryService extends VertxPoliceOfficerGrpc.PoliceOfficerVertxImplBase {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  private final Vertx vertx;

  public RegistryService(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public Future<RegistryID> registry(MessageData request) {
    var message = new MessageContent(request.getThreadId(), request.getUserId(), request.getMessageId(), request.getContent());
    LOG.info("Receiving messages to check violations...");
    vertx.eventBus().send("request.apply.policies", message.toJson());
    LOG.info("Policy was received and will checked in a second!!!");
    return Future.succeededFuture(RegistryID.newBuilder().setId(UUID.randomUUID().toString()).build());
  }

}
