package com.example.naman.entities;

import java.sql.Date;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
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
	
//	@ManyToOne
//	@JoinColumn(name="username")
	@Column(name = "username", unique = true)
	private String userName;
	
	private Date date;
	
	@CreationTimestamp
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;
	
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String createdBy;
	
	
}
