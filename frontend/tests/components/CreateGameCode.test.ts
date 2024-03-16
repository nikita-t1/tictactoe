import { mount } from '@vue/test-utils'
import {beforeEach, afterEach, describe, it, expect, vi, beforeAll, afterAll} from 'vitest'
import axios from 'axios'
import AxiosMockAdapter from 'axios-mock-adapter'
import CreateGameCode from "../../src/components/CreateGameCode.vue";
import i18n from "../../src/plugins/i18n";
import {createPinia} from "pinia";
import {backendServerMock, WebSocketMock} from "../globalMocks";
import {Server} from "mock-socket";

let mock: InstanceType<typeof AxiosMockAdapter>
let backendServer: Server


export const useI18n = () => {
    return {
        t: (key: string) => key,
    };
};

const options = {
    global: {
        plugins: [i18n, createPinia()],
        mocks: {
            $t: useI18n().t,
        },
    },
};

describe('CreateGameCode', () => {
    beforeAll(() => {
        backendServer = backendServerMock()
    })

    afterAll(() => {
        backendServer.close()
    })


    beforeEach(() => {
        mock = new AxiosMockAdapter(axios)
    })

    afterEach(() => {
        // reset the axios mock after each test
        mock.reset()
    })

    it('validates initial state of gameCode', () => {
        const wrapper = mount(CreateGameCode, options)
        expect(wrapper.text()).toContain('----')
    })


    it('requests game code when button is clicked', async () => {
        // Creating an axios mock for your post request
        mock.onPost('/create-game-code').reply(200, 'W33D')
        const wrapper = mount(CreateGameCode, options)

        // Find button and simulate click event
        const button = wrapper.find('button')
        await button.trigger('click')

        // gameCode should be updated after click
        console.log(wrapper.text())
        expect(wrapper.text()).not.toContain('----')
        expect(wrapper.text()).toContain('W33D')
    })
    // More tests...
})
