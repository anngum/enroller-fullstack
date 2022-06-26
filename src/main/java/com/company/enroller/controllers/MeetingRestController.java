package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantOfMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Collection<Participant> participants = meetingService.getParticipants(id);
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity<String>("Unable to create, already exist", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/add", method = RequestMethod.PUT)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody String login) {
        Meeting foundMeeting = meetingService.findById(id);
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundMeeting == null || foundParticipant == null) {
            return new ResponseEntity<String>("Unable to add, meeting not exist or user not exist", HttpStatus.NOT_FOUND);
        }
        foundMeeting.addParticipant(foundParticipant);
        meetingService.update(foundMeeting);

        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") long id, @RequestBody String login) {
        Meeting foundMeeting = meetingService.findById(id);
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundMeeting == null || foundParticipant == null) {
            return new ResponseEntity<String>("Unable to add, meeting not exist or user not exist", HttpStatus.NOT_FOUND);
        }
        foundMeeting.removeParticipant(foundParticipant);
        meetingService.update(foundMeeting);

        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<String>("Unable to update, meeting not exist", HttpStatus.NOT_FOUND);
        }
        foundMeeting.setTitle(meeting.getTitle());
        foundMeeting.setDescription(meeting.getDescription());
        foundMeeting.setDate(meeting.getDate());
        meetingService.update(foundMeeting);

        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<String>("Unable to delete, not exist", HttpStatus.NOT_FOUND);
        }
        meetingService.delete(foundMeeting);

        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }
}
