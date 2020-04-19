package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {


	DatabaseConnector connector;
//	Session session;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
//		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Participant> getAll() {
		return connector.getSession().createCriteria(Participant.class).list();
//		return session.createCriteria(Participant.class).list();
	}

	public Participant findByLogin(String login) {
		return (Participant) connector.getSession().get(Participant.class, login);
	}

	public Participant add(Participant participant) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return participant;
	}

	public void delete(Participant participant) {
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
		
	}
	
}
