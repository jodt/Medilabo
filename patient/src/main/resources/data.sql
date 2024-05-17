USE patients;

INSERT INTO address (number,street)
VALUES
    (1, 'Brookside St'),
    (2, 'High St'),
    (3, 'Club Road'),
    (4, 'Valley Dr');


INSERT INTO patient (first_name, last_name, date_of_birth, gender, phone_number, address_id)
    VALUES
    ('Test','TestNone','1966-12-31','F','100-222-3333',1),
    ('Test','TestBorderline','1945-06-24','M ','200-333-4444',2),
    ('Test','TestInDanger','2004-06-18','M','300-444-5555',3),
    ('Test','TestEarlyOnset','2002-06-28','F','400-555-6666',4);


