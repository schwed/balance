package com.kon.balance.data.domain.orient.document

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotation should be used to mark entity class
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.SOURCE)
@GroovyASTTransformationClass(['com.kpmg.dns.balance.data.domain.orient.document.OrientDocumentTransformation'])
@interface OrientDocument {

    /**
     * Specifies OrientDB document class name
     */
    String value() default ''

    /**
     * Flag to generate static initSchema and initSchemaLinks methods
     */
    boolean initSchema() default false
}