import {computed, createApp} from 'vue'
import {createPinia} from 'pinia'

import App from './App.vue'
import router from './router'

import './assets/main.css'
import {useFavicon, usePreferredDark} from "@vueuse/core";
import i18n from "@/plugins/i18n";

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)

app.mount('#app')

const isDark = usePreferredDark()
//TODO: make the favicon change based on the daisyUI theme
const favicon = computed(() => isDark.value ? 'tic-tac-toe_light.ico' : 'tic-tac-toe_dark.ico')
useFavicon(favicon)
