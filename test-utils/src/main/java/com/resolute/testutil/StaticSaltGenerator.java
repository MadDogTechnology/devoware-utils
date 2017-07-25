package com.resolute.testutil;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import com.resolute.security.crypto.SaltGenerator;

public class StaticSaltGenerator implements SaltGenerator {
  private final byte [] bytes;
  
  public static Builder newStaticSaltGenerator () {
    return new Builder();
  }
  
  private StaticSaltGenerator (Builder builder) {
    this.bytes = builder.bytes;
  }
  
  @Override
  public byte[] get() {
    return Arrays.copyOf(this.bytes, bytes.length);
  }
  
  public static class Builder {
    private byte [] bytes;
    
    private Builder () {}
    
    public Builder withBytes(byte [] bytes) {
      checkNotNull(bytes);
      checkArgument(bytes.length == 8, "8 bytes are required");
      this.bytes = Arrays.copyOf(bytes, bytes.length);
      return this;
    }
    
    public StaticSaltGenerator build () {
      checkNotNull(bytes);
      return new StaticSaltGenerator(this);
    }
  }
}