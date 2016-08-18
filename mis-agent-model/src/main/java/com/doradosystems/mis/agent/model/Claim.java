package com.doradosystems.mis.agent.model;

import static com.doradosystems.mis.agent.model.util.Constraints.checkNonnegative;
import static java.util.Objects.requireNonNull;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Claim.Builder.class)
public class Claim {

  private final Long id;
  private final String claimNumber;
  private final String providerId;
  private final String statusCode;
  private final String locationCode;
  private final String billTypeCode;
  private final long admissionDate;
  private final long receivedDate;
  private final long fromDate;
  private final long toDate;
  private final String patientLastName;
  private final String patientFirstInitial;
  private final double chargeTotal;
  private final double providerReimbursement;
  private final long paidDate;
  private final long cancelDate;
  private final String reasonCode;
  private final String nonPaymentCode;
  private final Long createdAt;
  private final Long updatedAt;
  
  @JsonCreator
  @Nonnull
  public static Builder builder () {
    return new Builder();
  }
  
  private Claim (@Nonnull Builder builder) {
    this.id = builder.id;
    this.claimNumber = builder.claimNumber;
    this.providerId = builder.providerId;
    this.statusCode = builder.statusCode;
    this.locationCode = builder.locationCode;
    this.billTypeCode = builder.billTypeCode;
    this.admissionDate = builder.admissionDate;
    this.receivedDate = builder.receivedDate;
    this.fromDate = builder.fromDate;
    this.toDate = builder.toDate;
    this.patientLastName = builder.patientLastName;
    this.patientFirstInitial = builder.patientFirstInitial;
    this.chargeTotal = builder.chargeTotal;
    this.providerReimbursement = builder.providerReimbursement;
    this.paidDate = builder.paidDate;
    this.cancelDate = builder.cancelDate;
    this.reasonCode = builder.reasonCode;
    this.nonPaymentCode = builder.nonPaymentCode;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
  }
  
  @CheckForNull
  public Long getId() {
    return id;
  }

  @Nonnull
  public String getClaimNumber() {
    return claimNumber;
  }

  @Nonnull
  public String getProviderId() {
    return providerId;
  }

  @Nonnull
  public String getStatusCode() {
    return statusCode;
  }

  @Nonnull
  public String getLocationCode() {
    return locationCode;
  }

  @Nonnull
  public String getBillTypeCode() {
    return billTypeCode;
  }

  public long getAdmissionDate() {
    return admissionDate;
  }

  public long getReceivedDate() {
    return receivedDate;
  }

  public long getFromDate() {
    return fromDate;
  }

  public long getToDate() {
    return toDate;
  }

  @Nonnull
  public String getPatientLastName() {
    return patientLastName;
  }

  @Nonnull
  public String getPatientFirstInitial() {
    return patientFirstInitial;
  }

  public double getChargeTotal() {
    return chargeTotal;
  }

  public double getProviderReimbursement() {
    return providerReimbursement;
  }

  public long getPaidDate() {
    return paidDate;
  }

  public long getCancelDate() {
    return cancelDate;
  }

  @Nonnull
  public String getReasonCode() {
    return reasonCode;
  }

  @Nonnull
  public String getNonPaymentCode() {
    return nonPaymentCode;
  }

  @CheckForNull
  public Long getCreatedAt() {
    return createdAt;
  }

