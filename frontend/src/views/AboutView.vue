<template>
    <div class="max-w-[50rem] flex flex-col justify-center mx-auto w-full">

        <div class="text-center py-10 px-4 sm:px-6 lg:px-8">
            <div class="inline-flex text-center justify-center items-end gap-4">
                <img class="mx-auto h-12 w-auto transition-all duration-700" :src="favicon" alt="TicTacToe">
                <h1 class="block text-6xl font-bold text-gray-800 dark:text-white">TicTacToe</h1>
            </div>

            <div class="mt-5 flex flex-col justify-center items-center gap-2 sm:flex-row sm:gap-3">
<!--                <router-link to="/start" class="w-full sm:w-auto inline-flex justify-center items-center gap-x-3 text-center bg-blue-600 hover:bg-blue-700 border border-transparent text-white text-sm font-medium rounded-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white transition py-3 px-4 dark:focus:ring-offset-gray-800" >-->
<!--                    Back to Start-->
<!--                </router-link>-->
<!--                <div class="inline-flex align-bottom w-full justify-center items-center text-blue-500 py-1 px-4">-->
<!--                    <a href="https://github.com/nikita-t1/tictactoe/issues" target="_blank" class="hover:text-blue-700 sm:w-auto my-auto rounded-md border border-transparent font-semibold focus:outline-none focus:ring-2 ring-offset-white focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm px-1 dark:ring-offset-slate-900">-->
<!--                        GitHub-->
<!--                    </a>-->
<!--                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"><path fill="currentColor" d="M19 19H5V5h7V3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14c1.1 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83l1.41 1.41L19 6.41V10h2V3h-7z"/></svg>-->
<!--                </div>-->
            </div>

            <div class="text-start">
                <h3 class="mt-10 text-2xl font-bold text-gray-800 dark:text-white">{{ t("overview") }}</h3>
                <p class="mt-2 text-gray-600 dark:text-gray-400">{{ t("overview_desc") }}</p>
            </div>

            <div class="text-start">
                <h3 class="mt-10 text-2xl font-bold text-gray-800 dark:text-white">{{ t("motivation") }}</h3>
                <p class="mt-2 text-gray-600 dark:text-gray-400">{{ t("motivation_desc") }}</p>
                <ul class="list-disc list-inside mt-2 text-gray-600 dark:text-gray-400">
                    <li>
                        {{ t("first_time_using") }} <KBD text="WebSockets"/>
                    </li>
                    <li>{{ t("getting_more_familiar_with") }} <KBD text="Vue.js"/></li>
                    <li>{{ t("getting_more_familiar_with") }} <KBD text="TailwindCSS"/> {{ t("and") }} <KBD text="CSS"/> {{ t("in_general") }}</li>
                </ul>
            </div>

        </div>

    </div>
</template>

<style>

div, h1, p, a{
    @apply transition-all duration-700
}
</style>
<script setup lang="ts">
import {computed} from "vue";
import {useDark} from "@vueuse/core";
import KBD from "@/components/KBD.vue";
import {useI18n} from "vue-i18n";

const {t} = useI18n()

const isDark = useDark()
const favicon = computed(() => isDark.value ? '/tic-tac-toe_light.ico' : '/tic-tac-toe_dark.ico')

</script>
