// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: policeman.proto

package tech.claudioed.police.man.grpc;

public final class PolicemanProto {
  private PolicemanProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_policies_MessageData_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_policies_MessageData_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_policies_RegistryID_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_policies_RegistryID_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017policeman.proto\022\010policies\"S\n\013MessageDa" +
      "ta\022\016\n\006userId\030\001 \001(\t\022\020\n\010threadId\030\002 \001(\t\022\017\n\007" +
      "content\030\003 \001(\t\022\021\n\tmessageId\030\004 \001(\t\"\030\n\nRegi" +
      "stryID\022\n\n\002id\030\001 \001(\t2J\n\rPoliceOfficer\0229\n\010R" +
      "egistry\022\025.policies.MessageData\032\024.policie" +
      "s.RegistryID\"\000B2\n\036tech.claudioed.police." +
      "man.grpcB\016PolicemanProtoP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_policies_MessageData_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_policies_MessageData_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_policies_MessageData_descriptor,
        new java.lang.String[] { "UserId", "ThreadId", "Content", "MessageId", });
    internal_static_policies_RegistryID_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_policies_RegistryID_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_policies_RegistryID_descriptor,
        new java.lang.String[] { "Id", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
