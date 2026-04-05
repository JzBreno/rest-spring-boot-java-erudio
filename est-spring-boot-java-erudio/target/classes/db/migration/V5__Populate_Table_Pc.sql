-- Flyway Migration V1.1__Insert_initial_pcs.sql

-- Insert 1: Configuração de entrada (Entry Level)
INSERT INTO pc (cpu, ram_memory, storage_unit, video_card) VALUES
    ('Intel Core i3-10100F', '8GB DDR4 2666MHz', 'SSD SATA 240GB', 'NVIDIA GeForce GTX 1650');

-- Insert 2: Configuração de gama média (Mid-Range)
INSERT INTO pc (cpu, ram_memory, storage_unit, video_card) VALUES
    ('AMD Ryzen 5 5600', '16GB DDR4 3200MHz', 'SSD NVMe 500GB', 'NVIDIA GeForce RTX 3060');

-- Insert 3: Configuração de alto desempenho (High-Performance)
INSERT INTO pc (cpu, ram_memory, storage_unit, video_card) VALUES
    ('Intel Core i7-13700K', '32GB DDR5 5600MHz', 'SSD NVMe 1TB', 'NVIDIA GeForce RTX 4070 Ti');

-- Insert 4: Configuração para estação de trabalho (Workstation/Productivity)
INSERT INTO pc (cpu, ram_memory, storage_unit, video_card) VALUES
    ('AMD Ryzen 9 7900X', '64GB DDR5 6000MHz', 'SSD NVMe 2TB', 'AMD Radeon PRO W6800');

-- Insert 5: Configuração compacta/mini-PC (Small Form Factor)
INSERT INTO pc (cpu, ram_memory, storage_unit, video_card) VALUES
    ('Intel Core i5-12400', '16GB DDR4 3200MHz', 'SSD NVMe 500GB', 'Integrated Graphics');