package com.pratik.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Collection;
import java.util.List;

@Entity
@SQLRestriction("is_deleted = 0")
public class Role extends Base {

	@Column(name="NAME")
    private String name;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
 
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_privilege", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    private List<Privilege> privileges;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}  

}