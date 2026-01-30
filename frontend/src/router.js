import { createMemoryHistory, createRouter } from 'vue-router'

import HomeVue from './vues/home.vue'
import LoginVue from './vues/login.vue'
import SignupVue from './vues/signup.vue'
import ChatVue from './vues/chat.vue'

const routes = [
  { path: '/', component: HomeVue },
  { path: '/login', component: LoginVue },
  { path: '/signup', component: SignupVue },
  { path : '/chats', component: ChatVue, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createMemoryHistory(),
  routes,
})

router.beforeEach(async (to, from, next) => {
  if(!to.meta.requiresAuth){
    return next();
  }

  const token = localStorage.getItem('jwt')
  if(!token){
    return next('/login')
  }

  const res = await fetch('/api/auth/validate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      token: token
    })
  });
  const data = await res.json()
  if(data){
    return next();
  }

  localStorage.removeItem('jwt');
  return next('/login')

})

export default router;