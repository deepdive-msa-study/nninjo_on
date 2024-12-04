package com.nninjoon.userservice.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false, updatable = false)
	private String userId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	@Column(name = "is_deleted")
	private LocalDateTime isDeleted;

	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public User(String name, String userId, String email, String password, String role) {
		this.name = name;
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.role = (role != null) ? role : "ROLE_USER";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getUsername() {
		return "";
	}

	public static User create(String name, String userId, String email, String password){
		return User.builder()
			.name(name)
			.userId(userId)
			.email(email)
			.password(password)
			.build();
	}

	public void updateName(String name){
		this.name = name;
	}

	public void updateEmail(String email){
		this.email = email;
	}

	public void delete(){
		this.isDeleted = LocalDateTime.now();
	}
}