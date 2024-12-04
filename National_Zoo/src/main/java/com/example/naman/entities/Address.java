package com.example.naman.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor



@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "address_id")
	private Long addressId;
	
	@Column(nullable = false)
	private String street;
	
	@Column(name = "zip_code")
	private Long zipCode;
		
	@ManyToOne
	@JoinColumn(name = "city_id", nullable = false)
	private City city;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;
	
	@Column(name = "updated_at", updatable = true)
	private String updatedAt;

	@Column(name = "created_by")
	private String createdBy;
	 
	private Boolean archieved;
	@PrePersist
	public void func() {
		archieved = false;
	}
	
}
