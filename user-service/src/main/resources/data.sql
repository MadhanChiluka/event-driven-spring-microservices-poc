insert into card_details(id, name, card_number, valid_until_month, valid_until_year, cvv) 
values('138e753b-5011-4d91-a935-d3cedd9ba5e7', 'Madhan Chiluka', '1234567890123456', 5, 25, 111);

insert into user_table(user_id, first_name, last_name, card_details_id) 
values('4eab0a07-8bbc-4f3b-9c5e-48c1519ffb37', 'Madhan', 'Chiluka', '138e753b-5011-4d91-a935-d3cedd9ba5e7');