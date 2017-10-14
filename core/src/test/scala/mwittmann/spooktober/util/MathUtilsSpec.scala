package mwittmann.spooktober.util

import mwittmann.spooktober.unit.Vector2df
import org.specs2.mutable.Specification

object MathUtilsSpec extends Specification {


  "getAngle" should {

    "give 0 degrees for (0, 1), (0, 1)" >> {
      MathUtils.getAngle2(Vector2df(0, 1), Vector2df(0, 1)) mustEqual 0
    }

    "give 90 degrees for (0, 1), (1, 0)" >> {
      MathUtils.getAngle2(Vector2df(0, 1), Vector2df(1, 0)) mustEqual 270
    }

    "give 180 degrees for (0, 1), (0, -1)" >> {
      MathUtils.getAngle2(Vector2df(0, 1), Vector2df(0, -1)) mustEqual 180
    }

    "give 270 degrees for (0, 1), (-1, 0)" >> {
      MathUtils.getAngle2(Vector2df(0, 1), Vector2df(-1, 0)) mustEqual 90
    }

  }
}
