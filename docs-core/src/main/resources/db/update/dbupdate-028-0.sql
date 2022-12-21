alter table T_DOCUMENT add column DOC_BORROWER_ID varchar(64);
alter table T_DOCUMENT add column DOC_PROCESSED_VENDOR varchar(36);
alter table T_DOCUMENT add column DOC_PROCESSED_VENDOR_ID varchar(64);
update T_CONFIG set CFG_VALUE_C = '28' where CFG_ID_C = 'DB_VERSION';
