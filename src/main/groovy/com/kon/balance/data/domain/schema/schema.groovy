package com.kon.balance.data.domain.schema

import com.kon.balance.data.domain.orient.graph.Edge
import com.kon.balance.data.domain.orient.graph.Vertex
import groovy.transform.CompileStatic

/**
 * Created by kshevchuk on 11/3/2015.
 */
@Vertex
@CompileStatic
class Attribute {
    String name
    String columnName
    String sourceName
    String schemaName
    String tableName
    String isCDE

}

@Edge(from = Attribute, to = Attribute)
@CompileStatic
class Feeds {

}
