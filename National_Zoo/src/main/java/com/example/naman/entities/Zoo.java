package com.example.naman.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "zoo")
@EntityListeners(AuditingEntityListener.class)
public class Zoo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "zoo_id")
	private Long zooId;
	
	@Column(name = "zoo_name", nullable = false)
	private String zooName;
	
	@OneToOne(cascade = CascadeType.ALL) 	
	@JoinColumn(name = "address_id", nullable = true)
	private Address address;
	
	private boolean archieved;
	
	
	
	
	@CreationTimestamp
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private Instant updatedAt;
	
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String createdBy;
	
	@LastModifiedBy
	@Column(name = "updated_by", nullable = true, updatable = true)
	private String updatedBy;
	
	private String image;
	
	@PrePersist
	public void func() {
		updatedBy = null;
	}
	
}
