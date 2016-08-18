package com.doradosystems.mis.agent.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Operator.Builder.class)
public class Operator {
  private final long id;
  private final String userName;
  private final String password;
  
  @JsonCreator
  @Nonnull
  public static Builder builder() {
    return new Builder();
  }

  private Operator(@Nonnull Builder builder) {
    this.id = builder.id;
    this.userName = builder.userName;
    this.password = builder.password;
  }
  
  public long getId() {
    return id;
  }
  
  @Nonnull
  public String getUserName() {
    return userName;
  }
  
  @Nonnull
  public String getPassword() {
    return password;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
    Operator other = (Operator) obj;
    if (id != other.id)
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (userName == null) {
      if (other.userName != null)
        return false;
    } else if (!userName.equals(other.userName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Operator [id=" + id + ", userName=" + userName + ", password=" + password + "]";
  }

  @JsonPOJOBuilder
  public static class Builder {
    private Long id;
    private String userName;
    private String password;
    
    private Builder() {}

    public Builder withId(long id) {
      checkArgument(id >= 0, "id must be a non-negative number"); 
      this.id = id;
      return this;
    }

    public Builder withUserName(@Nonnull String userName) {
      requireNonNull(userName, "userName cannot be null");
      this.userName = userName;
      return this;
    }

    public Builder withPassword(@Nonnull String password) {
      requireNonNull(password, "password cannot be null");
      this.password = password;
      return this;
    }
    
    public Operator build () {
      requireNonNull(id, "id cannot be null");
      requireNonNull(userName, "userName cannot be null");
      requireNonNull(password, "password cannot be null");
      return new Operator(this);      
    }
  }

}
