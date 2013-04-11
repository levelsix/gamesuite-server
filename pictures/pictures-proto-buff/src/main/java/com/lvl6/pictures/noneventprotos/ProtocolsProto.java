// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Protocols.proto

package com.lvl6.pictures.noneventprotos;

public final class ProtocolsProto {
  private ProtocolsProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum EventProtocolRequest
      implements com.google.protobuf.ProtocolMessageEnum {
    C_CREATE_ACCOUNT_EVENT(0, 0),
    C_LOGIN_EVENT(1, 1),
    ;
    
    public static final int C_CREATE_ACCOUNT_EVENT_VALUE = 0;
    public static final int C_LOGIN_EVENT_VALUE = 1;
    
    
    public final int getNumber() { return value; }
    
    public static EventProtocolRequest valueOf(int value) {
      switch (value) {
        case 0: return C_CREATE_ACCOUNT_EVENT;
        case 1: return C_LOGIN_EVENT;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<EventProtocolRequest>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<EventProtocolRequest>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<EventProtocolRequest>() {
            public EventProtocolRequest findValueByNumber(int number) {
              return EventProtocolRequest.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.lvl6.pictures.noneventprotos.ProtocolsProto.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final EventProtocolRequest[] VALUES = {
      C_CREATE_ACCOUNT_EVENT, C_LOGIN_EVENT, 
    };
    
    public static EventProtocolRequest valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private EventProtocolRequest(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.EventProtocolRequest)
  }
  
  public enum EventProtocolResponse
      implements com.google.protobuf.ProtocolMessageEnum {
    S_CREATE_ACCOUNT_EVENT(0, 0),
    S_LOGIN_EVENT(1, 1),
    ;
    
    public static final int S_CREATE_ACCOUNT_EVENT_VALUE = 0;
    public static final int S_LOGIN_EVENT_VALUE = 1;
    
    
    public final int getNumber() { return value; }
    
    public static EventProtocolResponse valueOf(int value) {
      switch (value) {
        case 0: return S_CREATE_ACCOUNT_EVENT;
        case 1: return S_LOGIN_EVENT;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<EventProtocolResponse>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<EventProtocolResponse>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<EventProtocolResponse>() {
            public EventProtocolResponse findValueByNumber(int number) {
              return EventProtocolResponse.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.lvl6.pictures.noneventprotos.ProtocolsProto.getDescriptor().getEnumTypes().get(1);
    }
    
    private static final EventProtocolResponse[] VALUES = {
      S_CREATE_ACCOUNT_EVENT, S_LOGIN_EVENT, 
    };
    
    public static EventProtocolResponse valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private EventProtocolResponse(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.EventProtocolResponse)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017Protocols.proto\022\005proto*E\n\024EventProtoco" +
      "lRequest\022\032\n\026C_CREATE_ACCOUNT_EVENT\020\000\022\021\n\r" +
      "C_LOGIN_EVENT\020\001*F\n\025EventProtocolResponse" +
      "\022\032\n\026S_CREATE_ACCOUNT_EVENT\020\000\022\021\n\rS_LOGIN_" +
      "EVENT\020\001B2\n com.lvl6.pictures.noneventpro" +
      "tosB\016ProtocolsProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
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
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}