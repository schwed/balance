package com.kon.balance

import com.orientechnologies.common.log.OLogManager
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory

/**
 * Created by kshevchuk on 11/3/2015.
 */
def log = OLogManager.instance()
def graph
def config
if (binding.variables.containsKey('orient')) {
    graph = orient.graph

    if (propertiesFile == null || propertiesFile == "") {
        log.info this, "BALANCE CHECK - NEED PROPERTIES FILE TO RUN BALANCE CHECK EXPORTS"
        return "BALANCE CHECK - NEED PROPERTIES FILE"
    }
    config = new ConfigSlurper().parse(new File(propertiesFile).toURI().toURL())

    if (graph.getRawGraph().getName() != config.db.name) {
        log.info this, "BALANCE CHECK - CONNECTED TO ${graph.getRawGraph().getName()}! RECONNECT TO $config.db.name DATABASE"
        return "BALANCE CHECK - CONNECTED TO ${graph.getRawGraph().getName()}! RECONNECT TO $config.db.name DATABASE"
    }

    def springBoot = this.class.classLoader.loadClass('com.kpmg.dns.balance.BalanceBoot')
    log.info this, "BALANCE CHECK - LOADED SPRING BOOT: $springBoot"
    springBoot.startup graph, config

}
else {
    /**
     *  IDE DEVELOPMENT AND DEBUG
     */
    config = new ConfigSlurper().parse(new File('C:\\DOWNLOADS\\DEV\\balance-properties.groovy').toURI().toURL())
    graph = getDevelopmentGraph(config.db.name)
    BalanceBoot.startup graph, config
}

private def getDevelopmentGraph(dbname) {
    def url = "remote:USMDCKDDB6042/$dbname"
    def username = "root"
    def password = "Genie.2013"
    def factory = new OrientGraphFactory(url, username, password)
    graph = factory.tx
}
