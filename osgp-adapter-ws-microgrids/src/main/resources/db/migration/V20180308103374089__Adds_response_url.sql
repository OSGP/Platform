DO
$$
BEGIN

IF NOT EXISTS (
    SELECT 1
    FROM   information_schema.columns
    WHERE  table_schema = current_schema
    AND    table_name  = 'response_data'
    AND	   column_name = 'response_url') THEN

    ALTER TABLE response_data ADD COLUMN response_url character varying(255);

END IF;

END;
$$