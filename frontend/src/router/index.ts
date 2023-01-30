import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/start',
      name: 'start',
      component: () => import('../views/StartView.vue')
    },
    {
      path: '/create-new-game',
      name: 'create-new-game',
      component: () => import('../views/CreateNewGame.vue')
    },
    {
      path: '/join-existing-game',
      name: 'join-existing-game',
      component: () => import('../views/JoinExistingGame.vue')
    }
  ]
})

export default router
