<script setup lang="ts">
import { RouterView } from 'vue-router'
import ThemeToggle from "@/components/ThemeToggle.vue";
import NavBar from "@/components/NavBar.vue";
</script>

<template>
  <div class="transition-all duration-700 bg-gray-50 dark:bg-slate-900">
    <NavBar />
    <ThemeToggle />
    <RouterView />
  </div>
</template>

<style scoped>

</style>
