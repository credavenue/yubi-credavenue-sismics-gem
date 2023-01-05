ALTER TABLE t_tag ALTER COLUMN tag_name_c type varchar(128);
update T_CONFIG set CFG_VALUE_C = '30' where CFG_ID_C = 'DB_VERSION';
