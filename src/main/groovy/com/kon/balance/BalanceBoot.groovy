package com.kon.balance

import com.kon.balance.controller.BalanceBootController
import com.orientechnologies.common.log.OLogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources

/**
 * Created by kshevchuk on 11/4/2015.
 */
@SpringBootApplication
class BalanceBoot implements CommandLineRunner {
    def log = OLogManager.instance()
    static final Map<String, Object> map = new HashMap<>()

    @Autowired
    BalanceBootController controller

    static main(String[] args) {

        def builder
        builder = new SpringApplicationBuilder();
        builder.initializers new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
                //log.info this, "INITIALIZING .. $map"
                println "INITIALIZING ...$map"

                MutablePropertySources propertySources = configurableApplicationContext.getEnvironment().getPropertySources();
                Map<String, Object> maps = new HashMap<>();
                maps["graph"] = map.get('graph')
                maps["config"] = map.get('config')
                println "PASSING ${maps.graph}, ${maps.config}"
                propertySources.addFirst(new MapPropertySource("ORIENT-OBJECTS", maps))

            }
        }
        builder.web Boolean.FALSE
        builder.sources BalanceBoot
        builder.run args

    }

    @Override
    void run(String... strings) throws Exception {

        //log.info this, "WILL CALL CONTROLLER ..."
        println "WILL CALL CONTROLLER ..."
        controller.performBalanceExport()
    }

    static def startup(graph, config) {
        println "PUTTING INTO MAP $graph, $config"
        map.put("graph", graph)
        map.put("config", config)

        main "started"
        println "CLOSING SPRING BOOT"
    }
}
