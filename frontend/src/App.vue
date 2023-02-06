<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import ThemeToggle from "@/components/ThemeToggle.vue";
</script>

<template>
  <div class="bg-gray-700">
    <ThemeToggle />
    <RouterView />
  </div>
</template>

<style scoped>

</style>
