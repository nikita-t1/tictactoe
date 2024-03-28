<template>
    <div
        class="flex flex-col max-w-4xl content-center self-center items-center justify-center mx-auto space-x-0 space-y-6">

        <!-- Category: Singleplayer -->
        <div class="rounded-box flex flex-col border-base-300 bg-base-100">

            <p class="mt-1 text-md font-bold py-3 px-4 md:py-4 md:px-5">
                {{ t("singleplayer") }}
            </p>
            <div class="p-4 md:p-5">

                <div
                    class="rounded-box bg-base-200 min-h-[14rem] flex flex-col w-96 md:w-[50rem] border p-4 md:p-5 border-base-300">
                  <span class="text-md font-semibold">
                      {{ t("play_vs_computer") }}
                  </span>
                    <p class="grow mt-6">
                        {{ t("play_vs_computer_desc") }}
                    </p>
                    <router-link to="/singleplayer/play" class="w-full">
                        <button type="button" class="btn btn-primary w-full">
                            {{ t("start_game") }}
                        </button>
                    </router-link>
                </div>
            </div>
        </div>

        <!-- Category: Multiplayer -->
        <div class="rounded-box flex flex-col border-base-300 bg-base-100">
            <p class="mt-1 text-md font-bold rounded-t-xl py-3 px-4 md:py-4 md:px-5 border-base-300">
                {{ t("multiplayer") }}
            </p>
            <div class="p-4 md:p-5 flex flex-col md:flex-row md:space-x-8 space-y-6 md:space-y-0">

                <!-- Player 1 -->
                <div class="rounded-box bg-base-200 min-h-[14rem] flex flex-col w-96 border p-4 md:p-5 border-base-300">
                  <span class="text-md font-semibold">
                      {{ t("player") }} 1
                  </span>
                    <p class=" grow mt-6">
                        {{ t("create_new_game_desc") }}
                    </p>
                    <router-link to="/multiplayer/create-new-game" class="">
                        <button type="button" class="btn btn-primary w-full">
                            {{ t("create_new_game") }}
                        </button>
                    </router-link>
                </div>

                <!-- Player 2 -->
                <div class="rounded-box bg-base-200 min-h-[14rem] flex flex-col w-96 border p-4 md:p-5 border-base-300">
                  <span class="text-md font-semibold">
                      {{ t("player") }} 2
                  </span>
                    <p class="grow mt-6">
                        {{ t("join_existing_game_desc") }}
                    </p>
                    <router-link to="/multiplayer/join-existing-game" class="">
                        <button type="button" class="btn btn-primary w-full">
                            {{ t("join_existing_game") }}
                        </button>
                    </router-link>
                </div>

            </div>
        </div>

    </div>
</template>

<script setup lang="ts">

import {useI18n} from "vue-i18n";

const {t} = useI18n()

</script>

<style scoped>

</style>
