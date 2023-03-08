import {computed, createApp} from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n'

import App from './App.vue'
import router from './router'

import './assets/main.css'
import "preline"
import {useFavicon, usePreferredDark} from "@vueuse/core";

import en from './locales/en.json'
import de from './locales/de-DE.json'

const i18n = createI18n({
    locale: 'en', // set locale
    //fallbackLocale: 'en', // set fallback locale
    messages: {en, de}, // set locale messages
    // If you need to specify other options, you can set other options
    // ...
})

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)

app.mount('#app')

const isDark = usePreferredDark()
const favicon = computed(() => isDark.value ? 'tic-tac-toe_light.ico' : 'tic-tac-toe_dark.ico')
useFavicon(favicon)
