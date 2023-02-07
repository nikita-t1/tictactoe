<script setup lang="ts">
import { RouterView } from 'vue-router'
import ThemeToggle from "@/components/ThemeToggle.vue";
</script>

<template>
  <div class="transition-all duration-700 bg-gray-50 dark:bg-slate-900">
    <ThemeToggle />
    <RouterView />
  </div>
</template>

<style scoped>

</style>
