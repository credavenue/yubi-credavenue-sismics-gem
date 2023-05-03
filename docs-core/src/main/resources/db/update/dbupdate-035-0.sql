ALTER TABLE file_password RENAME TO file_details;
alter table file_details add column IS_SCANNED BOOLEAN DEFAULT FALSE;
ALTER TABLE T_DOCUMENT DROP COLUMN IS_SCANNED;
update T_CONFIG set CFG_VALUE_C = '35' where CFG_ID_C = 'DB_VERSION';