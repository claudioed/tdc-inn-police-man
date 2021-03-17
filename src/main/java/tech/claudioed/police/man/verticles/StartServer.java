package tech.claudioed.police.man.verticles;

import grpc.health.v1.HealthGrpc;
import grpc.health.v1.HealthOuterClass;
import io.grpc.ServerInterceptors;
import io.grpc.stub.StreamObserver;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Tracer;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import io.opentracing.util.GlobalTracer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.grpc.VertxServerBuilder;
import me.dinowernli.grpc.prometheus.Configuration;
import me.dinowernli.grpc.prometheus.MonitoringServerInterceptor;
import tech.claudioed.police.man.data.MessageContent;
import tech.claudioed.police.man.grpc.MessageData;
import tech.claudioed.police.man.grpc.RegistryID;
import tech.claudioed.police.man.grpc.VertxPoliceOfficerGrpc;
import tech.claudioed.police.man.service.HealthService;
import tech.claudioed.police.man.service.RegistryService;

import java.util.UUID;

public class StartServer extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Override
  public void start() throws Exception {
    TracingServerInterceptor opentracingInterceptor = getOpentracingInterceptor();
    var metricsInterceptor = MonitoringServerInterceptor.create(Configuration.allMetrics());
    var registryService = new RegistryService(this.vertx);
    var healthService = new HealthService();
    var server = VertxServerBuilder.forPort(7777)
      .addService(ServerInterceptors.intercept(registryService,opentracingInterceptor,metricsInterceptor))
      .addService(healthService).build();
    server.start();
  }

  private static TracingServerInterceptor getOpentracingInterceptor() {
    Tracer tracer = getTracer();
    return TracingServerInterceptor.newBuilder()
      .withStreaming()
      .withVerbosity()
      .withTracer(tracer)
      .build();
  }

  private static Tracer getTracer() {
    Tracer tracer = new JaegerTracer.Builder(StartServer.class.getSimpleName()).build();
    GlobalTracer.registerIfAbsent(tracer);
    return tracer;
  }

}
