package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = session.createQuery(hql);
		return query.list();
	}

	public Meeting findById(long id) {
		return (Meeting) session.get(Meeting.class, id);
	}
	
	public Meeting add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Meeting update(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.update(meeting);
		transaction.commit();
		return meeting;
	}
	
	public void delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}
	
	public  Collection<Meeting> findByTitleOrDescriptionOrFindByUser(String stringToFind, String login) {
		if (stringToFind == null) {stringToFind = "";} // in order to be able to get all meetings
		if (login == null) {login = "";} 
		// those queries still need to be re-written into one query
		if (login != "") {
			Query query = session.createQuery("select distinct m from Meeting m join m.participants p where p.login like :value");
			query.setParameter("value", "%" + login + "%");
			return query.list();
		}
		else {
			Query query = session.createQuery("from Meeting m where m.description like :value or m.title like :value");
			query.setParameter("value", "%" + stringToFind + "%");
			return query.list();
		}
//		Query query = session.createQuery("select distinct m from Meeting m where m.description like :value2 or m.title like :value2 and m in (select m from m join m.participants p where p.login like :value1) order by m.title");
//		query.setParameter("value1", "%" + login + "%");
//		query.setParameter("value2", "%" + stringToFind + "%");
//		return query.list();
	}
	
//	public  Collection<Meeting> findByTitleOrDescription(String stringToFind) {
//		if (stringToFind == null) {stringToFind = "";} // in order to be able to get all meetings
//		Query query = session.createQuery("from Meeting m where m.description like :value or m.title like :value");
//		query.setParameter("value", "%" + stringToFind + "%");
//		return query.list();
//	}
//	
//	public  Collection<Meeting> findByLogin(String login) {
//		if (login == null) {login = "";} // in order to be able to get all meetings
//		Query query = session.createQuery("select m from Meeting m join m.participants p where p.login like :value");
//		query.setParameter("value", "%" + login + "%");
//		return query.list();
//	}
}
