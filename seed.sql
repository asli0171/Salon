INSERT INTO admin (username, password) VALUES
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7i');

INSERT INTO hairdresser (name, bio, active) VALUES
                                                ('Anna Nielsen', 'Specialist i farve og klipning med 10 års erfaring.', true),
                                                ('Mads Christensen', 'Ekspert i herreklip og moderne styling.', true),
                                                ('Sara Pedersen', 'Behandlings- og plejespecialist.', true);

INSERT INTO service (name, description, duration_minutes, base_price) VALUES
                                                                          ('Klip', 'Professionel klipning tilpasset dit hår og ansigt.', 45, 350.00),
                                                                          ('Farve', 'Hårfarve med høj kvalitetsprodukter.', 90, 750.00),
                                                                          ('Behandling', 'Dybdegående hårpleje og genopbygning.', 60, 550.00),
                                                                          ('Styling', 'Professionel styling til særlige lejligheder.', 30, 250.00);

INSERT INTO slot (hairdresser_id, service_id, start_time, end_time, is_available) VALUES
                                                                                      (1, 1, '2026-06-02 09:00:00', '2026-06-02 09:45:00', true),
                                                                                      (1, 1, '2026-06-02 10:00:00', '2026-06-02 10:45:00', true),
                                                                                      (1, 2, '2026-06-02 11:00:00', '2026-06-02 12:30:00', true),
                                                                                      (2, 1, '2026-06-02 09:00:00', '2026-06-02 09:45:00', true),
                                                                                      (2, 1, '2026-06-02 13:00:00', '2026-06-02 13:45:00', true),
                                                                                      (3, 3, '2026-06-02 10:00:00', '2026-06-02 11:00:00', true),
                                                                                      (3, 3, '2026-06-02 14:00:00', '2026-06-02 15:00:00', true),
                                                                                      (1, 4, '2026-06-03 09:00:00', '2026-06-03 09:30:00', true),
                                                                                      (2, 2, '2026-06-03 10:00:00', '2026-06-03 11:30:00', true),
                                                                                      (3, 1, '2026-06-03 13:00:00', '2026-06-03 13:45:00', true);