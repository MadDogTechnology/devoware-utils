--liquibase formatted sql

--changeset challendy:9

CREATE TABLE claims (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  client_id integer NOT NULL,
  npi_id integer NOT NULL,
  claim_number VARCHAR(255) NOT NULL,
  provider_id VARCHAR(255) NOT NULL,
  status_code VARCHAR(255) ,
  location_code VARCHAR(255) ,
  bill_type_code VARCHAR(255) ,
  admission_date DATE ,
  received_date DATE,
  from_date DATE ,
  to_date DATE ,
  patient_last_name VARCHAR(255),
  patient_first_initial VARCHAR(255),
  charge_total NUMERIC(8,2),
  provider_reimbursement NUMERIC(8,2),
  paid_date DATE,
  cancel_date DATE,
  reason_code VARCHAR(255),
  nonpayment_code VARCHAR(255),
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

--rollback DROP TABLE IF EXISTS claims;

--changeset challendy:10
CREATE TRIGGER update_claims_updated_at
  BEFORE UPDATE ON claims
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
  
--rollback DROP TRIGGER IF EXISTS update_claims_updated_at ON claims;
