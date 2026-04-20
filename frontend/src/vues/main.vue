<template>
    <div>
        <p>You are logged in</p>
        <button @click="logout">Log out</button>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
let refreshInt = null
async function logout() {
    const res = await fetch('/api/auth/logout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body : JSON.stringify({})
    })
;
    if(res.status === 200){
        router.push('/');
    }


}
onMounted(async () => {
    if(!(await accessToken())) return
    refreshInt = setInterval(async () => {
        await refreshToken()
    }, 9 * 60 * 1000)

})
onUnmounted(() => {
    clearInterval(refreshInt)
})
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
async function accessToken(){
    const res = await fetch('/api/auth/access', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body : JSON.stringify({})
        });
        if(res.status != 200){
            return await refreshToken();
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
        router.push('/');
        return false;
    }
    return true;
}




</script>