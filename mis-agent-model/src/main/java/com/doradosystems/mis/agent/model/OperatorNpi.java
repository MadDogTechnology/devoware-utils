package com.doradosystems.mis.agent.model;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;

public class OperatorNpi {
  private final long operatorId;
  private final long npiId;

  public static Builder builder () {
    return new Builder();
  }
  
  private OperatorNpi (@Nonnull Builder builder) {
    this.operatorId = builder.operatorId;
    this.npiId = builder.npiId;
  }
  
  public long getOperatorId() {
    return operatorId;
  }

  public long getNpiId() {
    return npiId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (npiId ^ (npiId >>> 32));
    result = prime * result + (int) (operatorId ^ (operatorId >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OperatorNpi other = (OperatorNpi) obj;
    if (npiId != other.npiId)
      return false;
    if (operatorId != other.operatorId)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "OperatorNpi [operatorId=" + operatorId + ", npiId=" + npiId + "]";
  }

  public static class Builder {
    private Long operatorId;
    private Long npiId;

    private Builder () {}

    public Builder withOperatorId(long operatorId) {
      checkArgument(operatorId >= 0, "operatorId cannot be negative");
      this.operatorId = operatorId;
      return this;
    }

    public Builder withNpiId(long npiId) {
      checkArgument(npiId >= 0, "npiId cannot be negative");
      this.npiId = npiId;
      return this;
    }
    
    public OperatorNpi build () {
      return new OperatorNpi(this);
    }
  }
  
}
