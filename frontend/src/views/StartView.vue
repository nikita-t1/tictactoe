<template>
  <div class="flex flex-col md:flex-row max-w-4xl content-center self-center items-center justify-center mx-auto space-x-0 md:space-x-8 space-y-6 md:space-y-0 h-screen">

    <div class="min-h-[14rem] transition-all duration-700 flex flex-col w-96 bg-white border shadow-sm rounded-xl p-4 md:p-5 dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
      <h3 class="transition-all duration-700 text-md font-bold text-gray-800 dark:text-white">
          {{t("player")}} 1
      </h3>
      <p class=" grow transition-all duration-700 mt-6 text-gray-800 dark:text-gray-400">
          {{t("create_new_game_desc")}}
      </p>
      <router-link to="/create-new-game" class="flex-none mt-10 mx-auto w-full inline-flex items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
        <button type="button" class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-600 hover:bg-blue-700 text-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
            {{t("create_new_game")}}
        </button>
      </router-link>
    </div>

    <div class="min-h-[14rem] transition-all duration-700 flex flex-col w-96 bg-white border shadow-sm rounded-xl p-4 md:p-5 dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
      <h3 class="transition-all duration-700 text-md font-bold text-gray-800 dark:text-white">
          {{t("player")}} 2
      </h3>
      <p class="grow transition-all duration-700 mt-6 text-gray-800 dark:text-gray-400">
          {{t("join_existing_game_desc")}}
      </p>
      <router-link to="/join-existing-game" class="flex-none mx-auto w-full inline-flex items-center gap-2 mt-10  text-sm font-medium text-blue-500 hover:text-blue-700">
        <button type="button" class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
            {{ t("join_existing_game")}}
        </button>
      </router-link>
    </div>

  </div>
</template>

<script setup lang="ts">

import {useI18n} from "vue-i18n";

const { t } = useI18n()

</script>

<style scoped>

</style>