  @CheckForNull
  public Long getUpdatedAt() {
    return updatedAt;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (admissionDate ^ (admissionDate >>> 32));
    result = prime * result + ((billTypeCode == null) ? 0 : billTypeCode.hashCode());
    result = prime * result + (int) (cancelDate ^ (cancelDate >>> 32));
    long temp;
    temp = Double.doubleToLongBits(chargeTotal);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((claimNumber == null) ? 0 : claimNumber.hashCode());
    result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
    result = prime * result + (int) (fromDate ^ (fromDate >>> 32));
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((locationCode == null) ? 0 : locationCode.hashCode());
    result = prime * result + ((nonPaymentCode == null) ? 0 : nonPaymentCode.hashCode());
    result = prime * result + (int) (paidDate ^ (paidDate >>> 32));
    result = prime * result + ((patientFirstInitial == null) ? 0 : patientFirstInitial.hashCode());
    result = prime * result + ((patientLastName == null) ? 0 : patientLastName.hashCode());
    result = prime * result + ((providerId == null) ? 0 : providerId.hashCode());
    temp = Double.doubleToLongBits(providerReimbursement);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((reasonCode == null) ? 0 : reasonCode.hashCode());
    result = prime * result + (int) (receivedDate ^ (receivedDate >>> 32));
    result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
    result = prime * result + (int) (toDate ^ (toDate >>> 32));
    result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
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
    Claim other = (Claim) obj;
    if (admissionDate != other.admissionDate)
      return false;
    if (billTypeCode == null) {
      if (other.billTypeCode != null)
        return false;
    } else if (!billTypeCode.equals(other.billTypeCode))
      return false;
    if (cancelDate != other.cancelDate)
      return false;
    if (Double.doubleToLongBits(chargeTotal) != Double.doubleToLongBits(other.chargeTotal))
      return false;
    if (claimNumber == null) {
      if (other.claimNumber != null)
        return false;
    } else if (!claimNumber.equals(other.claimNumber))
      return false;
    if (createdAt == null) {
      if (other.createdAt != null)
        return false;
    } else if (!createdAt.equals(other.createdAt))
      return false;
    if (fromDate != other.fromDate)
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (locationCode == null) {
      if (other.locationCode != null)
        return false;
    } else if (!locationCode.equals(other.locationCode))
      return false;
    if (nonPaymentCode == null) {
      if (other.nonPaymentCode != null)
        return false;
    } else if (!nonPaymentCode.equals(other.nonPaymentCode))
      return false;
    if (paidDate != other.paidDate)
      return false;
    if (patientFirstInitial == null) {
      if (other.patientFirstInitial != null)
        return false;
    } else if (!patientFirstInitial.equals(other.patientFirstInitial))
      return false;
    if (patientLastName == null) {
      if (other.patientLastName != null)
        return false;
    } else if (!patientLastName.equals(other.patientLastName))
      return false;
    if (providerId == null) {
      if (other.providerId != null)
        return false;
    } else if (!providerId.equals(other.providerId))
      return false;
    if (Double.doubleToLongBits(providerReimbursement) != Double
        .doubleToLongBits(other.providerReimbursement))
      return false;
    if (reasonCode == null) {
      if (other.reasonCode != null)
        return false;
    } else if (!reasonCode.equals(other.reasonCode))
      return false;
    if (receivedDate != other.receivedDate)
      return false;
    if (statusCode == null) {
      if (other.statusCode != null)
        return false;
    } else if (!statusCode.equals(other.statusCode))
      return false;
    if (toDate != other.toDate)
      return false;
    if (updatedAt == null) {
      if (other.updatedAt != null)
        return false;
    } else if (!updatedAt.equals(other.updatedAt))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Claim [id=" + id + ", claimNumber=" + claimNumber + ", providerId=" + providerId
        + ", statusCode=" + statusCode + ", locationCode=" + locationCode + ", billTypeCode="
        + billTypeCode + ", admissionDate=" + admissionDate + ", receivedDate=" + receivedDate
        + ", fromDate=" + fromDate + ", toDate=" + toDate + ", patientLastName=" + patientLastName
        + ", patientFirstInitial=" + patientFirstInitial + ", chargeTotal=" + chargeTotal
        + ", providerReimbursement=" + providerReimbursement + ", paidDate=" + paidDate
        + ", cancelDate=" + cancelDate + ", reasonCode=" + reasonCode + ", nonPaymentCode="
        + nonPaymentCode + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
  }

  @JsonPOJOBuilder
  public static class Builder {
    private Long id;
    private String claimNumber;
    private String providerId;
    private String statusCode;
    private String locationCode;
    private String billTypeCode;
    private Long admissionDate;
    private Long receivedDate;
    private Long fromDate;
    private Long toDate;
    private String patientLastName;
    private String patientFirstInitial;
    private Double chargeTotal;
    private Double providerReimbursement;
    private Long paidDate;
    private Long cancelDate;
    private String reasonCode;
    private String nonPaymentCode;
    private Long createdAt;
    private Long updatedAt;
    
    private Builder () {}

    public Builder withId(@Nonnull Long id) {
      this.id = requireNonNull(id, "id cannot be null");
      return this;
    }

    public Builder withClaimNumber(@Nonnull String claimNumber) {
      this.claimNumber = requireNonNull(claimNumber, "claimNumber cannot be null");
      return this;
    }

    public Builder withProviderId(@Nonnull String providerId) {
      this.providerId = requireNonNull(providerId, "providerId cannot be null");
      return this;
    }

    public Builder withStatusCode(@Nonnull String statusCode) {
      this.statusCode = requireNonNull(statusCode, "statusCode cannot be null");
      return this;
    }

    public Builder withLocationCode(@Nonnull String locationCode) {
      this.locationCode = requireNonNull(locationCode, "locationCode cannot be null");
      return this;
    }

    public Builder withBillTypeCode(@Nonnull String billTypeCode) {
      this.billTypeCode = requireNonNull(billTypeCode, "billTypeCode cannot be null");
      return this;
    }

    public Builder withAdmissionDate(@Nonnull Long admissionDate) {
      this.admissionDate = checkNonnegative(requireNonNull(admissionDate, "admissionDate cannot be null"), "admissionDate cannot be negative");
      return this;
    }

    public Builder withReceivedDate(@Nonnull Long receivedDate) {
      this.receivedDate = checkNonnegative(requireNonNull(receivedDate, "receivedDate cannot be null"), "receivedDate cannot be negative");
      return this;
    }

    public Builder withFromDate(@Nonnull Long fromDate) {
      this.fromDate = checkNonnegative(requireNonNull(fromDate, "fromDate cannot be null"), "fromDate cannot be negative");
      return this;
    }

    public Builder withToDate(@Nonnull Long toDate) {
      this.toDate = checkNonnegative(requireNonNull(toDate, "toDate cannot be null"), "toDate cannot be negative");
      return this;
    }

    public Builder withPatientLastName(@Nonnull String patientLastName) {
      this.patientLastName = requireNonNull(patientLastName, "patientLastName cannot be null");
      return this;
    }

    public Builder withPatientFirstInitial(@Nonnull String patientFirstInitial) {
      this.patientFirstInitial = requireNonNull(patientFirstInitial, "patientFirstInitial cannot be null");
      return this;
    }

    public Builder withChargeTotal(@Nonnull Double chargeTotal) {
      this.chargeTotal = checkNonnegative(requireNonNull(chargeTotal, "chargeTotal cannot be null"), "chargeTotal cannot be negative");
      return this;
    }

    public Builder withProviderReimbursement(@Nonnull Double providerReimbursement) {
      this.providerReimbursement = checkNonnegative(requireNonNull(providerReimbursement, "providerReimbursement cannot be null"), "providerReimbursement cannot be negative");
      return this;
    }

    public Builder withPaidDate(@Nonnull Long paidDate) {
      this.paidDate = checkNonnegative(requireNonNull(paidDate, "paidDate cannot be null"), "paidDate cannot be negative");
      return this;
    }

    public Builder withCancelDate(@Nonnull Long cancelDate) {
      this.cancelDate = checkNonnegative(requireNonNull(cancelDate, "cancelDate cannot be null"), "cancelDate cannot be negative");
      return this;
    }

    public Builder withReasonCode(@Nonnull String reasonCode) {
      this.reasonCode = requireNonNull(reasonCode, "reasonCode cannot be null");
      return this;
    }

    public Builder withNonPaymentCode(@Nonnull String nonPaymentCode) {
      this.nonPaymentCode = requireNonNull(nonPaymentCode, "nonPaymentCode cannot be null");
      return this;
    }

    public Builder withCreatedAt(@Nonnull Long createdAt) {
      this.createdAt = checkNonnegative(requireNonNull(createdAt, "createdAt cannot be null"), "createdAt cannot be negative");
      return this;
    }

    public Builder withUpdatedAt(@Nonnull Long updatedAt) {
      this.updatedAt = checkNonnegative(requireNonNull(updatedAt, "updatedAt cannot be null"), "updatedAt cannot be negative");
      return this;
    }
    
    public Claim build () {
      requireNonNull(claimNumber, "claimNumber cannot be null");
      requireNonNull(providerId, "providerId cannot be null");
      requireNonNull(statusCode, "statusCode cannot be null");
      requireNonNull(locationCode, "locationCode cannot be null");
      requireNonNull(billTypeCode, "billTypeCode cannot be null");
      requireNonNull(admissionDate, "admissionDate cannot be null");
      requireNonNull(receivedDate, "receivedDate cannot be null");
      requireNonNull(fromDate, "fromDate cannot be null");
      requireNonNull(toDate, "toDate cannot be null");
      requireNonNull(patientLastName, "patientLastName cannot be null");
      requireNonNull(patientFirstInitial, "patientFirstInitial cannot be null");
      requireNonNull(chargeTotal, "chargeTotal cannot be null");
      requireNonNull(providerReimbursement, "providerReimbursement cannot be null");
      requireNonNull(paidDate, "paidDate cannot be null");
      requireNonNull(cancelDate, "cancelDate cannot be null");
      requireNonNull(reasonCode, "reasonCode cannot be null");
      requireNonNull(nonPaymentCode, "nonPaymentCode cannot be null");
      return new Claim(this);
    }
  }
}
