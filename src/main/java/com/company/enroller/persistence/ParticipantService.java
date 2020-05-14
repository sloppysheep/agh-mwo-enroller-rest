package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {


//	DatabaseConnector connector;
	Session session;

	public ParticipantService() {
//		connector = DatabaseConnector.getInstance();
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Participant> getAll() {
//		return connector.getSession().createCriteria(Participant.class).list();
		return session.createCriteria(Participant.class).list();
	}

	public Participant findByLogin(String login) {
//		return (Participant) connector.getSession().get(Participant.class, login);
		return (Participant) session.get(Participant.class, login);
	}

	public Participant add(Participant participant) {
//		Transaction transaction = this.connector.getSession().beginTransaction();
//		connector.getSession().save(participant);
		Transaction transaction = this.session.beginTransaction();
		session.save(participant);
		transaction.commit();
		return participant;
	}

	public void delete(Participant participant) {
//		Transaction transaction = this.connector.getSession().beginTransaction();
//		connector.getSession().delete(participant);
		Transaction transaction = this.session.beginTransaction();
		session.delete(participant);
		transaction.commit();
		
	}
	
	public Participant update(Participant participant) {
//		Transaction transaction = this.connector.getSession().beginTransaction();
//		connector.getSession().update(participant);
		Transaction transaction = this.session.beginTransaction();
		session.update(participant);
		transaction.commit();
		return participant;
		
	}
}
