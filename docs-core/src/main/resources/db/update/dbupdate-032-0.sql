ALTER TABLE doc_subtype ADD COLUMN additional_tag VARCHAR(16);
update T_CONFIG set CFG_VALUE_C = '32' where CFG_ID_C = 'DB_VERSION';
