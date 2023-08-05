<script setup lang="ts">

const props = defineProps({
    text: {
        type: String,
        required: true
    }
})

</script>

<template>
    <span class="inline-flex items-center gap-1.5 py-1 px-2 rounded-md text-xs font-medium bg-gray-200 dark:bg-gray-800 dark:text-gray-200 text-gray-800"><code>{{text}}</code></span>
</template>

<style scoped>

</style>
