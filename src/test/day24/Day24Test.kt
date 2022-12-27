package day24

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day24Test {

    @Test
    fun `snafucate 0, 1, 2`() {
        1L.snafucate() shouldBe "1"
        2L.snafucate() shouldBe "2"
    }

    @Test
    fun `snafucate 5`() {
        5L.snafucate() shouldBe "10"
    }

    @Test
    fun `snafucate 32969743607087`() {
        32969743607087.snafucate() shouldBe "10"
    }

    @Test
    fun `snafucate 4`() {
        4L.snafucate() shouldBe "1-"
    }

    @Test
    fun `snafucate 3`() {
        3L.snafucate() shouldBe "1="
    }

    @Test
    fun `snafucate 15`() {
        15L.snafucate() shouldBe "1=0"
    }

    @Test
    fun `snafucate 201`() {
        201L.snafucate() shouldBe "2=01"
    }


    @Test
    fun `unsnafucate 0, 1, 2`() {
        "0".unsnafucate() shouldBe 0
        "1".unsnafucate() shouldBe 1
        "2".unsnafucate() shouldBe 2
    }

    @Test
    fun `unsnafucate 10`() {
        "10".unsnafucate() shouldBe 5
    }

    @Test
    fun `unsnafucate 4`() {
        "1-".unsnafucate() shouldBe 4
    }

    @Test
    fun `unsnafucate 1=-0-2`() {
        "1=-0-2".unsnafucate() shouldBe 1747
    }

    @Test
    fun `unsnafucate 2=01`() {
        "2=01".unsnafucate() shouldBe 201
    }

}