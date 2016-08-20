package com.doradosystems.mis.agent.model;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClaimTest {
  private ObjectMapper mapper;
  
  @Before
  public void setup() {
    this.mapper = new ObjectMapper();
  }
  
  @Test
  public void test_deserialization () throws JsonParseException, JsonMappingException, IOException {
    String json = "{\"claim_number\":\"449824056A\",\"provider_id\":\"675344\",\"status_code\":\"S\",\"location_code\":\"B0100\",\"bill_type_code\":\"214\",\"admission_date\":\"2016-06-22\",\"received_date\":\"2016-08-04\",\"from_date\":\"2016-07-01\",\"to_date\":\"2016-07-05\",\"patient_last_name\":\"LAWLESS\",\"patient_first_initial\":\"N\",\"charge_total\":1833.2,\"provider_reimbursement\":2281.79,\"paid_date\":null,\"cancel_date\":null,\"reason_code\":null,\"nonpayment_code\":null}";
    Claim claim = mapper.readValue(json, Claim.class);
    Assert.assertNotNull(claim);
  }

}
