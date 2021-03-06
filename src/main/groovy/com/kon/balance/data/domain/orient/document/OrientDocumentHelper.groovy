package com.kon.balance.data.domain.orient.document

import com.orientechnologies.orient.core.metadata.schema.OType
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

/**
 * OrientDB document helper methods
 */
@CompileStatic
class OrientDocumentHelper {

    /**
     * Provides simple static query execution
     * Note that orient database connection
     * should be already attached to thread
     * @since 0.1.0
     *
     * @param query
     * @param params
     * @return <T>
     */
    static <T> Object executeQuery(Class<T> clazz, String query, boolean singleResult, ... params) {
        def orientQuery = new OSQLSynchQuery<ODocument>(query)
        List<ODocument> result = (List<ODocument>) new ODocument().getDatabaseIfDefined().command(orientQuery).execute(params)
        if (singleResult) {
            return transformDocument(clazz, result[0])
        }
        return transformDocumentCollection(clazz, result, OType.LINKLIST)
    }

    /**
     * Dynamic mehtod for getting document instance from entity
     * @since 0.1.1
     *
     * @param object
     * @return ODocument instance
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    static ODocument transformEntity(object) {
        object.document
    }

    /**
     * Transforms collection of entities into collection of documents
     * @since 0.1.1
     *
     * @param entities
     * @return
     */
    static List<ODocument> transformEntityCollection(Iterable<?> entities) {
        entities.collect {
            transformEntity(it)
        }
    }

    /**
     * Transforms collection of documents into collection of entities
     * @since 0.1.1
     *
     * @param clazz
     * @param type
     * @param documents
     * @return
     */
    static <T> Iterable<T> transformDocumentCollection(Class<T> clazz, def documents, OType type = null) {
        def collection = ((Iterable)documents).collect {
            transformDocument(clazz, it)
        }
        switch (type) {
            case OType.LINKSET:
                return new LinkedHashSet<T>(collection)
                break;
        }
        return collection
    }

    /**
     * Transform document into entity instance
     * @since 0.1.1
     *
     * @param clazz
     * @param document
     * @return
     */
    static <T> T transformDocument(Class<T> clazz, Object document) {
        if (!document) {
            return null
        }
        return clazz.newInstance(document)
    }

}
