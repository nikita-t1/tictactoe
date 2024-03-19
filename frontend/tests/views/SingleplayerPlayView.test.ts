import {mount} from '@vue/test-utils'
import SingleplayerPlayView from "../../src/views/SingleplayerPlayView.vue";
import {useSingleplayerGameStore} from "../../src/stores/useSingleplayerGameStore";
import {describe, test, vi} from 'vitest'
import {storeToRefs} from "pinia";
import {options} from "../globalMocks";
import {WebSocketCodes} from "../../src/StatusCodes";
import {EMPTY_BOARD_FIELD, MOVE_BY_PLAYER} from "../../src/helper/GameBoardHelper";


describe('SingleplayerPlayView', () => {

    test('restart game on mount', ({expect}) => {
        const gameStore = useSingleplayerGameStore()
        mount(SingleplayerPlayView, options)
        expect(gameStore.replay).toHaveBeenCalledTimes(1)
    })

    test('renders correctly based on props', ({expect}) => {
        const gameStore = useSingleplayerGameStore()
        const {currentStatusCode, hasGameEnded, awaitingMoveBy, gameBoard} = storeToRefs(gameStore)

        currentStatusCode.value = WebSocketCodes.STATUS_OK
        hasGameEnded.value = true
        awaitingMoveBy.value = MOVE_BY_PLAYER
        gameBoard.value = Array(9).fill(EMPTY_BOARD_FIELD)

        let wrapper = mount(SingleplayerPlayView, options)

        // find the 'replay' and 'home' buttons
        expect(wrapper.findAll('button').length).toBe(2)

        hasGameEnded.value = false
        wrapper = mount(SingleplayerPlayView, options)
        // 'replay' and 'home' buttons should not be visible when game is ongoing
        expect(wrapper.findAll('button').length).toBe(0)
    })

    test('run functions on button clicks', ({expect}) => {
        const gameStore = useSingleplayerGameStore()
        const replaySpy = vi.spyOn(gameStore, 'replay')
        const {currentStatusCode, hasGameEnded, awaitingMoveBy, gameBoard} = storeToRefs(gameStore)

        hasGameEnded.value = true
        currentStatusCode.value = WebSocketCodes.STATUS_OK

        const wrapper = mount(SingleplayerPlayView, options)
        expect(gameStore.replay).toHaveBeenCalledTimes(1)

        expect(wrapper.find('#replay_btn').exists()).toBe(true)
        wrapper.find('#replay_btn').trigger('click')
        // replay should be called twice, once on mount and once on button click
        expect(replaySpy).toHaveBeenCalledTimes(2)
    })

})
