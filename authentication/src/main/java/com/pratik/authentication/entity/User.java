package com.pratik.authentication.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Entity
@Table(name="user")
@SQLRestriction("is_deleted = 0")
public class User  extends Base   {

	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;

	@ManyToMany
    @JoinTable( 
        name = "user_role", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}