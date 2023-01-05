CREATE TABLE tag_type(id serial PRIMARY KEY, name VARCHAR(128) UNIQUE NOT NULL);
CREATE TABLE all_tags(id serial PRIMARY KEY, tag_id VARCHAR NOT NULL, tag_name VARCHAR UNIQUE NOT NULL, tag_type_id INT, FOREIGN KEY(tag_id) REFERENCES t_tag(tag_id_c), FOREIGN KEY(tag_type_id) REFERENCES tag_type(id));
CREATE TABLE doc_type (id serial PRIMARY KEY, "key" VARCHAR(128) UNIQUE NOT NULL, label VARCHAR(128));
CREATE TABLE doc_subtype (id serial PRIMARY KEY, doc_type_id INT NOT NULL, "key" VARCHAR(128) NOT NULL, label VARCHAR(128), FOREIGN KEY(doc_type_id) REFERENCES doc_type (id));
update T_CONFIG set CFG_VALUE_C = '29' where CFG_ID_C = 'DB_VERSION';
