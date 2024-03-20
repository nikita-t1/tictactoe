<script setup lang="ts">
import { RouterView } from 'vue-router'
import ThemeToggle from "@/components/ThemeToggle.vue";
import NavBar from "@/components/NavBar.vue";
</script>

<template>
  <div class="transition-all duration-700 h-screen flex flex-col">
    <NavBar />
    <ThemeToggle />
    <RouterView class="grow"/>
  </div>
</template>

<style scoped>

</style>
