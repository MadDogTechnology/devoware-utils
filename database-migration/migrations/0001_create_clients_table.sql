--liquibase formatted sql

--changeset challendy:1

CREATE TABLE clients (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

--rollback DROP TABLE IF EXISTS clients;

--changeset challendy:2 splitStatements:false
CREATE OR REPLACE FUNCTION update_updated_at_column()
  RETURNS TRIGGER AS $$
  BEGIN
    NEW.updated_at = now();
    RETURN NEW;
  END;
  $$ language plpgsql;

--rollback DROP FUNCTION IF EXISTS update_updated_at_column()

--changeset challendy:3
CREATE TRIGGER update_clients_updated_at
  BEFORE UPDATE ON clients
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

--rollback DROP TRIGGER IF EXISTS update_clients_updated_at ON clients;