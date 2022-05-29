<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
	<button @click="registering=false" :class="registering ? 'button button-clear' : ''" > Zaloguj </button>
	<button  @click="registering=true" :class="registering ? '' : 'button button-clear'" > Zarejestruj </button>
      <login-form @login="login($event)" v-if="registering==false"></login-form>
	  <login-form @login="register($event)" v-else button-label="Zarejestruj się"></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
				registering: false,
                authenticatedUsername: ""
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
            logout() {
                this.authenticatedUsername = '';
            },
			register(user) {
				this.$http.post('participants', user)
				.then(response => {
				// udało się
				})
				.catch(response => {
				// nie udało sie     
			});
			
			}
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .logo {
    vertical-align: middle;
  }
</style>

