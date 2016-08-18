package com.doradosystems.mis.agent.model.util;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

public class Constraints {
  
  public static <T extends Number> T checkNonnegative(@Nonnull T number, @Nonnull String message) {
    checkArgument(requireNonNull(number).doubleValue() >= 0, requireNonNull(message));
    return number;
  }

  private Constraints() {
  }

}
