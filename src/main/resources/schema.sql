--DROP TABLE IF EXISTS photo; -- for development only
DROP TABLE IF EXISTS file;

-- CREATE TABLE photo (
--    photoId BIGSERIAL PRIMARY KEY,
--    originalFileName VARCHAR(255) NOT NULL,
--    fileExtension VARCHAR(255) NOT NULL,
--    uniqueFileName VARCHAR(255) NOT NULL
-- );

-- CREATE TABLE file_type
-- (
--   file_type_id SERIAL PRIMARY KEY,
--   file_type    VARCHAR(140) NOT NULL,
--   created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW()
-- );

CREATE TABLE file
(
  file_id            BIGSERIAL PRIMARY KEY,
  original_file_name VARCHAR(255) NOT NULL,
  file_extension     VARCHAR(255) NOT NULL,
  unique_file_name   VARCHAR(255) NOT NULL,
  file_size          BIGINT       NOT NULL,
  file_type          VARCHAR(140) NOT NULL,
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW()--,
  --FOREIGN KEY (file_type) REFERENCES file_type (file_type_id)
);

comment on column file.file_size is 'Size in bytes';
