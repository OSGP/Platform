-- device_model table
DROP SEQUENCE device_model_id_sequence;

ALTER TABLE device_model RENAME COLUMN manufacturer TO manufacturer_id;

ALTER TABLE device_model RENAME COLUMN code TO model_code;
ALTER TABLE device_model ALTER COLUMN model_code TYPE varchar(15);

ALTER TABLE device_model RENAME COLUMN name TO description;
ALTER TABLE device_model ALTER COLUMN description TYPE varchar(255);

-- manufacturer table
ALTER TABLE manufacturer RENAME COLUMN code TO manufacturer_id;