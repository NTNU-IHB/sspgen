package no.ntnu.ihb.sspgen.dsl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class QuarterTruckTest {

    @Test
    fun testValidQuarterTruck() {
        Assertions.assertDoesNotThrow {
            validSspDefinition.validate()
        }
    }

    @Test
    fun testInvalidQuarterTruck() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            invalidSspDefinition.validate()
        }
    }

    @Test
    fun testModelDescriptionRetrieval() {
        Assertions.assertEquals(3, validSspDefinition.parsedModelDescriptions.size)
    }

    private companion object {

        val validSspDefinition = ssp("QuarterTruck") {

            resources {
                val cl = QuarterTruckTest::class.java.classLoader
                val fmuPath = cl.getResource("quarter-truck")!!.file
                file("$fmuPath/chassis.fmu")
                file("$fmuPath/wheel.fmu")
                file("$fmuPath/ground.fmu")
            }

            ssd("QuarterTruck") {

                system("QuarterTruck") {

                    elements {

                        component("chassis", "resources/chassis.fmu") {
                            connectors {
                                real("p.e", output)
                                real("p.f", input)
                            }
                        }

                        component("wheel", "resources/wheel.fmu") {
                            connectors {
                                real("p.f", input)
                                real("p1.e", input)
                                real("p.e", output)
                                real("p1.f", output)
                            }
                        }

                        component("ground", "resources/ground.fmu") {
                            connectors {
                                real("p.e", input)
                                real("p.f", output)
                            }
                        }

                    }

                    connections {

                        "chassis.p.e" to "wheel.p1.e"
                        "wheel.p1.f" to "chassis.p.f"
                        "wheel.p.e" to "ground.p.e"
                        "ground.p.f" to "wheel.p.f"

                    }

                }

            }

        }

        val invalidSspDefinition = ssp("QuarterTruck") {

            resources {
                val fmuPath = QuarterTruckTest::class.java.classLoader.getResource("quarter-truck")!!.file
                file("$fmuPath/chassis.fmu")
            }

            ssd("QuarterTruck") {

                system("QuarterTruck") {

                    elements {

                        component("chassis", "resources/chassis.fmu") {
                            connectors {
                                real("p.s", output)
                                real("p.d", input)
                            }
                        }
                    }

                }

            }

        }

    }

}
