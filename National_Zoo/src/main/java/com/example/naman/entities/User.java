package com.example.naman.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.naman.annotations.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "user_id")
	private Long userId;
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "username", nullable = false, unique = true)
	private String userName;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "role_id")
	private Roles role;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;
	
	@Transient
	private  Collection<? extends GrantedAuthority> authority;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;
	
	private boolean archieved;
	
	private String image;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private RefreshToken refreshToken;
	
	@LastModifiedBy
	@Column(name = "updated_by", nullable = true, updatable = true)
	private String updatedBy;

	@PrePersist
	public void func() {
		updatedBy = null;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		System.out.println("Hello" +  role.getRole());
//		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		return authority;
	}

	@Override
	public String getUsername() {
		
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled() 
	{
		return true;
	}
	
	
	
	
	
}
