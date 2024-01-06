CREATE TABLE data
(
    id integer PRIMARY KEY AUTOINCREMENT,
    data_key text NOT NULL,
    data_property text NOT NULL,
    source_type text NOT NULL
);

CREATE TABLE data_column
(
    id integer PRIMARY KEY AUTOINCREMENT,
    column_name text NOT NULL,
    data text NOT NULL,
    properties text NOT NULL,
    data_id integer,
    CONSTRAINT fk_data FOREIGN KEY (data_id)
        REFERENCES data(id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);