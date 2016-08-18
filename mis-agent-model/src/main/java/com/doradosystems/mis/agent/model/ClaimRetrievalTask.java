package com.doradosystems.mis.agent.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@JsonDeserialize(builder = ClaimRetrievalTask.Builder.class)
public class ClaimRetrievalTask {
  private final Operator operator;
  private final List<NationalProviderIdentifier> nationalProviderIdentifiers;

  @JsonCreator
  @Nonnull
  public static Builder builder () {
    return new Builder();
  }
  
  private ClaimRetrievalTask(@Nonnull Builder builder) {
    this.operator = builder.operator;
    this.nationalProviderIdentifiers = ImmutableList.copyOf(builder.nationalProviderIdentifiers);
  }
  
  @Nonnull
  public Operator getOperator() {
    return operator;
  }

  @Nonnull
  public List<NationalProviderIdentifier> getNationalProviderIdentifiers() {
    return nationalProviderIdentifiers;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nationalProviderIdentifiers == null) ? 0 : nationalProviderIdentifiers.hashCode());
    result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
    ClaimRetrievalTask other = (ClaimRetrievalTask) obj;
    if (nationalProviderIdentifiers == null) {
      if (other.nationalProviderIdentifiers != null)
        return false;
    } else if (!nationalProviderIdentifiers.equals(other.nationalProviderIdentifiers))
      return false;
    if (operator == null) {
      if (other.operator != null)
        return false;
    } else if (!operator.equals(other.operator))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    return "ClaimRetrievalTask [operator=" + operator + ", npis=" + nationalProviderIdentifiers + "]";
  }

  @JsonPOJOBuilder
  public static class Builder {
    private Operator operator;
    private final List<NationalProviderIdentifier> nationalProviderIdentifiers = Lists.newArrayList();
    
    private Builder () {}

    @Nonnull
    public Builder withOperator(@Nonnull Operator operator) {
      requireNonNull(operator, "operator cannot be null");
      this.operator = operator;
      return this;
    }
    
    @Nonnull
    public Builder withNationalProviderIdentifier(@Nonnull NationalProviderIdentifier nationalProviderIdentifiers) {
      requireNonNull(nationalProviderIdentifiers, "notionalProviderIdentifiers cannot be null");
      this.nationalProviderIdentifiers.add(nationalProviderIdentifiers);
      return this;
    }
    
    @Nonnull
    public Builder withNationalProviderIdentifiers(@Nonnull Collection<NationalProviderIdentifier> nationalProviderIdentifiers) {
      requireNonNull(nationalProviderIdentifiers, "nationalProviderIdentifiers cannot be null");
      this.nationalProviderIdentifiers.addAll(nationalProviderIdentifiers);
      return this;
    }
    
    @Nonnull
    public ClaimRetrievalTask build () {
      requireNonNull(operator, "operator cannot be null");
      checkArgument(nationalProviderIdentifiers.size() > 0, "nationalProviderIdentifiers cannot be empty");
      return new ClaimRetrievalTask(this);
    }
  }
}
