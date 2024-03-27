<template>
  <div class="max-w-[50rem] flex flex-col  justify-center mx-auto my-auto w-full">

    <div class="text-center py-10 px-4 sm:px-6 lg:px-8">
      <h1 class="block text-7xl font-bold sm:text-9xl">{{hash}}</h1>
      <h1 class="block text-2xl font-bold-white"></h1>
      <p class="mt-6">{{msg}}</p>
      <div class="mt-5 flex flex-col justify-center items-center gap-2 sm:flex-row sm:gap-3">
        <router-link to="/start" class="btn btn-primary w-full sm:w-auto inline-flex justify-center items-center gap-x-3 text-center text-sm font-medium py-3 px-4" >
          Back to Start
        </router-link>
        <a href="https://github.com/nikita-t1/tictactoe/issues" target="_blank" class="btn btn-ghost w-full sm:w-auto inline-flex justify-center items-center gap-2 font-semibold transition-all text-sm py-3 px-4">
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

</style>
