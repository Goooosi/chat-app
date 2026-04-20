<template>
    <div class="home">
        <h1>Welcome to Log-In</h1>
        <p></p>
        <RouterLink to="/login">Login</RouterLink>
        <RouterLink to="/signup">signup</RouterLink>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
const router = useRouter()
async function accessToken(){
    const res = await fetch('/api/auth/access', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body : JSON.stringify({})
        });
        if(res.status != 200){
            return await refreshToken()
        }
        return true;
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