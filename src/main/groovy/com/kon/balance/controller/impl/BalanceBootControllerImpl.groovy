package com.kon.balance.controller.impl

import com.kon.balance.controller.BalanceBootController
import com.kon.balance.service.BalanceBootService
import com.orientechnologies.common.log.OLogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment
import org.springframework.core.env.MutablePropertySources
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by kshevchuk on 10/30/2015.
 */
@Component
class BalanceBootControllerImpl implements BalanceBootController {
    OLogManager log = OLogManager.instance()
    def graph
    def config

    @Autowired
    Environment environment

    @Autowired
    BalanceBootService service


    @Override
    def performBalanceExport() {
        log.info this, "ENVIRONMENT IS: $environment"
        log.info this, "WILL PERFORM BALANCE CHECK EXPORT.GRAPH: $graph, CONFIG: $config"

        service.processBalanceCheckData graph, config
        service.outputBalanceCheckData()

        return null
    }

    @PostConstruct
    public void init() {
        MutablePropertySources envPropSources = ((ConfigurableEnvironment) environment).getPropertySources();
        graph = envPropSources.get("ORIENT-OBJECTS").getProperty("graph");
        config = envPropSources.get("ORIENT-OBJECTS").getProperty("config");
    }
}
