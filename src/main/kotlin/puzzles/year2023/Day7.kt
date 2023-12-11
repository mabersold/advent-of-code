package puzzles.year2023

import java.util.PriorityQueue
import puzzles.Puzzle

class Day7: Puzzle(2023, 7, "Camel Cards") {
    override fun part1(): Any {
        val input = getInput(1)
        val priorityQueue = PriorityQueue(Hand.customComparator)

        input.forEach { inputString ->
            val hand = buildHand(inputString)
            priorityQueue.add(hand)
        }

        var runningTotal = 0
        var counter = 1

        while (priorityQueue.isNotEmpty()) {
            val hand = priorityQueue.poll()
            runningTotal += hand.bid * counter
            counter++
        }

        return runningTotal
    }

    override fun part2(): Any {
        val input = getInput(1)
        val priorityQueue = PriorityQueue(Hand.jokerComparator)
        input.forEach { inputString ->
            val hand = buildHand(inputString, true)
            priorityQueue.add(hand)
        }

        var runningTotal = 0
        var counter = 1

        while (priorityQueue.isNotEmpty()) {
            val hand = priorityQueue.poll()
            runningTotal += hand.bid * counter
            counter++
        }

        return runningTotal
    }

    private fun buildHand(input: String, useJokers: Boolean = false): Hand {
        val (cards, bid) = input.split(" ")

        val distinctChars = cards.toCharArray().distinct()
        val totalJokers = cards.count { c -> c == 'J' }
        val type = when (distinctChars.size) {
            1 -> Type.FIVE_OF_A_KIND
            2 -> {
                if (useJokers && totalJokers > 0) {
                    Type.FIVE_OF_A_KIND
                } else if (listOf(4, 1).contains(cards.count { it == cards[0] })) {
                    Type.FOUR_OF_A_KIND
                } else {
                    Type.FULL_HOUSE
                }
            }
            3 -> {
                if (distinctChars.map { dc -> cards.count { it == dc } }.any { it == 3 }) {
                    if (useJokers && totalJokers > 0) {
                        Type.FOUR_OF_A_KIND
                    } else {
                        Type.THREE_OF_A_KIND
                    }
                } else {
                    if (useJokers && totalJokers > 1) {
                        Type.FOUR_OF_A_KIND
                    } else if (useJokers && totalJokers == 1) {
                        Type.FULL_HOUSE
                    } else {
                        Type.TWO_PAIR
                    }
                }
            }
            4 -> {
                if (useJokers && totalJokers > 0) {
                    Type.THREE_OF_A_KIND
                } else {
                    Type.ONE_PAIR
                }
            }
            else -> if (useJokers && totalJokers > 0) {
                Type.ONE_PAIR
            } else {
                Type.HIGH_CARD
            }
        }

        return Hand(cards, type, bid.toInt())
    }

    data class Hand(
        val cards: String,
        val type: Type,
        val bid: Int
    ) {
        companion object {
            val customComparator: Comparator<Hand> = compareBy<Hand> { it.type.rank }
                .then { hand1, hand2 ->
                    compareCards(hand1.cards, hand2.cards)
            }

            val jokerComparator: Comparator<Hand> = compareBy<Hand> { it.type.rank }
                .then { hand1, hand2 ->
                    compareCards(hand1.cards, hand2.cards, true)
                }

            private fun compareCards(cards1: String, cards2: String, useJokers: Boolean = false): Int {
                for (i in 0..4) {
                    if (cards1[i] != cards2[i]) {
                        return if (getCardWeight(cards1[i], useJokers) > getCardWeight(cards2[i], useJokers)) {
                            1
                        } else {
                            -1
                        }
                    }
                }

                return 0
            }

            private fun getCardWeight(card: Char, useJokers: Boolean): Int {
                if (card.isDigit()) {
                    return card.digitToInt()
                }

                return when (card) {
                    'A' -> 14
                    'K' -> 13
                    'Q' -> 12
                    'J' -> {
                        if (useJokers) {
                            1
                        } else {
                            11
                        }
                    }
                    'T' -> 10
                    else -> 0
                }
            }
        }
    }

    enum class Type(val rank: Int) {
        FIVE_OF_A_KIND(6),
        FOUR_OF_A_KIND(5),
        FULL_HOUSE(4),
        THREE_OF_A_KIND(3),
        TWO_PAIR(2),
        ONE_PAIR(1),
        HIGH_CARD(0)
    }
}