<template>
    <div v-if="!isPepe">
        <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" fill="currentColor" viewBox="0 0 1024 1024">
            <path
                d="M512 85.333333a426.666667 426.666667 0 1 0 426.666667 426.666667A426.666667 426.666667 0 0 0 512 85.333333z m0 768a341.333333 341.333333 0 1 1 341.333333-341.333333 341.333333 341.333333 0 0 1-341.333333 341.333333z"/>
        </svg>
    </div>
    <div v-else>
        <img src="/pepeno.png" alt="">
    </div>

</template>
<script setup lang="ts">
import {useStorage} from "@vueuse/core";

const isPepe = useStorage('isPepe', false)


</script>
