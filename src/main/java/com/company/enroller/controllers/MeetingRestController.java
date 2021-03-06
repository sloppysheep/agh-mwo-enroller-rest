package com.company.enroller.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController

@RequestMapping("/meetings")

public class MeetingRestController {

	private MeetingService meetingService;
	private ParticipantService participantService;

	@Autowired
	public MeetingRestController(MeetingService meetingService, ParticipantService participantService) {
		this.meetingService = meetingService;
		this.participantService = participantService;
	}
	
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ResponseEntity<?> getMeetings() {
//		Collection<Meeting> meetings = meetingService.getAll();
//		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(meeting.getId());
		if (foundMeeting != null) {
			return new ResponseEntity(
					"Unable to create. A meeting with id " + meeting.getId() + " already exist.",
					HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	//ADVANCED
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
	

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @RequestBody Participant participant) {
		Meeting meeting = meetingService.findById(id);
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		
		if (meeting == null) {
			return new ResponseEntity("Unable to add the participant to a non-existing meeting. A meeting with an id " + id + " doesn't exist.", HttpStatus.NOT_FOUND);
		}
		if (foundParticipant == null) {
			return new ResponseEntity("Unable to add the participant to the meeting. A participant with login " + participant.getLogin() + " doesn't exist.", HttpStatus.NOT_FOUND);
		}
		for(Participant p1 : meeting.getParticipants()) {
			if (p1 == participant) {
				return new ResponseEntity(
						"Unable to create. A participant with login " + participant.getLogin() + " already exist.",
						HttpStatus.CONFLICT);
			}
		}
		meeting.addParticipant(foundParticipant);
		meeting = meetingService.update(meeting);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
	
	//GOLD
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delMeeting(@PathVariable("id") long id) {
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity("Unable to delete non-existant meeting. Meeting with id: " + id + " does not exist", HttpStatus.NOT_FOUND);
		}
		meetingService.delete(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity("Meeting with id: " + id + " does not exist", HttpStatus.NOT_FOUND);
		}

		foundMeeting.setDate(meeting.getDate());
		foundMeeting.setDescription(meeting.getDescription());
		foundMeeting.setTitle(meeting.getTitle());
		meeting = meetingService.update(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") long id, @PathVariable("login") String login)  {
		Meeting foundMeeting = meetingService.findById(id);
		Participant foundParticipant = participantService.findByLogin(login);
		if (foundMeeting == null) {
			return new ResponseEntity("Meeting with id: " + id + " does not exist", HttpStatus.NOT_FOUND);
		}
		if (foundParticipant == null) {
			return new ResponseEntity("Participant with login: " + login + " does not exist", HttpStatus.NOT_FOUND);
		}
		foundMeeting.removeParticipant(foundParticipant);
		foundMeeting = meetingService.update(foundMeeting);
		return new ResponseEntity<Collection<Participant>>(foundMeeting.getParticipants(), HttpStatus.OK);
	}
	
	//PREMIUM
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> findByString(@RequestParam(value = "find", required = false) String toBeFound, @RequestParam(value = "participant", required = false) String login) {
		Collection<Meeting> meetings = meetingService.findByTitleOrDescriptionOrFindByUser(toBeFound, login);
//		if (meetings.size() == 0) {
//			return new ResponseEntity(HttpStatus.NOT_FOUND);
//		}
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ResponseEntity<?> findByParticipant(@RequestParam(value = "participant") String login) {
//		Collection<Meeting> meetings = meetingService.findByLogin(login);
//		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
//	}
}