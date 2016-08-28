-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: migrations.xml
-- Ran at: 8/26/16 10:57 AM
-- Against: cdevoto@jdbc:postgresql://localhost:5432/cdevoto
-- Liquibase version: 3.4.2
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE public.databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM public.databasechangeloglock;

INSERT INTO public.databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE public.databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'carloss-mbp.dh.resolutebi.com (10.32.33.23)', LOCKGRANTED = '2016-08-26 10:57:24.119' WHERE ID = 1 AND LOCKED = FALSE;

-- Create Database Change Log Table
CREATE TABLE public.databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITHOUT TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255));

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0001_create_clients_table.sql::1::challendy
CREATE TABLE clients (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0001_create_clients_table.sql', NOW(), 1, '7:1577716b7971ac312105ad099bd85e3c', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0001_create_clients_table.sql::2::challendy
CREATE OR REPLACE FUNCTION update_updated_at_column()
  RETURNS TRIGGER AS $$
  BEGIN
    NEW.updated_at = now();
    RETURN NEW;
  END;
  $$ language plpgsql;

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0001_create_clients_table.sql', NOW(), 2, '7:f7c5e5622fe084563975878fce84f12b', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0001_create_clients_table.sql::3::challendy
CREATE TRIGGER update_clients_updated_at
  BEFORE UPDATE ON clients
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('3', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0001_create_clients_table.sql', NOW(), 3, '7:6d3202fbd73ceadaf32f433d38aa7797', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0002_create_operators_table.sql::4::challendy
CREATE TABLE operators (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  client_id INTEGER NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  region_code VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('4', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0002_create_operators_table.sql', NOW(), 4, '7:c3c76c2ff465b3e03c65efed3052b4f9', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0002_create_operators_table.sql::5::challendy
CREATE TRIGGER update_operators_updated_at
  BEFORE UPDATE ON operators
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('5', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0002_create_operators_table.sql', NOW(), 5, '7:b04bbc1de728c4c727c5a8c27e375969', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0003_create_npis_table.sql::6::challendy
CREATE TABLE operators_npis (
  operator_id integer NOT NULL,
  npi_id integer NOT NULL
);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('6', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0003_create_npis_table.sql', NOW(), 6, '7:ad7937db92f7418cfd96231458a2a38c', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0003_create_npis_table.sql::7::challendy
CREATE TABLE npis (
  id SERIAL PRIMARY KEY NOT NULL UNIQUE,
  id_number INTEGER NOT NULL,
  region_code VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('7', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0003_create_npis_table.sql', NOW(), 7, '7:cf46ac88f4c27679d2d3e2719ffc8341', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0003_create_npis_table.sql::8::challendy
CREATE TRIGGER update_npis_updated_at
  BEFORE UPDATE ON npis
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('8', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0003_create_npis_table.sql', NOW(), 8, '7:4ad179bac9c378ad23670fce7b8036ae', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0004_create_claims_table.sql::9::challendy
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

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('9', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0004_create_claims_table.sql', NOW(), 9, '7:34560ddaa94c319f987100c573826ba6', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset /Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0004_create_claims_table.sql::10::challendy
CREATE TRIGGER update_claims_updated_at
  BEFORE UPDATE ON claims
  FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('10', 'challendy', '/Users/cdevoto/Documents/eclipse/workspaces/dorado/database-migration/migrations/0004_create_claims_table.sql', NOW(), 10, '7:13ea4a0db7fb7757156bef815307a243', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Release Database Lock
UPDATE public.databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;
