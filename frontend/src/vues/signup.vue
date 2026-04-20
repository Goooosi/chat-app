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
        <RouterLink to="/login">Log in</RouterLink>
        <h2>{{ msg }}</h2>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { RouterLink ,useRouter } from 'vue-router';
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
      password: password.value,
      role: 'USER'
    })
  })
  if(res.status === 200){
    router.push('/main')
  } else {
    msg.value = 'Username is already taken.'
  }
}

async function accessToken(){
    const res = await fetch('/api/auth/access', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body : JSON.stringify({})
        });
        if(res.status != 200){
            return await refreshToken()
        }
        return true
}
async function refreshToken(){
    const res = await fetch('/api/auth/refresh', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body : JSON.stringify({})
    });
    if(res.status != 200){

        return false;
    }
    return true;
}

onMounted(async () => {
    if(await accessToken()) {
        router.push('/main')
    }
})
</script>