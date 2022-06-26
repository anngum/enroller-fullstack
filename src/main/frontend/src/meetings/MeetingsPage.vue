<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
				   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
			this.getMeetings();
            return {
                meetings: []
            };
        },
        methods: {
            addNewMeeting(meeting) {
                this.meetings.push(meeting);
            },
            addMeetingParticipant(meeting) {
			this.addParticipantToMeeting(meeting)
                meeting.participants.push(this.username);
            },
            removeMeetingParticipant(meeting) {
			this.deleteParticipantFromMeeting(this.username);
                meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
            },
            deleteMeeting(meeting) {
			this.delMeeting(meeting);
                this.meetings.splice(this.meetings.indexOf(meeting), 1);
            },
			
			getMeetings(){
				var x = this.$http.get('meetings')
				.then(response => {
                        this.meetings = response.body;
						this.meetings.forEach(meeting=>this.loadParticipants(meeting));
                    });
				console.log(x);
				return x;
			
			},
			
			loadParticipants(meeting){
			var x = this.$http.get('meetings/'+meeting.id+'/participants')
			.then(response => {
                        meeting.participants = response.body;
						
                    });
			
			},
			
			addParticipantToMeeting(meeting){
			// "/{id}/add" PUT
			this.$http.put('meetings/'+meeting.id+'/add');
			},
			
			deleteParticipantFromMeeting(username){
			// "/{id}/delete"
			this.$http.delete('meetings/'+username.id+'/delete');
			},
			
			delMeeting(meeting){
			//"/{id}" DELETE
			this.$http.delete('meetings/'+meeting.id);
			}
        }
    }
</script>
