import {createI18n} from "vue-i18n";
import en from "@/locales/en.json";
import de from "@/locales/de-DE.json";

export default createI18n({
    legacy: false, // you must set `false`, to use Composition API
    locale: 'en', // set locale
    //fallbackLocale: 'en', // set fallback locale
    messages: {en, de}, // set locale messages
    // If you need to specify other options, you can set other options
    // ...
})
