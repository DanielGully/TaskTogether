CREATE TABLE to_do_entity
(
    id            UUID         NOT NULL,
    title         VARCHAR(100) NOT NULL,
    description   VARCHAR(500),
    priority      SMALLINT     NOT NULL,
    deadline_date date,
    CONSTRAINT pk_todoentity PRIMARY KEY (id)
);