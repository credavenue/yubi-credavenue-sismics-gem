ALTER TABLE doc_subtype ADD CONSTRAINT doc_subtype_key_unique UNIQUE ("key");
update T_CONFIG set CFG_VALUE_C = '31' where CFG_ID_C = 'DB_VERSION';
