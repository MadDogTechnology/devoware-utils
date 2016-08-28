--liquibase formatted sql

--changeset challendy:6

CREATE TABLE operators_npis (
  operator_id integer NOT NULL,
  npi_id integer NOT NULL
);

--rollback DROP TABLE IF EXISTS operators_npis;

--changeset challendy:7

CREATE TABLE npis (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  id_number INTEGER NOT NULL,
  region_code VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

--rollback DROP TABLE IF EXISTS npis;

--changeset challendy:8
CREATE TRIGGER update_npis_updated_at
  BEFORE UPDATE ON npis
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

--rollback DROP TRIGGER IF EXISTS update_npis_updated_at ON npis;
