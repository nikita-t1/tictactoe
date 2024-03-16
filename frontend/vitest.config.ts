/// <reference types="vitest" />
import { defineConfig } from 'vite'
import vue from "@vitejs/plugin-vue";
import path from "path";


export default defineConfig({
    plugins: [
        vue(),
    ],
    // @ts-ignore
    test: {
        globalThis: true,
        environment: 'happy-dom'
        // ... Specify options here.
    },
    resolve: {
        alias: {
            // '@': fileURLToPath(new URL('./src', import.meta.url)),
            '@': path.resolve(__dirname, './src')
        }
    }
})
