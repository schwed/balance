package com.kon.balance.service

/**
 * Created by kshevchuk on 11/2/2015.
 */
interface BalanceBootService {

    def processBalanceCheckData(graph, config)
    def outputBalanceCheckData()


}