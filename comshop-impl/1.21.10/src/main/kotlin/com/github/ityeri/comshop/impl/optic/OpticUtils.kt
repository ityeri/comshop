package com.github.ityeri.comshop.impl.optic

import arrow.optics.PTraversal


sealed class Choice3<out A, out B, out C> {
    data class First<out A>(val value: A) : Choice3<A, Nothing, Nothing>()
    data class Second<out B>(val value: B) : Choice3<Nothing, B, Nothing>()
    data class Third<out C>(val value: C) : Choice3<Nothing, Nothing, C>()

    inline fun <R> fold(
        ifFirst: (A) -> R,
        ifSecond: (B) -> R,
        ifThird: (C) -> R
    ): R = when (this) {
        is First -> ifFirst(value)
        is Second -> ifSecond(value)
        is Third -> ifThird(value)
    }
}

fun <
        S1, S2, S3,
        T1, T2, T3,
        A, B
        > split3(
    traversal1: PTraversal<S1, T1, A, B>,
    traversal2: PTraversal<S2, T2, A, B>,
    traversal3: PTraversal<S3, T3, A, B>
): PTraversal<
        Choice3<S1, S2, S3>,
        Choice3<T1, T2, T3>,
        A,
        B
        > = PTraversal { choiceS, mapFunc ->
    choiceS.fold(
        ifFirst = { s1 ->
            Choice3.First(traversal1.modify(s1, mapFunc))
        },
        ifSecond = { s2 ->
            Choice3.Second(traversal2.modify(s2, mapFunc))
        },
        ifThird = { s3 ->
            Choice3.Third(traversal3.modify(s3, mapFunc))
        }
    )
}
