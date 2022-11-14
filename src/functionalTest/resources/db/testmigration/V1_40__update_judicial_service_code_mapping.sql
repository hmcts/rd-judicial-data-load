DELETE  FROM judicial_service_code_mapping where  ticket_code ='405' and service_code ='ABA3';
DELETE  FROM judicial_service_code_mapping where  ticket_code ='402' and service_code ='ABA2';
DELETE  FROM judicial_service_code_mapping where  ticket_code ='314' and service_code ='ABA2';


INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description)
VALUES ('115','400','ABA3','Family Public Law'),
('116','317','ABA3','Family Public Law');

COMMIT;