package com.kon.balance.data.access

import com.kon.balance.data.domain.schema.Feeds

/**
 * Created by kshevchuk on 11/4/2015.
 */
interface BalanceBootDataAccess {

    def getAttributes(graph, config)

    List<Feeds> getFeeds(graph, config)

}