<template>
    <button
        @click="setTheme(props.theme)"
        class="outline-base-content text-start outline-offset-4 w-full"
        data-set-theme={theme}
        data-act-class="[&_svg]:visible">
            <span
              :data-theme=props.theme
              class="bg-base-100 rounded-btn text-base-content block cursor-pointer font-sans">
            <span class="grid grid-cols-5 grid-rows-3">
              <span class="col-span-5 row-span-3 row-start-1 flex items-center gap-2 px-4 py-3">
                <span class="flex-grow text-sm">
                  {{ props.theme }}
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
</template>
<script setup lang="ts">

const props = defineProps<{
    theme: string;
    // setTheme: (theme: string) => void;
}>();

const emit = defineEmits<{
    setTheme: [theme: string];
}>();

function setTheme(theme: string) {
    emit("setTheme", theme);
}
</script>
