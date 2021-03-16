package tech.claudioed.police.man.service;

import grpc.health.v1.HealthGrpc;
import grpc.health.v1.HealthOuterClass;
import io.grpc.stub.StreamObserver;

public class HealthService extends HealthGrpc.HealthImplBase {

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
