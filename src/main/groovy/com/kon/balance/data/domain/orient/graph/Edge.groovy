package com.kon.balance.data.domain.orient.graph

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotation for marking classes that will wrap OrientEdge
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.SOURCE)
@GroovyASTTransformationClass(['com.kpmg.dns.balance.data.domain.orient.graph.EdgeTransformation'])
@interface Edge {
    /**
     * OrientDB edge class name
     * @since 0.1.0
     */
    String value() default ''

    /**
     * Vertex entity class
     * @see com.tinkerpop.blueprints.Direction#IN
     * @since 0.1.0
     */
    Class from()

    /**
     * Vertex entity class
     * @see com.tinkerpop.blueprints.Direction#OUT
     * @since 0.1.0
     */
    Class to()

    /**
    * Flag to generate static initSchema and initSchemaLinks methods
    *
    * @since 0.1.1
    * @return
    */
    boolean initSchema() default false
}