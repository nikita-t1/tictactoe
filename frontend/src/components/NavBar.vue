<template>
    <div class="">
        <header class="flex justify-start flex-nowrap z-50 w-full text-sm py-4">
            <nav class="max-w-[85rem] w-full px-4 flex" aria-label="Global">
                <div class="flex gap-5 mt-0 pl-5"> <!-- flex-col md:flex-row -->

                    <router-link class="" to="/">
                        <button type="button" class="bg-base-100 btn btn-ghost btn-primary text-primary">
                            <MaterialSymbolsHomeOutlineRounded/>
                            <span class="hidden md:block">{{ t("home") }}</span>
                        </button>
                    </router-link>

                    <router-link class="" to="/about">
                        <button type="button" class="bg-base-100 btn btn-ghost btn-primary text-primary">
                            <UilCommentInfoAlt/>
                            <span class="hidden md:block">{{ t("about") }}</span>
                        </button>
                    </router-link>

                    <div class="">
                        <select v-model="$i18n.locale" class="py-3 px-3 pr-4 block w-full rounded-md text-sm bg-base-100 border-base-200 border-2">
                            <option v-for="locale in $i18n.availableLocales" :key="`locale-${locale}`" :value="locale">
                                {{ locale }}
                            </option>
                        </select>
                    </div>
                </div>

            </nav>
        </header>
    </div>
</template>

<script setup lang="ts">
import MaterialSymbolsHomeOutlineRounded from '~icons/material-symbols/home-outline-rounded'
import UilCommentInfoAlt from '~icons/uil/comment-info-alt'
import {useI18n} from "vue-i18n";

const {t} = useI18n()


</script>

<style scoped>


</style>
