ALTER TABLE file_password RENAME TO file_details;
alter table file_details add column IS_SCANNED BOOLEAN DEFAULT FALSE;
alter table file_details alter column file_password drop not null;
alter table file_details add column reviewer_status_note TEXT;
update T_CONFIG set CFG_VALUE_C = '35' where CFG_ID_C = 'DB_VERSION';