insert into users(id,email,password_hash,name,created_at)
values (1,'demo@demo.com','demo','Demo User',now());

insert into instruments(id,owner_id,title,description,price_per_day,category)
values (1,1,'Yamaha Acoustic Guitar','Dreadnought 6‑string',10,'guitar');
