package com.kon.balance.data.domain.orient.graph

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotation for marking classes that will wrap OrientVertex
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.SOURCE)
@GroovyASTTransformationClass(['com.kpmg.dns.balance.data.domain.orient.graph.VertexTransformation'])
@interface Vertex {

    /**
     * Vertex class name in OrientDB
     * @return
     */
    String value() default ''

    /**
     * Flag to generate static initSchema and initSchemaLinks methods
     *
     * @since 0.1.1
     * @return
     */
    boolean initSchema() default false
}