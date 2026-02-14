CREATE TABLE files (
                       id BIGINT AUTO_INCREMENT NOT NULL,
                       path VARCHAR(255) NOT NULL,
                       filesize BIGINT NOT NULL,
                       filename VARCHAR(255) NOT NULL,
                       CONSTRAINT pk_files PRIMARY KEY (id)
);