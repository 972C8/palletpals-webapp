/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class User {

	@Id
	@GeneratedValue
	private Long id;
	@NotEmpty(message = "Please provide a name.")
	private String userName;
	@Email(message = "Please provide a valid e-mail.")
	@NotEmpty(message = "Please provide an e-mail.")
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // only create object property from JSON
	private String password;
	//@NotEmpty
	@JsonIgnore
	private UserType role;
	@Transient // will not be stored in DB
	private String remember;
	private Language language;
	private Appearance appearance;
	@OneToOne
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private ShippingAddress address;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		String transientPassword = this.password;
		this.password = null;
		return transientPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemember() {
		return remember;
	}

	public void setRemember(String remember) {
		this.remember = remember;
	}

	public void setRole(UserType role){ this.role = role;}

	public UserType getRole() {
		return role;
	}
}
