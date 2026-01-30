<template>
    <div>
        <h1>hi uhh you logged in</h1>
        <button @submit.prevent="logout()">Log out</button>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter;
function logout() {
    localStorage.removeItem('jwt');
    router.push('jwt');
}

</script>