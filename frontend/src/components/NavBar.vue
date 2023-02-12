<template>
  <div class="absolute">
    <header class="relative flex justify-start flex-nowrap z-50 w-full text-sm py-4">
      <nav class="max-w-[85rem] w-full px-4 flex" aria-label="Global">
          <div class="flex gap-5 mt-5 flex-row mt-0 pl-5">
            <router-link class="" to="/">
              <button type="button" class="transition-all duration-700 py-[.688rem] px-4 inline-flex justify-center items-center gap-2 rounded-md border-2 border-gray-200 font-semibold text-blue-600 hover:text-white hover:bg-blue-600 hover:border-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:border-gray-700 dark:hover:border-blue-600">
                <MaterialSymbolsHomeOutlineRounded/>
                Home
              </button>
            </router-link>
            <router-link class="" to="/about">
              <button type="button" class="transition-all duration-700 py-[.688rem] px-4 inline-flex justify-center items-center gap-2 rounded-md border-2 border-gray-200 font-semibold text-blue-600 hover:text-white hover:bg-blue-600 hover:border-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:border-gray-700 dark:hover:border-blue-600">
                <UilCommentInfoAlt />
                About
              </button>
            </router-link>
          </div>

      </nav>
    </header>
  </div>
</template>

<script setup lang="ts">
import MaterialSymbolsHomeOutlineRounded from '~icons/material-symbols/home-outline-rounded'
import UilCommentInfoAlt from '~icons/uil/comment-info-alt'

</script>

<style scoped>


</style>