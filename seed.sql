
INSERT INTO admin (username, password) VALUES
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7i');
-- password: admin123


INSERT INTO hairdresser (name, bio, active) VALUES
    ('Helin', 'Indehaver, frisør og kosmetolog med speciale i farveteknikker og avancerede ansigtsbehandlinger.', true);


INSERT INTO service (name, description, duration_minutes, base_price) VALUES
                                                                          ('Dame klipning ink. vask og føn', 'Professionel klipning inklusiv hårvask, afslappende hovedbundsmassage og styling.', 45, 500.00),
                                                                          ('Dame klipning ink. vask, kur, føn og styling', 'Klipning med hårkur og professionel styling.', 80, 700.00),
                                                                          ('Herreklip ink. vask', 'Professionel herreklipning med hårvask.', 45, 350.00),
                                                                          ('Maskineklip', 'Hurtig og effektiv maskineklipning.', 30, 195.00),
                                                                          ('Børneklip 0-9 år', 'Blid og professionel klipning til de mindste.', 40, 250.00),
                                                                          ('Pandehår', 'Trim af pandehår.', 15, 100.00),
                                                                          ('Helfarve ink. vask (kort hår)', 'Helfarve med hårvask til kort hår.', 90, 800.00),
                                                                          ('Helfarve ink. vask (mellem langt hår)', 'Helfarve med hårvask til mellemlangt hår.', 150, 950.00),
                                                                          ('Helfarve ink. vask (langt hår)', 'Helfarve med hårvask til langt hår.', 150, 1150.00),
                                                                          ('Balayage/babylights/airtouch', 'Moderne farveteknikker til et naturligt, solkysset look.', 180, 1500.00),
                                                                          ('Toning', 'Toning af håret for ekstra glans og farve.', 60, 500.00),
                                                                          ('Vask, hårkur og føntørring', 'Plejende hårvask med kur og professionel føntørring.', 60, 350.00),
                                                                          ('Farve af bryn', 'Professionel farvning af bryn.', 15, 150.00),
                                                                          ('Farve af vipper', 'Professionel farvning af vipper.', 15, 150.00),
                                                                          ('Retning af bryn med tråd eller pincet', 'Formgivning af bryn til din ansigtsform.', 15, 150.00),
                                                                          ('Farvning af bryn + retning', 'Kombination af farvning og formgivning af bryn.', 20, 250.00),


                                                                          ('Luksus ansigtsbehandling', 'Hudanalyse, afrensning, peeling, dybderens, ansigtsmaske, ansigtsmassage og afsluttende creme.', 80, 800.00),
                                                                          ('MicroNeedling med skinpen', 'Avanceret hudfornyelse der stimulerer kollagen og elastin. Mindsker ar, fine linjer og pigmentforandringer.', 45, 950.00),
                                                                          ('Diamantslibning behandling', 'Skånsom fjernelse af døde hudceller med microdermabrasion. Efterlader huden glattere og mere ensartet.', 45, 475.00),
                                                                          ('AHA / kemisk peeling', 'Eksfoliering med naturlige frugtsyrer der fjerner døde hudceller og forbedrer hudens tekstur.', 30, 350.00);


INSERT INTO slot (hairdresser_id, service_id, start_time, end_time, is_available) VALUES
                                                                                      (1, 1,  '2026-06-10 09:00:00', '2026-06-10 09:45:00', true),
                                                                                      (1, 1,  '2026-06-10 10:00:00', '2026-06-10 10:45:00', true),
                                                                                      (1, 3,  '2026-06-10 11:00:00', '2026-06-10 11:45:00', true),
                                                                                      (1, 7,  '2026-06-10 12:00:00', '2026-06-10 13:30:00', true),
                                                                                      (1, 17, '2026-06-11 09:00:00', '2026-06-11 10:20:00', true),
                                                                                      (1, 18, '2026-06-11 11:00:00', '2026-06-11 11:45:00', true),
                                                                                      (1, 19, '2026-06-11 12:00:00', '2026-06-11 12:45:00', true),
                                                                                      (1, 20, '2026-06-12 09:00:00', '2026-06-12 09:30:00', true),
                                                                                      (1, 10, '2026-06-12 10:00:00', '2026-06-12 13:00:00', true),
                                                                                      (1, 2,  '2026-06-12 14:00:00', '2026-06-12 15:20:00', true);