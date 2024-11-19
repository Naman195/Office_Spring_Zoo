create table country(
country_id bigint primary key auto_increment,
country_name varchar(25) not null
);

create table state(
	state_id bigint primary key auto_increment,
    state_name varchar(20) not null,
    country_id bigint not null,
    foreign key (country_id) references country(country_id)
    );

   
   create table city(
   city_id bigint primary key auto_increment,
   city_name varchar(25) not null,
   state_id bigint not null,
   foreign key (state_id) references state(state_id)
   );
   
   create table address (
   address_id bigint primary key auto_increment,
   street varchar(25) not null,
   zip_code bigint,
   city_id bigint not null,
   foreign key(city_id) references city(city_id),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT Null ON UPDATE CURRENT_TIMESTAMP,
   created_by VARCHAR(255),
   archieved boolean default false
   );

    
create table roles (
role_id bigint primary key auto_increment,
role_name varchar(25) not null
);


create table animal_category_types (
category_id bigint primary key auto_increment,
category_name varchar(25) not null 
);


create table user (
user_id bigint primary key auto_increment,
first_name varchar(25) not null,
last_name varchar(25) not null,
username varchar(25) not null.
password varchar(20) not null,
archieved boolean default false,
address_id bigint not null,
role_id bigint not null,
foreign key(address_id) references address(address_id)
foreign key(role_id) references roles(role_id),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT Null  ON UPDATE CURRENT_TIMESTAMP,
);

create table zoo (
zoo_id bigint primary key AUTO_INCREMENT,
zoo_name varchar(25) not null,
address_id bigint not null,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT null ON UPDATE CURRENT_TIMESTAMP,
created_by VARCHAR(25) NOT NULL,
updated_by VARCHAR(25),
foreign key (address_id) references address (address_id)
);

create table animal (
animal_id bigint primary key AUTO_INCREMENT,
animal_name varchar(25) not null,
animal_type varchar(25) not null,
zoo_id bigint not null,
created_by VARCHAR(25) NOT NULL,
updated_by VARCHAR(25),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT null ON UPDATE CURRENT_TIMESTAMP,
foreign key (zoo_id) references zoo(zoo_id)

);	



    
    
    
    
    
    
    
    
    
    
    

    
