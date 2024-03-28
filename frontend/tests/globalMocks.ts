import {getBaseURL} from "../src/getBaseURL";
import {Server} from 'mock-socket';
import i18n from "../src/plugins/i18n";
import {createTestingPinia} from "@pinia/testing";
import {vi} from "vitest";

export class WebSocketMock {
    constructor() {}
    addEventListener() {}
    removeEventListener() {}
    send() {}
}

export const backendServerMock = () => {
    const fakeURL = getBaseURL()
    const mockServer = new Server(fakeURL);

    mockServer.on('connection', socket => {
        socket.on('message', data => {

        });
    });
    return mockServer
}

export const useI18n = () => {
    return {
        t: (key: string) => key,
    };
};

export const options = {
    global: {
        plugins: [i18n, createTestingPinia({
            createSpy: vi.fn,
        })],
        mocks: {
            $t: useI18n().t,
        },
        provide: {}

    },
};
