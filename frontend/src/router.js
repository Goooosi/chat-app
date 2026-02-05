import { createMemoryHistory, createRouter } from 'vue-router'

import HomeVue from './vues/home.vue'
import LoginVue from './vues/login.vue'
import SignupVue from './vues/signup.vue'
import ChatVue from './vues/chat.vue'

const routes = [
  { path: '/', component: HomeVue },
  { path: '/login', component: LoginVue },
  { path: '/signup', component: SignupVue },
  { path : '/chats', component: ChatVue }
]

const router = createRouter({
  history: createMemoryHistory(),
  routes,
})

export default router;