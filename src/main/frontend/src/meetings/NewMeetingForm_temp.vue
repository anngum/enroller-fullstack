<template>
  <div>
    <form @submit.prevent="addNewMeeting()" v-if="adding">
      <h3>Dodaj nowe spotkanie</h3>
      <label>Nazwa</label>
      <input type="text" v-model="newMeeting.title">
      <label>Opis</label>
      <textarea v-model="newMeeting.description"></textarea>
      <button>Dodaj</button>
      <span class="error" v-if="error">Spotkanie musi mieć nazwę!</span>
    </form>
    <button @click="adding = true" v-else>Dodaj nowe spotkanie</button>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                newMeeting: {participants: []},
				
                adding: false,
                error: false
            };
        },
        methods: {
            addNewMeeting() {
                this.error = false;
				console.log("Nowe spotkanie z formularza");
				console.log(this.newMeeting);
                if (this.newMeeting.title) {
                    this.$emit('added', this.newMeeting);
                    //this.newMeeting = {participants: []};
					
					this.$http.post('meetings/add', this.newMeeting)
					.then(() => {
                        this.success('Utworzono spotkanie.');
                        this.adding = false;
                    })
                    .catch(response => this.failure('Błąd przy tworzeniu spotkania. Kod odpowiedzi: ' + response.status));
                    
                } else {
                    this.error = true;
                }
            },
			success(message) {
                this.message = message;
                this.isError = false;
            },
            failure(message) {
                this.message = message;
                this.isError = true;
            },
            clearMessage() {
                this.message = undefined;
            }
        }
    }
</script>

<style scoped>
  .error {
    color: #F00;
  }
</style>
