import { createRouter, createWebHistory } from 'vue-router'

import HomeVue from './vues/home.vue'
import LoginVue from './vues/login.vue'
import SignupVue from './vues/signup.vue'
import MainVue from './vues/main.vue'


const routes = [
  { path: '/', component: HomeVue },
  { path: '/login', component: LoginVue },
  { path: '/signup', component: SignupVue },
  { path : '/main', component: MainVue }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router;