package grpc.health.v1;

import static grpc.health.v1.HealthGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;


@javax.annotation.Generated(
value = "by VertxGrpc generator",
comments = "Source: health.proto")
public final class VertxHealthGrpc {
    private VertxHealthGrpc() {}

    public static HealthVertxStub newVertxStub(io.grpc.Channel channel) {
        return new HealthVertxStub(channel);
    }

    
    public static final class HealthVertxStub extends io.grpc.stub.AbstractStub<HealthVertxStub> {
        private final io.vertx.core.impl.ContextInternal ctx;
        private HealthGrpc.HealthStub delegateStub;

        private HealthVertxStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = HealthGrpc.newStub(channel);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        private HealthVertxStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = HealthGrpc.newStub(channel).build(channel, callOptions);
            this.ctx = (io.vertx.core.impl.ContextInternal) io.vertx.core.Vertx.currentContext();
        }

        @Override
        protected HealthVertxStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HealthVertxStub(channel, callOptions);
        }

        
        public io.vertx.core.Future<grpc.health.v1.HealthOuterClass.HealthCheckResponse> check(grpc.health.v1.HealthOuterClass.HealthCheckRequest request) {
            return io.vertx.grpc.stub.ClientCalls.oneToOne(ctx, request, delegateStub::check);
        }

        
        public io.vertx.core.streams.ReadStream<grpc.health.v1.HealthOuterClass.HealthCheckResponse> watch(grpc.health.v1.HealthOuterClass.HealthCheckRequest request) {
            return io.vertx.grpc.stub.ClientCalls.oneToMany(ctx, request, delegateStub::watch);
        }

    }

    
    public static abstract class HealthVertxImplBase implements io.grpc.BindableService {
        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public HealthVertxImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        
        public io.vertx.core.Future<grpc.health.v1.HealthOuterClass.HealthCheckResponse> check(grpc.health.v1.HealthOuterClass.HealthCheckRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        
        public void watch(grpc.health.v1.HealthOuterClass.HealthCheckRequest request, io.vertx.core.streams.WriteStream<grpc.health.v1.HealthOuterClass.HealthCheckResponse> response) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            grpc.health.v1.HealthGrpc.getCheckMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            grpc.health.v1.HealthOuterClass.HealthCheckRequest,
                                            grpc.health.v1.HealthOuterClass.HealthCheckResponse>(
                                            this, METHODID_CHECK, compression)))
                    .addMethod(
                            grpc.health.v1.HealthGrpc.getWatchMethod(),
                            asyncServerStreamingCall(
                                    new MethodHandlers<
                                            grpc.health.v1.HealthOuterClass.HealthCheckRequest,
                                            grpc.health.v1.HealthOuterClass.HealthCheckResponse>(
                                            this, METHODID_WATCH, compression)))
                    .build();
        }
    }

    private static final int METHODID_CHECK = 0;
    private static final int METHODID_WATCH = 1;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final HealthVertxImplBase serviceImpl;
        private final int methodId;
        private final String compression;

        MethodHandlers(HealthVertxImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_CHECK:
                    io.vertx.grpc.stub.ServerCalls.oneToOne(
                            (grpc.health.v1.HealthOuterClass.HealthCheckRequest) request,
                            (io.grpc.stub.StreamObserver<grpc.health.v1.HealthOuterClass.HealthCheckResponse>) responseObserver,
                            compression,
                            serviceImpl::check);
                    break;
                case METHODID_WATCH:
                    io.vertx.grpc.stub.ServerCalls.oneToMany(
                            (grpc.health.v1.HealthOuterClass.HealthCheckRequest) request,
                            (io.grpc.stub.StreamObserver<grpc.health.v1.HealthOuterClass.HealthCheckResponse>) responseObserver,
                            compression,
                            serviceImpl::watch);
                    break;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
