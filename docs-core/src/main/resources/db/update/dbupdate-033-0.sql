CREATE TABLE IF NOT EXISTS file_password(id serial PRIMARY KEY, file_id VARCHAR(64) UNIQUE NOT NULL, file_password BYTEA NOT NULL);
update T_CONFIG set CFG_VALUE_C = '33' where CFG_ID_C = 'DB_VERSION';
