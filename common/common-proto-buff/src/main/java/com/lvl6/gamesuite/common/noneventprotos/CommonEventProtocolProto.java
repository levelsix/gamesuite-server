// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CommonEventProtocol.proto

package com.lvl6.gamesuite.common.noneventprotos;

public final class CommonEventProtocolProto {
  private CommonEventProtocolProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum CommonEventProtocolRequest
      implements com.google.protobuf.ProtocolMessageEnum {
    C_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT(0, 1),
    C_CREATE_ACCOUNT_VIA_EMAIL_EVENT(1, 2),
    C_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT(2, 3),
    C_LOGIN_EVENT(3, 4),
    C_IN_APP_PURCHASE_EVENT(4, 5),
    C_LOGOUT_EVENT(5, 51),
    ;
    
    public static final int C_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT_VALUE = 1;
    public static final int C_CREATE_ACCOUNT_VIA_EMAIL_EVENT_VALUE = 2;
    public static final int C_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT_VALUE = 3;
    public static final int C_LOGIN_EVENT_VALUE = 4;
    public static final int C_IN_APP_PURCHASE_EVENT_VALUE = 5;
    public static final int C_LOGOUT_EVENT_VALUE = 51;
    
    
    public final int getNumber() { return value; }
    
    public static CommonEventProtocolRequest valueOf(int value) {
      switch (value) {
        case 1: return C_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT;
        case 2: return C_CREATE_ACCOUNT_VIA_EMAIL_EVENT;
        case 3: return C_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT;
        case 4: return C_LOGIN_EVENT;
        case 5: return C_IN_APP_PURCHASE_EVENT;
        case 51: return C_LOGOUT_EVENT;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<CommonEventProtocolRequest>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<CommonEventProtocolRequest>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<CommonEventProtocolRequest>() {
            public CommonEventProtocolRequest findValueByNumber(int number) {
              return CommonEventProtocolRequest.valueOf(number);
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
      return com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final CommonEventProtocolRequest[] VALUES = {
      C_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT, C_CREATE_ACCOUNT_VIA_EMAIL_EVENT, C_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT, C_LOGIN_EVENT, C_IN_APP_PURCHASE_EVENT, C_LOGOUT_EVENT, 
    };
    
    public static CommonEventProtocolRequest valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private CommonEventProtocolRequest(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.CommonEventProtocolRequest)
  }
  
  public enum CommonEventProtocolResponse
      implements com.google.protobuf.ProtocolMessageEnum {
    S_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT(0, 1),
    S_CREATE_ACCOUNT_VIA_EMAIL_EVENT(1, 2),
    S_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT(2, 3),
    S_LOGIN_EVENT(3, 4),
    S_IN_APP_PURCHASE_EVENT(4, 5),
    S_FORCE_LOGOUT_EVENT(5, 50),
    ;
    
    public static final int S_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT_VALUE = 1;
    public static final int S_CREATE_ACCOUNT_VIA_EMAIL_EVENT_VALUE = 2;
    public static final int S_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT_VALUE = 3;
    public static final int S_LOGIN_EVENT_VALUE = 4;
    public static final int S_IN_APP_PURCHASE_EVENT_VALUE = 5;
    public static final int S_FORCE_LOGOUT_EVENT_VALUE = 50;
    
    
    public final int getNumber() { return value; }
    
    public static CommonEventProtocolResponse valueOf(int value) {
      switch (value) {
        case 1: return S_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT;
        case 2: return S_CREATE_ACCOUNT_VIA_EMAIL_EVENT;
        case 3: return S_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT;
        case 4: return S_LOGIN_EVENT;
        case 5: return S_IN_APP_PURCHASE_EVENT;
        case 50: return S_FORCE_LOGOUT_EVENT;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<CommonEventProtocolResponse>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<CommonEventProtocolResponse>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<CommonEventProtocolResponse>() {
            public CommonEventProtocolResponse findValueByNumber(int number) {
              return CommonEventProtocolResponse.valueOf(number);
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
      return com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.getDescriptor().getEnumTypes().get(1);
    }
    
    private static final CommonEventProtocolResponse[] VALUES = {
      S_CREATE_ACCOUNT_VIA_FACEBOOK_EVENT, S_CREATE_ACCOUNT_VIA_EMAIL_EVENT, S_CREATE_ACCOUNT_VIA_NO_CREDENTIALS_EVENT, S_LOGIN_EVENT, S_IN_APP_PURCHASE_EVENT, S_FORCE_LOGOUT_EVENT, 
    };
    
    public static CommonEventProtocolResponse valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private CommonEventProtocolResponse(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.CommonEventProtocolResponse)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\031CommonEventProtocol.proto\022\005proto*\336\001\n\032C" +
      "ommonEventProtocolRequest\022\'\n#C_CREATE_AC" +
      "COUNT_VIA_FACEBOOK_EVENT\020\001\022$\n C_CREATE_A" +
      "CCOUNT_VIA_EMAIL_EVENT\020\002\022-\n)C_CREATE_ACC" +
      "OUNT_VIA_NO_CREDENTIALS_EVENT\020\003\022\021\n\rC_LOG" +
      "IN_EVENT\020\004\022\033\n\027C_IN_APP_PURCHASE_EVENT\020\005\022" +
      "\022\n\016C_LOGOUT_EVENT\0203*\345\001\n\033CommonEventProto" +
      "colResponse\022\'\n#S_CREATE_ACCOUNT_VIA_FACE" +
      "BOOK_EVENT\020\001\022$\n S_CREATE_ACCOUNT_VIA_EMA" +
      "IL_EVENT\020\002\022-\n)S_CREATE_ACCOUNT_VIA_NO_CR",
      "EDENTIALS_EVENT\020\003\022\021\n\rS_LOGIN_EVENT\020\004\022\033\n\027" +
      "S_IN_APP_PURCHASE_EVENT\020\005\022\030\n\024S_FORCE_LOG" +
      "OUT_EVENT\0202BD\n(com.lvl6.gamesuite.common" +
      ".noneventprotosB\030CommonEventProtocolProt" +
      "o"
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
