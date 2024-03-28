import {mount} from '@vue/test-utils'
import InputGameCode from "../../src/components/InputGameCode.vue";
import {afterAll, beforeAll, describe, expect, it} from "vitest";
import {useWebSocketStore} from "../../src/stores/useMultiplayerWebsocketStore";
import {backendServerMock, options} from "../globalMocks";
import {Server} from "mock-socket";

let backendServer: Server

describe('InputGameCode', () => {

    beforeAll(() => {
        backendServer = backendServerMock()
    })

    afterAll(() => {
        backendServer.close()
    })

    it('calls webSocketStore.init when startGame is clicked', async () => {
        const socketStore = useWebSocketStore()
        const wrapper = mount(InputGameCode, options)

        // Find input and enter game code
        const input = wrapper.find('input')
        await input.setValue('Y0L0')

        await wrapper.find('button').trigger('click')
        expect(socketStore.init).toHaveBeenCalledOnce()
    })

})
