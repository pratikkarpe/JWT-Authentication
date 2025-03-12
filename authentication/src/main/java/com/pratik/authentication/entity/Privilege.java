package com.pratik.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLRestriction;

import java.util.Collection;

@Entity
@SQLRestriction("is_deleted = 0")
public class Privilege extends Base{

	@Column(name="NAME")
	private String name;

	@JsonIgnore
    @ManyToMany(mappedBy = "privileges")
	@NotFound(action= NotFoundAction.IGNORE)
    private Collection<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
    
    
}