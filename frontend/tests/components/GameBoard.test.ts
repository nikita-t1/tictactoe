// import the required testing libraries and components
import { mount } from '@vue/test-utils'
import {beforeEach, afterEach, describe, it, expect, vi, beforeAll, afterAll} from 'vitest'
import GameBoard from "../../src/components/GameBoard.vue";
import IconCross from "../../src/components/icons/IconCross.vue";
import IconCircle from "../../src/components/icons/IconCircle.vue";

import {EMPTY_BOARD_FIELD,MOVE_BY_OPPONENT as MOVE_BY_COMPUTER, MOVE_BY_PLAYER} from "../../src/helper/GameBoardHelper";

describe('GameBoard', () => {

    it('emits correct event', async () => {
        const wrapper = mount(GameBoard, {
            props: {
                gameBoard: Array(9).fill(null),
                awaitingMoveBy: MOVE_BY_PLAYER
            }
        });

        await wrapper.find('.field').trigger('click')
        expect(wrapper.emitted()).toHaveProperty('playerMove');
        expect(wrapper.emitted().playerMove).toHaveLength(1);
        console.log(wrapper.emitted().playerMove)
        expect(wrapper.emitted().playerMove[0]).toEqual([0]); // first game board slot clicked
    });

    it('emits playerMove event when clicked and awaitingMoveBy is MOVE_BY_PLAYER', async () => {
        const wrapper = mount(GameBoard, {
            props: {
                gameBoard: Array(9).fill(null),
                awaitingMoveBy: MOVE_BY_PLAYER
            }
        });

        await wrapper.find('.field').trigger('click');
        expect(wrapper.emitted()).toHaveProperty('playerMove');
    })

    it('does not emit playerMove event when clicked and awaitingMoveBy is not MOVE_BY_PLAYER', async () => {
        const wrapper = mount(GameBoard, {
            props: {
                gameBoard: Array(9).fill(null),
                awaitingMoveBy: MOVE_BY_COMPUTER
            }
        });

        await wrapper.find('.field').trigger('click');
        expect(wrapper.emitted()).not.toHaveProperty('playerMove');
    })

    it('renders correct number of game slots', () => {
        const gameBoard = [
            EMPTY_BOARD_FIELD, MOVE_BY_PLAYER, EMPTY_BOARD_FIELD,
            MOVE_BY_COMPUTER, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
            MOVE_BY_PLAYER, EMPTY_BOARD_FIELD, MOVE_BY_COMPUTER
        ]
        const wrapper = mount(GameBoard, {
            props: { gameBoard , awaitingMoveBy: MOVE_BY_PLAYER }
        });

        const gameSlots = wrapper.findAll('.field')
        expect(gameSlots.length).toBe(gameBoard.length);
    });

    it('shows correct icons based on gameBoard values', () => {
        const gameBoard = [
            EMPTY_BOARD_FIELD, MOVE_BY_PLAYER, EMPTY_BOARD_FIELD,
            MOVE_BY_COMPUTER, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
            MOVE_BY_PLAYER, EMPTY_BOARD_FIELD, MOVE_BY_COMPUTER
        ]
        const wrapper = mount(GameBoard, {
            props: { gameBoard, awaitingMoveBy: MOVE_BY_PLAYER }
        });

        const playerMoves = wrapper.findAllComponents(IconCross)
        const computerMoves = wrapper.findAllComponents(IconCircle)
        expect(playerMoves.length).toBe(gameBoard.filter(move => move === MOVE_BY_PLAYER).length);
        expect(computerMoves.length).toBe(gameBoard.filter(move => move === MOVE_BY_COMPUTER).length);
    });
});
