// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Logout.proto

package com.lvl6.pictures.eventprotos;

public final class LogoutEventProto {
  private LogoutEventProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface LogoutRequestProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional .proto.BasicUserProto sender = 1;
    boolean hasSender();
    com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto getSender();
    com.lvl6.pictures.noneventprotos.UserProto.BasicUserProtoOrBuilder getSenderOrBuilder();
  }
  public static final class LogoutRequestProto extends
      com.google.protobuf.GeneratedMessage
      implements LogoutRequestProtoOrBuilder {
    // Use LogoutRequestProto.newBuilder() to construct.
    private LogoutRequestProto(Builder builder) {
      super(builder);
    }
    private LogoutRequestProto(boolean noInit) {}
    
    private static final LogoutRequestProto defaultInstance;
    public static LogoutRequestProto getDefaultInstance() {
      return defaultInstance;
    }
    
    public LogoutRequestProto getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.lvl6.pictures.eventprotos.LogoutEventProto.internal_static_proto_LogoutRequestProto_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.lvl6.pictures.eventprotos.LogoutEventProto.internal_static_proto_LogoutRequestProto_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional .proto.BasicUserProto sender = 1;
    public static final int SENDER_FIELD_NUMBER = 1;
    private com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto sender_;
    public boolean hasSender() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto getSender() {
      return sender_;
    }
    public com.lvl6.pictures.noneventprotos.UserProto.BasicUserProtoOrBuilder getSenderOrBuilder() {
      return sender_;
    }
    
    private void initFields() {
      sender_ = com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, sender_);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, sender_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.lvl6.pictures.eventprotos.LogoutEventProto.internal_static_proto_LogoutRequestProto_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.lvl6.pictures.eventprotos.LogoutEventProto.internal_static_proto_LogoutRequestProto_fieldAccessorTable;
      }
      
      // Construct using com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getSenderFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (senderBuilder_ == null) {
          sender_ = com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.getDefaultInstance();
        } else {
          senderBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto.getDescriptor();
      }
      
      public com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto getDefaultInstanceForType() {
        return com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto.getDefaultInstance();
      }
      
      public com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto build() {
        com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto buildPartial() {
        com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto result = new com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (senderBuilder_ == null) {
          result.sender_ = sender_;
        } else {
          result.sender_ = senderBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto) {
          return mergeFrom((com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto other) {
        if (other == com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto.getDefaultInstance()) return this;
        if (other.hasSender()) {
          mergeSender(other.getSender());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.Builder subBuilder = com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.newBuilder();
              if (hasSender()) {
                subBuilder.mergeFrom(getSender());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setSender(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional .proto.BasicUserProto sender = 1;
      private com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto sender_ = com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto, com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.Builder, com.lvl6.pictures.noneventprotos.UserProto.BasicUserProtoOrBuilder> senderBuilder_;
      public boolean hasSender() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto getSender() {
        if (senderBuilder_ == null) {
          return sender_;
        } else {
          return senderBuilder_.getMessage();
        }
      }
      public Builder setSender(com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto value) {
        if (senderBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          sender_ = value;
          onChanged();
        } else {
          senderBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder setSender(
          com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.Builder builderForValue) {
        if (senderBuilder_ == null) {
          sender_ = builderForValue.build();
          onChanged();
        } else {
          senderBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder mergeSender(com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto value) {
        if (senderBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              sender_ != com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.getDefaultInstance()) {
            sender_ =
              com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.newBuilder(sender_).mergeFrom(value).buildPartial();
          } else {
            sender_ = value;
          }
          onChanged();
        } else {
          senderBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder clearSender() {
        if (senderBuilder_ == null) {
          sender_ = com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.getDefaultInstance();
          onChanged();
        } else {
          senderBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      public com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.Builder getSenderBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getSenderFieldBuilder().getBuilder();
      }
      public com.lvl6.pictures.noneventprotos.UserProto.BasicUserProtoOrBuilder getSenderOrBuilder() {
        if (senderBuilder_ != null) {
          return senderBuilder_.getMessageOrBuilder();
        } else {
          return sender_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto, com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.Builder, com.lvl6.pictures.noneventprotos.UserProto.BasicUserProtoOrBuilder> 
          getSenderFieldBuilder() {
        if (senderBuilder_ == null) {
          senderBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto, com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto.Builder, com.lvl6.pictures.noneventprotos.UserProto.BasicUserProtoOrBuilder>(
                  sender_,
                  getParentForChildren(),
                  isClean());
          sender_ = null;
        }
        return senderBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:proto.LogoutRequestProto)
    }
    
    static {
      defaultInstance = new LogoutRequestProto(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:proto.LogoutRequestProto)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_LogoutRequestProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_proto_LogoutRequestProto_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014Logout.proto\022\005proto\032\nUser.proto\";\n\022Log" +
      "outRequestProto\022%\n\006sender\030\001 \001(\0132\025.proto." +
      "BasicUserProtoB1\n\035com.lvl6.pictures.even" +
      "tprotosB\020LogoutEventProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_proto_LogoutRequestProto_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_proto_LogoutRequestProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_proto_LogoutRequestProto_descriptor,
              new java.lang.String[] { "Sender", },
              com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto.class,
              com.lvl6.pictures.eventprotos.LogoutEventProto.LogoutRequestProto.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.lvl6.pictures.noneventprotos.UserProto.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}