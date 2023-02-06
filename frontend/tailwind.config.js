/** @type {import('tailwindcss').Config} */
module.exports = {
    darkMode: 'class',
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
        "node_modules/preline/dist/*.js",
    ],
    theme: {
        extend: {},
    },
    plugins: [
        require('preline/plugin'),
    ],
}
