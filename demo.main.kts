
@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.sspgen:dsl:0.3.0")

import no.ntnu.ihb.sspgen.dsl.ssp

ssp("TestSsdGen") {

    ssd("A simple CLI test") {

        author = "John Doe"
        description = "A simple description"

        system("Test") {

            description = "An even simpler description"

            elements {
                component("FMU1", "resources/FMU1.fmu") {
                    connectors {
                        real("output", output) {
                            unit("m/s")
                        }
                        real("input", input)
                        integer("counter", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("input", 2.0)
                            integer("counter", 99)
                        }
                    }
                }
                component("FMU2", "resources/FMU2.fmu") {
                    connectors {
                        real("input", input)
                        real("output", output)
                    }
                }
            }

            connections {
                "FMU2.output" to "FMU1.input"
                ("FMU1.output" to "FMU2.input").linearTransformation(factor = 1.5)
            }

        }

        defaultExperiment(startTime = 1.0)

    }

    resources {
        file("path/to/FMU1.fmu")
        file("path/to/FMU2.fmu")
        url("example.com/someFile.txt")
    }

}