<template>
    <div class="max-w-[50rem] flex flex-col justify-center mx-auto w-full">

        <div class="text-center py-10 px-4 sm:px-6 lg:px-8">
            <div class="flex text-center justify-center items-center gap-10">
                <IconLogo class="h-20 w-auto"/>
                <h1 class="h-12 text-6xl font-bold">TicTacToe</h1>
            </div>

            <div class="text-start">
                <h3 class="mt-10 text-2xl font-bold">{{ t("overview") }}</h3>
                <p class="mt-2">{{ t("overview_desc") }}</p>
            </div>

            <div class="text-start">
                <h3 class="mt-10 text-2xl font-bold">{{ t("motivation") }}</h3>
                <p class="mt-2">{{ t("motivation_desc") }}</p>
                <ul class="list-disc list-inside mt-2">
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

* {
    @apply transition-all duration-700
}
</style>
<script setup lang="ts">
import {computed} from "vue";
import {useDark} from "@vueuse/core";
import KBD from "@/components/KBD.vue";
import {useI18n} from "vue-i18n";
import IconLogo from "@/components/icons/IconLogo.vue";

const {t} = useI18n()

const isDark = useDark()
const favicon = computed(() => isDark.value ? '/tic-tac-toe_light.ico' : '/tic-tac-toe_dark.ico')

</script>
