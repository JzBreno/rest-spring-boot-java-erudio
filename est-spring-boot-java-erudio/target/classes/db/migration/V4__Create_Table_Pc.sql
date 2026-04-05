CREATE TABLE IF NOT EXISTS pc (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  cpu VARCHAR(40) NOT NULL,
    ram_memory VARCHAR(40) NOT NULL,
    storage_unit VARCHAR(40) NOT NULL,
    video_card VARCHAR(40) NOT NULL
    );