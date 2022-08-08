DELETE  FROM judicial_service_code_mapping where  ticket_code ='405' and service_code ='ABA4';
DELETE  FROM judicial_service_code_mapping where  ticket_code ='405' and service_code ='ABA5';
UPDATE judicial_service_code_mapping SET service_code ='ABA5',service_description ='Family Private Law' where ticket_code ='317';
UPDATE judicial_service_code_mapping SET service_code ='ABA5',service_description ='Family Private Law' where ticket_code ='400';
COMMIT;