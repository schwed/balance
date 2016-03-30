package com.kon.balance.service.impl

import com.kon.balance.data.access.BalanceBootDataAccess
import com.kon.balance.service.BalanceBootService
import com.orientechnologies.common.log.OLogManager
import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Vertex
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by kshevchuk on 11/2/2015.
 */
@Component
class BalanceBootServiceImpl implements BalanceBootService {

    @Autowired
    BalanceBootDataAccess dataAccess

    def log = OLogManager.instance()

    def outputDataList = []

    @Override
    def outputBalanceCheckData() {
        if (outputDataList.empty) {
            log.info this, "THE BALANCE CHECK EXPORT DATA IS EMPTY, WILL NOT CREATE OUTPUT FILE. PLEASE VERIFY DB CONNECTION."
            return
        }
        def path = new File(".").getCanonicalPath() - "bin" + "scripts" + File.separator + "BALANCE" + File.separator + "OUTPUT" + File.separator
        log.info this, "WILL OUTPUT FILE TO: $path"
        println "WILL OUTPUT FILE TO: $path"

        //def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
        //def scriptFile = getClass().protectionDomain.codeSource.location.path
        //println "SCRIPT DIRECTORY: $scriptDir"
        //println "FILE DIRECTORY: $scriptFile"

        def outputFile = path + "balance.csv"

        log.info(this, "THE COMPLETE OUTPUT DIRECTORY ID: $outputFile")
        def file = new File(outputFile)
        def writer = file.newWriter()
        log.info(this, "CREATED WRITER")
        writer << "SOURCE-BALANCE-CHECK-ID,SOURCE-SYSTEM,SOURCE-SCHEMA,SOURCE-TABLE,SOURCE-ATTRIBUTE,TARGET-BALANCE-CHECK-ID,TARGET-SYSTEM,TARGET-SCHEMA,TARGET-TABLE,TARGET-ATTRIBUTE\r\n"

        outputDataList.each { Map<String, BalanceCheckIdData> map ->
            def sourceDataObject = map['source']
            def targetDataObject = map['target']
            writer << "$sourceDataObject.balanceCheckId,$sourceDataObject.source,$sourceDataObject.schema,$sourceDataObject.table,$sourceDataObject.attribute,$targetDataObject.balanceCheckId,$targetDataObject.source,$targetDataObject.schema,$targetDataObject.table,$targetDataObject.attribute\r\n"
        }

        log.info(this, "DONE WRITING FILE")

        writer.close()
    }


    @Override
    def processBalanceCheckData(graph, config) {

        def value = dataAccess.getAttributes(graph, config)

        println "GOT ATTRIBUTES $value"

        println "GRAPH DB NAME: ${graph.getRawGraph().getName()}"
        def fields = config.fields
        Iterable<Vertex> vertexIterable = graph.getVertices()
        vertexIterable.each { sourceVertex ->

            Iterable<Edge> edgeIterable = sourceVertex.getEdges(Direction.OUT)
            edgeIterable.each { edge ->
                def targetVertex = edge.getVertex(Direction.IN)

                def recordMap = [:]
                def sourceData = new BalanceCheckIdData()
                sourceData.source = sourceVertex.getProperty(fields.system).toString()
                sourceData.schema = sourceVertex.getProperty(fields.schema).toString()
                sourceData.table = sourceVertex.getProperty(fields.table).toString()
                sourceData.attribute = sourceVertex.getProperty(fields.attribute).toString()
                sourceData.balanceCheckId = "$sourceVertex.id$edge.id"
                recordMap['source'] = sourceData
                log.info(this, "SOURCE DATA: balanceCheckId = $sourceData.balanceCheckId, $fields.system = $sourceData.source, $fields.schema = $sourceData.schema, $fields.table = $sourceData.table, $fields.attribute = $sourceData.attribute")

                def targetData = new BalanceCheckIdData()
                targetData.balanceCheckId = "$edge.id$targetVertex.id"
                targetData.source = targetVertex.getProperty(fields.system).toString()
                targetData.schema = targetVertex.getProperty(fields.schema).toString()
                targetData.table = targetVertex.getProperty(fields.table).toString()
                targetData.attribute = targetVertex.getProperty(fields.attribute).toString()
                recordMap['target'] = targetData
                log.info this, "TARGED DATA: balanceCheckId = $targetData.balanceCheckId, $fields.system = $targetData.source, $fields.schema = $targetData.schema, $fields.table = $targetData.table, $fields.attribute = $targetData.attribute"

                outputDataList.add recordMap
            }
        }

        log.info this, "OUTPUT DATA SIZE: $outputDataList.size"
        outputDataList

    }

    class BalanceCheckIdData {
        def balanceCheckId
        def source
        def schema
        def table
        def attribute
    }

}
