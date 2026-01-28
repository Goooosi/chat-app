<template>
    <div>
    <h1>Login</h1>
    <form @submit.prevent="login()">
        <div class="field">
            <label>Username</label>
            <input v-model="username" type="username" required />
        </div>
        <div class="field">
            <label>Password</label>
            <input v-model="password" type="password" required />
        </div>

        <button type="submit">Log in</button>
    </form>
    <h2>{{ msg }}</h2>

    <p class="create-account">Create Account</p>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const username = ref('')
const password = ref('')
let msg = ref('')
const router = useRouter()

async function login(){
    const res = fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body : JSON.stringify({
            username: username.value,
            password: password.value
        })
    })
    const data = await res.json()

    if(res.status === 200){
        localStorage.setItem('jwt', data['token'])
        router.push('/chats')
    } else {
        msg = 'Incorrect info'
    }

}


</script>