CREATE TABLE xxx (
  id          BIGSERIAL     PRIMARY KEY,
  name        VARCHAR(255)  NOT NULL,
  category_id BIGSERIAL     NOT NULL,
  price       INTEGER       NOT NULL,
  active      BOOLEAN       NOT NULL
);
