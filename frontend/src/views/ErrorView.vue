<template>
  <div class="max-w-[50rem] flex flex-col  justify-center mx-auto my-auto w-full h-screen">

    <div class="text-center py-10 px-4 sm:px-6 lg:px-8">
      <h1 class="block text-7xl font-bold text-gray-800 sm:text-9xl dark:text-white">{{hash}}</h1>
      <h1 class="block text-2xl font-bold text-white"></h1>
      <p class="mt-6 text-gray-600 dark:text-gray-400">{{msg}}</p>
      <div class="mt-5 flex flex-col justify-center items-center gap-2 sm:flex-row sm:gap-3">
        <router-link to="/start" class="w-full sm:w-auto inline-flex justify-center items-center gap-x-3 text-center bg-blue-600 hover:bg-blue-700 border border-transparent text-white text-sm font-medium rounded-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white transition py-3 px-4 dark:focus:ring-offset-gray-800" >
          Back to Start
        </router-link>
        <a href="https://github.com/nikita-t1/tictactoe/issues" target="_blank" class="w-full sm:w-auto inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold text-blue-500 hover:text-blue-700 focus:outline-none focus:ring-2 ring-offset-white focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm py-3 px-4 dark:ring-offset-slate-900">
          Report Issue
        </a>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">

import {ErrorCodes} from "@/StatusCodes";
import {useI18n} from "vue-i18n";

const {t} = useI18n()


const hash = parseInt(location.hash.slice(1)) || ErrorCodes.GENERIC_ERROR
const msg = t("error_msg." + hash)

</script>

<style scoped>
div, h1, p, a{
  @apply transition-all duration-700
}
</style>
