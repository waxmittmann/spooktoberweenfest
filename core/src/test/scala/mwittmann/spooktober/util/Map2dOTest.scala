package mwittmann.spooktober.util

import mwittmann.spooktober.entity.Player
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.Map2dOTest.grid
import mwittmann.spooktober.util.MathUtils.Angle
import org.scalacheck._
import org.specs2.{ScalaCheck, Specification}

object Map2dOTest extends Specification with ScalaCheck {

  val grid = new Map2d[Unit](
    Dimensions2df(100, 100),
    Dimensions2df(10, 10)
  )

  case class UnitStorable(
    position: Position2df,
    dimensions: Dimensions2df,
    rotation: Float = 0.0f,
    item: Unit
  ) extends MapStorable[Unit] {
    override def copy(
      position: Position2df = position,
      dimensions: Dimensions2df = dimensions,
      rotation: Angle = rotation
    ) = UnitStorable(position, dimensions, rotation, item)
  }

  override def is =
    s2"""Map2d
      inBounds should
        be true at lower-left $beTrueAtLowerLeft
        be true at lower-right beTrueAtLowerRight
        be true at upper-left beTrueAtUpperLeft
        be true at upper-right $beTrueAtUpperRight
        be false when too left $beFalseWhenTooFarLeft
        be false when too right
        be false when too high
        be false when too low

      insert should
        succeed when at lower-left $succeedAtLowerLeft
        succeed when at upper-right $succeedAtUpperRight
        succeed when inBounds succeeds $succeedWhenInBounds

      """

  def beTrueAtLowerLeft = {
    grid.inBounds(new UnitStorable(
      Position2df(0, 0),
      Dimensions2df(10, 10),
      0,
      ()
    )) must beTrue
  }

  def beTrueAtUpperRight = {
    grid.inBounds(new UnitStorable(
      Position2df(90, 90),
      Dimensions2df(9.999f, 9.999f),
      0,
      ()
    )) must beTrue
  }

  def beFalseWhenTooFarLeft = {
    grid.inBounds(new UnitStorable(
      Position2df(-0.0001f, 10),
      Dimensions2df(9.999f, 9.999f),
      0,
      ()
    )) must beFalse
  }

  def succeedAtLowerLeft = {
    grid.insert(new UnitStorable(
      Position2df(0, 0),
      Dimensions2df(99.9f, 99.9f),
      0,
      ()
    ))
    ok
  }

  def succeedAtUpperRight = {
    grid.inBounds(new UnitStorable(
      Position2df(99, 99),
      Dimensions2df(0.999f, 0.999f),
      0,
      ()
    ))
    ok
  }


  def succeedWhenInBounds = {
    val gen = for {
      x <- Gen.choose(0.0f, 89.999f)
      y <- Gen.choose(0.0f, 89.999f)
    } yield (x, y)


    def abStrings: Arbitrary[(Float, Float)] =
      Arbitrary(gen)

    prop { (v: (Float, Float)) =>

      val grid = new Map2d[Unit](
        Dimensions2df(100, 100),
        Dimensions2df(10, 10)
      )

      grid.insert(new UnitStorable(
        Position2df(v._1, v._2),
        Dimensions2df(10f, 10f),
        0,
        ()
      ))

      ok
    }.setArbitrary(abStrings)
  }
}
