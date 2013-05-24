// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PicturesEventProtocol.proto

package com.lvl6.pictures.noneventprotos;

public final class PicturesEventProtocolProto {
  private PicturesEventProtocolProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum PicturesEventProtocolRequest
      implements com.google.protobuf.ProtocolMessageEnum {
    C_REFILL_TOKENS_BY_WAITING_EVENT(0, 1000),
    C_COMPLETED_ROUND_EVENT(1, 1001),
    C_RETRIEVE_NEW_QUESTIONS_EVENT(2, 1002),
    C_START_ROUND_EVENT(3, 1003),
    C_SPEND_RUBIES_EVENT(4, 1004),
    C_SEARCH_FOR_USER_EVENT(5, 1005),
    ;
    
    public static final int C_REFILL_TOKENS_BY_WAITING_EVENT_VALUE = 1000;
    public static final int C_COMPLETED_ROUND_EVENT_VALUE = 1001;
    public static final int C_RETRIEVE_NEW_QUESTIONS_EVENT_VALUE = 1002;
    public static final int C_START_ROUND_EVENT_VALUE = 1003;
    public static final int C_SPEND_RUBIES_EVENT_VALUE = 1004;
    public static final int C_SEARCH_FOR_USER_EVENT_VALUE = 1005;
    
    
    public final int getNumber() { return value; }
    
    public static PicturesEventProtocolRequest valueOf(int value) {
      switch (value) {
        case 1000: return C_REFILL_TOKENS_BY_WAITING_EVENT;
        case 1001: return C_COMPLETED_ROUND_EVENT;
        case 1002: return C_RETRIEVE_NEW_QUESTIONS_EVENT;
        case 1003: return C_START_ROUND_EVENT;
        case 1004: return C_SPEND_RUBIES_EVENT;
        case 1005: return C_SEARCH_FOR_USER_EVENT;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<PicturesEventProtocolRequest>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<PicturesEventProtocolRequest>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<PicturesEventProtocolRequest>() {
            public PicturesEventProtocolRequest findValueByNumber(int number) {
              return PicturesEventProtocolRequest.valueOf(number);
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
      return com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final PicturesEventProtocolRequest[] VALUES = {
      C_REFILL_TOKENS_BY_WAITING_EVENT, C_COMPLETED_ROUND_EVENT, C_RETRIEVE_NEW_QUESTIONS_EVENT, C_START_ROUND_EVENT, C_SPEND_RUBIES_EVENT, C_SEARCH_FOR_USER_EVENT, 
    };
    
    public static PicturesEventProtocolRequest valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private PicturesEventProtocolRequest(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.PicturesEventProtocolRequest)
  }
  
  public enum PicturesEventProtocolResponse
      implements com.google.protobuf.ProtocolMessageEnum {
    S_REFILL_TOKENS_BY_WAITING_EVENT(0, 1000),
    S_COMPLETED_ROUND_EVENT(1, 1001),
    S_RETRIEVE_NEW_QUESTIONS_EVENT(2, 1002),
    S_START_ROUND_EVENT(3, 1003),
    S_SPEND_RUBIES_EVENT(4, 1004),
    S_SEARCH_FOR_USER_EVENT(5, 1005),
    ;
    
    public static final int S_REFILL_TOKENS_BY_WAITING_EVENT_VALUE = 1000;
    public static final int S_COMPLETED_ROUND_EVENT_VALUE = 1001;
    public static final int S_RETRIEVE_NEW_QUESTIONS_EVENT_VALUE = 1002;
    public static final int S_START_ROUND_EVENT_VALUE = 1003;
    public static final int S_SPEND_RUBIES_EVENT_VALUE = 1004;
    public static final int S_SEARCH_FOR_USER_EVENT_VALUE = 1005;
    
    
    public final int getNumber() { return value; }
    
    public static PicturesEventProtocolResponse valueOf(int value) {
      switch (value) {
        case 1000: return S_REFILL_TOKENS_BY_WAITING_EVENT;
        case 1001: return S_COMPLETED_ROUND_EVENT;
        case 1002: return S_RETRIEVE_NEW_QUESTIONS_EVENT;
        case 1003: return S_START_ROUND_EVENT;
        case 1004: return S_SPEND_RUBIES_EVENT;
        case 1005: return S_SEARCH_FOR_USER_EVENT;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<PicturesEventProtocolResponse>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<PicturesEventProtocolResponse>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<PicturesEventProtocolResponse>() {
            public PicturesEventProtocolResponse findValueByNumber(int number) {
              return PicturesEventProtocolResponse.valueOf(number);
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
      return com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.getDescriptor().getEnumTypes().get(1);
    }
    
    private static final PicturesEventProtocolResponse[] VALUES = {
      S_REFILL_TOKENS_BY_WAITING_EVENT, S_COMPLETED_ROUND_EVENT, S_RETRIEVE_NEW_QUESTIONS_EVENT, S_START_ROUND_EVENT, S_SPEND_RUBIES_EVENT, S_SEARCH_FOR_USER_EVENT, 
    };
    
    public static PicturesEventProtocolResponse valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private PicturesEventProtocolResponse(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.PicturesEventProtocolResponse)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\033PicturesEventProtocol.proto\022\005proto*\333\001\n" +
      "\034PicturesEventProtocolRequest\022%\n C_REFIL" +
      "L_TOKENS_BY_WAITING_EVENT\020\350\007\022\034\n\027C_COMPLE" +
      "TED_ROUND_EVENT\020\351\007\022#\n\036C_RETRIEVE_NEW_QUE" +
      "STIONS_EVENT\020\352\007\022\030\n\023C_START_ROUND_EVENT\020\353" +
      "\007\022\031\n\024C_SPEND_RUBIES_EVENT\020\354\007\022\034\n\027C_SEARCH" +
      "_FOR_USER_EVENT\020\355\007*\334\001\n\035PicturesEventProt" +
      "ocolResponse\022%\n S_REFILL_TOKENS_BY_WAITI" +
      "NG_EVENT\020\350\007\022\034\n\027S_COMPLETED_ROUND_EVENT\020\351" +
      "\007\022#\n\036S_RETRIEVE_NEW_QUESTIONS_EVENT\020\352\007\022\030",
      "\n\023S_START_ROUND_EVENT\020\353\007\022\031\n\024S_SPEND_RUBI" +
      "ES_EVENT\020\354\007\022\034\n\027S_SEARCH_FOR_USER_EVENT\020\355" +
      "\007B>\n com.lvl6.pictures.noneventprotosB\032P" +
      "icturesEventProtocolProto"
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
