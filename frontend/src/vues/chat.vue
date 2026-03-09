<template>
    <div id="chat-window">

        <input type="text" name="message" id="message-box" v-model="message">
        <button @submit.prevent="sent_message" type="submit">send</button>
    </div>
</template>

<script setup>
import { Client } from '@stomp/stompjs';
import { onMounted, ref } from 'vue';
let stompclient = null
const username = localStorage.getItem('username')
onMounted(() => {
    const token = localStorage.getItem('jwt');
    console.log("WS token:", token)
    stompclient = new Client({
        brokerURL: "/websocket",
        reconnectDelay: 3000,
        debug: msg => console.log(msg),
    })
    stompclient.connectHeaders = {Authorization: "Bearer " + token}
    stompclient.onConnect = () => {stompclient.subscribe('/user/queue/private', (res) => {const msg = JSON.parse(res.body); console.log(msg)} )}
    stompclient.onStompError = frame => { console.error("Broker error:", frame.headers["message"]); console.error("Details:", frame.body); 
    stompclient.onWebSocketError = event => { console.error("WebSocket error:", event); }
    };
    stompclient.activate()
})

const message = ref("")
function sent_message(){
    if(!message.value.trim()) {return}
    stompclient.publish({
        destination: "/app/private-message",
        body: JSON.stringify({
            "senderUser": username,
            "receiverUser": "bob",
            "type": "PRIVATE",
            "message": "Hi bob"
        })
    })
}


</script>