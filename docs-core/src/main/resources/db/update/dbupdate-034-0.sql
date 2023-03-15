alter table T_DOCUMENT add column PROCESSING_DETAIL TEXT;
alter table T_DOCUMENT add column WORKFLOW_DETAIL TEXT;
update T_CONFIG set CFG_VALUE_C = '34' where CFG_ID_C = 'DB_VERSION';
