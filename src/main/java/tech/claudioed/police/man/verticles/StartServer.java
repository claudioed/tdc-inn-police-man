package tech.claudioed.police.man.verticles;

import grpc.health.v1.HealthGrpc;
import grpc.health.v1.HealthOuterClass;
import io.grpc.stub.StreamObserver;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.grpc.VertxServerBuilder;
import tech.claudioed.police.man.data.MessageContent;
import tech.claudioed.police.man.grpc.MessageData;
import tech.claudioed.police.man.grpc.RegistryID;
import tech.claudioed.police.man.grpc.VertxPoliceOfficerGrpc;

import java.util.UUID;

public class StartServer extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Override
  public void start() throws Exception {
    var server = VertxServerBuilder.forPort(7777).addService(new VertxPoliceOfficerGrpc.PoliceOfficerVertxImplBase() {
      @Override
      public Future<RegistryID> registry(MessageData request) {
        var message = new MessageContent(request.getThreadId(), request.getUserId(), request.getMessageId(), request.getContent());
        LOG.info("Receiving messages to check violations...");
        vertx.eventBus().send("request.apply.policies", message.toJson());
        LOG.info("Policy was received and will checked in a second!!!");
        return Future.succeededFuture(RegistryID.newBuilder().setId(UUID.randomUUID().toString()).build());
      }
    }).addService(
      new HealthGrpc.HealthImplBase() {
        @Override
        public void check(HealthOuterClass.HealthCheckRequest request, StreamObserver<HealthOuterClass.HealthCheckResponse> responseObserver) {
          responseObserver.onNext(HealthOuterClass.HealthCheckResponse.newBuilder().setStatus(HealthOuterClass.HealthCheckResponse.ServingStatus.SERVING).build());
          responseObserver.onCompleted();
        }
        @Override
        public void watch(HealthOuterClass.HealthCheckRequest request, StreamObserver<HealthOuterClass.HealthCheckResponse> responseObserver) {
          responseObserver.onNext(HealthOuterClass.HealthCheckResponse.newBuilder().setStatus(HealthOuterClass.HealthCheckResponse.ServingStatus.SERVING).build());
          responseObserver.onCompleted();
        }
      }
    ).build();
    server.start();
  }

}
