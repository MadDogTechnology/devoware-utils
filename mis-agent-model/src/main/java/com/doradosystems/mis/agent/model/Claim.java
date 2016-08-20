package com.doradosystems.mis.agent.model;

import static com.doradosystems.mis.agent.model.util.Constraints.checkNonnegative;
import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Claim.Builder.class)
public class Claim {

  private Long id;

  @JsonProperty("claim_number")
  private String claimNumber;

  @JsonProperty("provider_id")
  private String providerId;

  @JsonProperty("status_code")
  private String statusCode;

  @JsonProperty("location_code")
  private String locationCode;

  @JsonProperty("bill_type_code")
  private String billTypeCode;

  @JsonProperty("admission_date")
  private Date admissionDate;

  @JsonProperty("received_date")
  private Date receivedDate;

  @JsonProperty("from_date")
  private Date fromDate;

  @JsonProperty("to_date")
  private Date toDate;

  @JsonProperty("patient_last_name")
  private String patientLastName;

  @JsonProperty("patient_first_initial")
  private String patientFirstInitial;

  @JsonProperty("charge_total")
  private BigDecimal chargeTotal;

  @JsonProperty("provider_reimbursement")
  private BigDecimal providerReimbursement;

  @JsonProperty("paid_date")
  private Date paidDate;

  @JsonProperty("cancel_date")
  private Date cancelDate;

  @JsonProperty("reason_code")
  private String reasonCode;

  @JsonProperty("nonpayment_code")
  private String nonpaymentCode;  

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
    this.nonpaymentCode = builder.nonpaymentCode;
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

  public Date getAdmissionDate() {
    return admissionDate;
  }

  public Date getReceivedDate() {
    return receivedDate;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
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

  public BigDecimal getChargeTotal() {
    return chargeTotal;
  }

  public BigDecimal getProviderReimbursement() {
    return providerReimbursement;
  }

  public Date getPaidDate() {
    return paidDate;
  }

  public Date getCancelDate() {
    return cancelDate;
  }

  @Nonnull
  public String getReasonCode() {
    return reasonCode;
  }

  @Nonnull
  public String getNonpaymentCode() {
    return nonpaymentCode;
  }

  @JsonPOJOBuilder
  public static class Builder {
    private Long id;
    private String claimNumber;
    private String providerId;
    private String statusCode;
    private String locationCode;
    private String billTypeCode;
    private Date admissionDate;
    private Date receivedDate;
    private Date fromDate;
    private Date toDate;
    private String patientLastName;
    private String patientFirstInitial;
    private BigDecimal chargeTotal;
    private BigDecimal providerReimbursement;
    private Date paidDate;
    private Date cancelDate;
    private String reasonCode;
    private String nonpaymentCode;  
    
    private Builder () {}

    public Builder withId(@Nonnull Long id) {
      this.id = requireNonNull(id, "id cannot be null");
      return this;
    }

    @JsonProperty("claim_number")
    public Builder withClaimNumber(@Nonnull String claimNumber) {
      this.claimNumber = requireNonNull(claimNumber, "claimNumber cannot be null");
      return this;
    }

    @JsonProperty("provider_id")
    public Builder withProviderId(@Nonnull String providerId) {
      this.providerId = requireNonNull(providerId, "providerId cannot be null");
      return this;
    }

    @JsonProperty("status_code")
    public Builder withStatusCode(@Nonnull String statusCode) {
      this.statusCode = requireNonNull(statusCode, "statusCode cannot be null");
      return this;
    }

    @JsonProperty("location_code")
    public Builder withLocationCode(@Nonnull String locationCode) {
      this.locationCode = requireNonNull(locationCode, "locationCode cannot be null");
      return this;
    }

    @JsonProperty("bill_type_code")
    public Builder withBillTypeCode(@Nonnull String billTypeCode) {
      this.billTypeCode = requireNonNull(billTypeCode, "billTypeCode cannot be null");
      return this;
    }

    @JsonProperty("admission_date")
    public Builder withAdmissionDate(@Nonnull Date admissionDate) {
      this.admissionDate = requireNonNull(admissionDate, "admissionDate cannot be null");
      return this;
    }

    @JsonProperty("received_date")
    public Builder withReceivedDate(@Nonnull Date receivedDate) {
      this.receivedDate = requireNonNull(receivedDate, "receivedDate cannot be null");
      return this;
    }

    @JsonProperty("from_date")
    public Builder withFromDate(@Nonnull Date fromDate) {
      this.fromDate = requireNonNull(fromDate, "fromDate cannot be null");
      return this;
    }

    @JsonProperty("to_date")
    public Builder withToDate(@Nonnull Date toDate) {
      this.toDate = requireNonNull(toDate, "toDate cannot be null");
      return this;
    }

    @JsonProperty("patient_last_name")
    public Builder withPatientLastName(@Nonnull String patientLastName) {
      this.patientLastName = requireNonNull(patientLastName, "patientLastName cannot be null");
      return this;
    }

    @JsonProperty("patient_first_initial")
    public Builder withPatientFirstInitial(@Nonnull String patientFirstInitial) {
      this.patientFirstInitial = requireNonNull(patientFirstInitial, "patientFirstInitial cannot be null");
      return this;
    }

    @JsonProperty("charge_total")
    public Builder withChargeTotal(@Nonnull BigDecimal chargeTotal) {
      this.chargeTotal = checkNonnegative(requireNonNull(chargeTotal, "chargeTotal cannot be null"), "chargeTotal cannot be negative");
      return this;
    }

    @JsonProperty("provider_reimbursement")
    public Builder withProviderReimbursement(@Nonnull BigDecimal providerReimbursement) {
      this.providerReimbursement = checkNonnegative(requireNonNull(providerReimbursement, "providerReimbursement cannot be null"), "providerReimbursement cannot be negative");
      return this;
    }

    @JsonProperty("paid_date")
    public Builder withPaidDate(@Nullable Date paidDate) {
      this.paidDate = paidDate;
      return this;
    }

    @JsonProperty("cancel_date")
    public Builder withCancelDate(@Nullable Date cancelDate) {
      this.cancelDate = cancelDate;
      return this;
    }

    @JsonProperty("reason_code")
    public Builder withReasonCode(@Nullable String reasonCode) {
      this.reasonCode = reasonCode;
      return this;
    }

    @JsonProperty("nonpayment_code")
    public Builder withNonpaymentCode(@Nullable String nonpaymentCode) {
      this.nonpaymentCode = nonpaymentCode;
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
      return new Claim(this);
    }
  }
}
