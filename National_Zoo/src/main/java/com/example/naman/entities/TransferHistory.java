package com.example.naman.entities;

import java.sql.Date;

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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "transfer_history")
public class TransferHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "id")
	private Long Id;
	
	
	@ManyToOne
	@JoinColumn(name="from_zoo")
	private Zoo fromZoo;
	
	@ManyToOne
	@JoinColumn(name="to_zoo")
	private Zoo toZoo;
	
	@ManyToOne
	@JoinColumn(name="animal_id")
	private Animal animalId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User userId;
	
	private Date date;
	
	
	
	
	
	

}
