<template>
    <div class="inline-block rounded-lg m-2 absolute top-2 right-6">
        <div class="drawer drawer-end ">
            <input id="my-drawer-4" type="checkbox" class="drawer-toggle" />
            <div class="drawer-content " @click.right="pepe" @contextmenu.prevent>
                <!-- Page content here -->
                <label for="my-drawer-4" class="bg-base-100 drawer-button btn btn-ghost text-primary">
                    <svg id="theme-toggle-dark-icon" class="w-8 h-8"
                         fill="currentColor"
                         viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                        <path d="M3.5 2A1.5 1.5 0 002 3.5V15a3 3 0 106 0V3.5A1.5 1.5 0 006.5 2h-3zm11.753 6.99L9.5 14.743V6.257l1.51-1.51a1.5 1.5 0 012.122 0l2.121 2.121a1.5 1.5 0 010 2.122zM8.364 18H16.5a1.5 1.5 0 001.5-1.5v-3a1.5 1.5 0 00-1.5-1.5h-2.136l-6 6zM5 16a1 1 0 100-2 1 1 0 000 2z"/>
                    </svg>
                </label>
            </div>
            <div class="drawer-side">
                <label for="my-drawer-4" aria-label="close sidebar" class="drawer-overlay"></label>
                <div class="menu p-4 w-80 min-h-full bg-base-200 text-base-content">
                    <!-- Sidebar content here -->
                    <p v-for="theme in themes" :key="theme" class="w-full my-1">
                        <button
                            @click="setTheme(theme)"
                            class="outline-base-content text-start outline-offset-4 w-full"
                            data-set-theme={theme}
                            data-act-class="[&_svg]:visible">
                          <span
                              :data-theme=theme
                              class="bg-base-100 rounded-btn text-base-content block cursor-pointer font-sans">
                            <span class="grid grid-cols-5 grid-rows-3">
                              <span class="col-span-5 row-span-3 row-start-1 flex items-center gap-2 px-4 py-3">
                                <span class="flex-grow text-sm">
                                  {{ theme }}
                                </span>
                                <span class="flex h-full shrink-0 flex-wrap gap-1">
                                  <span class="bg-primary rounded-badge w-2"/>
                                  <span class="bg-secondary rounded-badge w-2"/>
                                  <span class="bg-accent rounded-badge w-2"/>
                                  <span class="bg-neutral rounded-badge w-2"/>
                                </span>
                              </span>
                            </span>
                          </span>
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useColorMode, useStorage} from '@vueuse/core'

const themes = [
    "light",
    "dark",
    "cupcake",
    "bumblebee",
    "emerald",
    "corporate",
    "synthwave",
    "retro",
    "cyberpunk",
    "valentine",
    "halloween",
    "garden",
    "forest",
    "aqua",
    "lofi",
    "pastel",
    "fantasy",
    "black",
    "luxury",
    "dracula",
    "cmyk",
    "autumn",
    "business",
    "acid",
    "lemonade",
    "night",
    "coffee",
    "winter",
    "dim",
    "nord",
    "sunset",
]

const mode = useColorMode({
    emitAuto: true,
    selector: 'html',
    attribute: 'data-theme',
})

// is used in the Cross and Circle components
const isPepe = useStorage('isPepe', false) // returns Ref<boolean>
function pepe(){
    console.log("pepe")
    isPepe.value = false
    if (document.documentElement.style.backgroundImage !== 'url("/pepe.png")') {
        isPepe.value = true
        document.documentElement.style.backgroundImage = 'url("/pepe.png")';
        document.documentElement.style.backgroundSize = '100px 100px';
    } else {
        isPepe.value = false
        document.documentElement.style.backgroundImage = '';
        document.documentElement.style.backgroundSize = '';
    }
}

function setTheme(theme: string) {
    // @ts-ignore
    mode.value = theme
    document.documentElement.setAttribute('data-theme', theme)
}

</script>
