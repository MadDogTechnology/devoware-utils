--liquibase formatted sql

--changeset challendy:4

CREATE TABLE operators (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  client_id INTEGER NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  region_code VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

--rollback DROP TABLE IF EXISTS operators;

--changeset challendy:5
CREATE TRIGGER update_operators_updated_at
  BEFORE UPDATE ON operators
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

--rollback DROP TRIGGER IF EXISTS update_operator_updated_at ON operators;
