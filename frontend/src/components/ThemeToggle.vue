<template>
  <div>
    <button @click="next()" aria-label="Switch Theme Button" id="theme-toggle" type="button"
            class="transition-all duration-1000 inline-block dark:text-gray-400 text-slate-700 hover:bg-gray-200 dark:hover:bg-gray-800 rounded-lg p-1.5 m-2 absolute top-2 right-6">


      <svg id="theme-toggle-dark-icon" class="w-8 h-8"
           fill="currentColor"
           viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">

          <path v-if="mode === 'dark'" d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"/>
          <path v-if="mode === 'light'" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z"/>
          <path v-if="mode === 'auto'" d="M3.5 2A1.5 1.5 0 002 3.5V15a3 3 0 106 0V3.5A1.5 1.5 0 006.5 2h-3zm11.753 6.99L9.5 14.743V6.257l1.51-1.51a1.5 1.5 0 012.122 0l2.121 2.121a1.5 1.5 0 010 2.122zM8.364 18H16.5a1.5 1.5 0 001.5-1.5v-3a1.5 1.5 0 00-1.5-1.5h-2.136l-6 6zM5 16a1 1 0 100-2 1 1 0 000 2z"/>

      </svg>

    </button>
  </div>
</template>

<script setup lang="ts">
import {useColorMode, useCycleList} from '@vueuse/core'

const mode = useColorMode({
  emitAuto: true,
  selector: 'html',
  attribute: 'class',
})

const {next} = useCycleList(['dark', 'light', 'auto'], {initialValue: mode})
</script>

<style scoped>

</style>