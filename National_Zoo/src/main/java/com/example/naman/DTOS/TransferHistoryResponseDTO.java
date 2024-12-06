package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistoryResponseDTO {
	
	private Long id;
    private String animalName;
    private String fromZooName;
    private String toZooName;
    private String userName;
    private String date;
}


//create table transfer_history(
//		
//		id bigint primary key auto_increment,
//	    from_zoo bigint not null,
//	    to_zoo bigint not null,
//	    animal_id bigint not null,
//	    username varchar(100) not null,
//		date timestamp DEFAULT current_timestamp,
//	    foreign key (from_zoo) references zoo(zoo_id),
//	    foreign key(to_zoo) references zoo(zoo_id),
//	    foreign key (animal_id) references animal(animal_id),
//	    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//	    created_by VARCHAR(25) NOT NULL
//	    
//	    );
//	    
//	    desc transfer_history;
//	    
//	    Drop table transfer_history;