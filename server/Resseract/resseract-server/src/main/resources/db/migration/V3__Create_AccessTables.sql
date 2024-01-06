CREATE TABLE data_access
(
    id integer PRIMARY KEY AUTOINCREMENT,
    user_identifier text NOT NULL,
    access_role text NOT NULL,
    data_id integer REFERENCES data(id) ON update CASCADE ON delete CASCADE
);

CREATE TABLE dashboard_access
(
    id integer PRIMARY KEY AUTOINCREMENT,
    user_identifier text NOT NULL,
    access_role text NOT NULL,
    dashboard_id integer REFERENCES dashboard(id) ON update CASCADE ON delete CASCADE
);
