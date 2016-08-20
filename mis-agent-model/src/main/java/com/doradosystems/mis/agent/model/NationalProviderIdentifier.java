package com.doradosystems.mis.agent.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = NationalProviderIdentifier.Builder.class)
public class NationalProviderIdentifier {
  private final long id;
  private final long idNumber;
  private final String regionCode;
  
  @JsonCreator
  @Nonnull
  public static Builder builder() {
    return new Builder();
  }

  private NationalProviderIdentifier(@Nonnull Builder builder) {
    this.id = builder.id;
    this.idNumber = builder.idNumber;
    this.regionCode = builder.regionCode;
  }
  
  public long getId() {
    return id;
  }
  
  public long getIdNumber() {
    return idNumber;
  }

  @Nonnull
  public String getRegionCode() {
    return regionCode;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + (int) (idNumber ^ (idNumber >>> 32));
    result = prime * result + ((regionCode == null) ? 0 : regionCode.hashCode());
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
    NationalProviderIdentifier other = (NationalProviderIdentifier) obj;
    if (id != other.id)
      return false;
    if (idNumber != other.idNumber)
      return false;
    if (regionCode == null) {
      if (other.regionCode != null)
        return false;
    } else if (!regionCode.equals(other.regionCode))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "NationalProviderIdentifier [id=" + id + ", idNumber=" + idNumber + ", regionCode="
        + regionCode + "]";
  }

  @JsonPOJOBuilder
  public static class Builder {
    private Long id;
    private Long idNumber;
    private String regionCode;
    
    private Builder() {}

    public Builder withId(long id) {
      checkArgument(id >= 0, "id must be a non-negative number"); 
      this.id = id;
      return this;
    }

    public Builder withIdNumber(long idNumber) {
      checkArgument(idNumber >= 0, "idNumber must be a non-negative number"); 
      this.idNumber = idNumber;
      return this;
    }

    public Builder withRegionCode(@Nonnull String regionCode) {
      requireNonNull(regionCode, "password cannot be null");
      this.regionCode = regionCode;
      return this;
    }
    
    public NationalProviderIdentifier build () {
      requireNonNull(id, "id cannot be null");
      requireNonNull(idNumber, "idNumber cannot be null");
      requireNonNull(regionCode, "password cannot be null");
      return new NationalProviderIdentifier(this);      
    }
  }

}
