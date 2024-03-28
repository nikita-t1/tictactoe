<script setup lang="ts">

const props = defineProps({
    text: {
        type: String,
        required: true
    }
})

</script>

<template>
    <span class="kbd inline-flex items-center gap-1.5 py-1 px-2 rounded-md text-xs font-medium "><code>{{text}}</code></span>
</template>

<style scoped>

</style>
