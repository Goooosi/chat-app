<template>
    <div>
        <h1>Sign Up</h1>
        <form @submit.prevent="signup()">
            <div>
                <label>Username</label>
                <input v-model="username" type="username" required />
            </div>
            <div>
                <label>Password</label>
                <input v-model="password" type="password" required />
            </div>
            <button type="submit">Sign up</button>
        </form>

        <h2>{{ msg }}</h2>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const username = ref('');
const password = ref('');
let msg = ref('')
const router = useRouter()

async function signup(){
  const res = await fetch('/api/auth/signup', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      username: username.value,
      password: password.value
    })
  })

  const data = await res.json()
  if(res.status === 200){
    localStorage.setItem('jwt', data['token'])
    router.push('/chats')
  } else {
    msg = 'Username is already taken.'
  }
}
</script>