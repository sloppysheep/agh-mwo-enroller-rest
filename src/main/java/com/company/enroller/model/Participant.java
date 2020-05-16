package com.company.enroller.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "participant")
public class Participant {
	
	@Id
	private String login;

	@Column
	private String password;
	
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "meeting_participant", joinColumns = { @JoinColumn(name = "participant_login") }, inverseJoinColumns = {
			@JoinColumn(name = "meeting_id") })
	Set<Meeting> meetings = new HashSet<>();
//	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
//	private Set<Meeting> meetings;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Collection<Meeting> getMeetings() {
		return meetings;
	}
}
