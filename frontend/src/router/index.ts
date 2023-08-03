import {createRouter, createWebHistory} from 'vue-router'
import StartView from '@/views/StartView.vue'
import CreateNewGame from '@/views/CreateNewGame.vue'
import JoinExistingGame from '@/views/JoinExistingGame.vue'
import MultiplayerPlayView from '@/views/MultiplayerPlayView.vue'
import SingleplayerPlayView from '@/views/SingleplayerPlayView.vue'
import ErrorView from "@/views/ErrorView.vue";

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
            alias: "/",
            name: 'start',
            component: StartView,
        },
        {
            path: '/multiplayer/create-new-game',
            name: 'create-new-game',
            component: CreateNewGame,
        },
        {
            path: '/multiplayer/join-existing-game',
            name: 'join-existing-game',
            component: JoinExistingGame,
        },
        {
            path: '/multiplayer/play',
            name: 'multiplayer-play',
            component: MultiplayerPlayView
        },
        {
            path: '/singleplayer/play',
            name: 'singleplayer-play',
            component: SingleplayerPlayView
        },
        {
            path: '/error',
            name: 'error',
            component: ErrorView
        }
    ]
})

export default router
