package com.kon.balance.data.access.impl

import com.kon.balance.data.access.BalanceBootDataAccess
import com.kon.balance.data.domain.schema.Attribute
import com.kon.balance.data.domain.schema.Feeds
import org.springframework.stereotype.Component

/**
 * Created by kshevchuk on 11/4/2015.
 */
@Component
class BalanceBootDataAccessImpl implements BalanceBootDataAccess {

    @Override
    def getAttributes(graph, config) {
        def result = attributes(graph)

        println "RESULT: $result"
    }

    @Override
    List<Feeds> getFeeds(graph, config) {
        return null
    }

    private def attributes(orientGraph) {
        return orientGraph.withTransaction { graph ->

            return attributeToJSON(Attribute.graphQuery('select from Attribute'))
        }
    }

    static List attributeToJSON(def attributes) {
        attributes.collect { Attribute attribute ->
            [rid: attribute.id.toString(),
             name: attribute.name,
             columnName: attribute.columnName,
             sourceName: attribute.sourceName,
             schemaName: attribute.schemaName,
             tableName: attribute.tableName,
             isCDE: attribute.isCDE]
        }
    }
}
