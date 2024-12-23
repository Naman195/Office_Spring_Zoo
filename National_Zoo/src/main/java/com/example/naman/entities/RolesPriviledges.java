package com.example.naman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "roles_priviledges")
public class RolesPriviledges {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "id")
	private Long Id;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Roles roles;
	
	
	@ManyToOne
	@JoinColumn(name = "priviledge_id")
	private Priviledges priviledges;
	
}

//create table priviledges (
//		id bigint primary key AUTO_INCREMENT,
//	    priviledges varchar(100) not null );
//	    
//
//	create table roles_priviledges (
//		id bigint primary key AUTO_INCREMENT,
//	    role_id bigint not null,
//	    priviledge_id bigint not null,
//	    foreign key (role_id) references roles(id)
//	    );
//	    
//	select * from roles;
//
//	insert into priviledges ( priviledges ) values ( "create" ), ( "update" ), ("delete");
//
//	select *  from priviledges;
