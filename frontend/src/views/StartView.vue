<template>
  <div class="flex max-w-4xl content-center self-center items-center justify-center mx-auto space-x-8 h-screen">
    <div class="flex flex-col bg-white border shadow-sm rounded-xl p-4 md:p-5 dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
      <h3 class="text-md font-bold text-gray-800 dark:text-white">
        Player 1
      </h3>
      <p class="mt-2 text-gray-800 dark:text-gray-400">
        Create a Game-Code to start a new Session
      </p>
      <router-link to="/create-new-game" class="flex-none  mt-3 mx-auto w-full inline-flex items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
        <button type="button" class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
          Create New Game
        </button>
      </router-link>
    </div>

    <div class="flex flex-col bg-white border shadow-sm rounded-xl p-4 md:p-5 dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
      <h3 class="text-md font-bold text-gray-800 dark:text-white">
        Player 2
      </h3>
      <p class="mt-2 text-gray-800 dark:text-gray-400">
        Enter an already existing Game-Code
      </p>
      <router-link to="/join-existing-game" class="flex-none mt-3 mx-auto w-full inline-flex items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
        <button type="button" class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
          Join Existing Game
        </button>
      </router-link>
    </div>

  </div>
</template>

<script setup lang="ts">

</script>

<style scoped>

</style>