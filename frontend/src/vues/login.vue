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

    <RouterLink to="/signup">Sign up</RouterLink>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
const username = ref('')
const password = ref('')
let msg = ref('')
const router = useRouter()


async function login(){
    const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body : JSON.stringify({
            username: username.value,
            password: password.value,
            role: 'USER'
        })
    })

    if(res.status === 200){
        router.push('/main')
    } else {
        msg.value = 'Incorrect info'
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