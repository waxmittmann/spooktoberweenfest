package mwittmann.spooktober.util

import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.Map2dO.{MapStorable, printlnd}

import scala.collection.mutable

object Map2dO {
  val debug = false

  def printlnd(str: String) = if (debug) println(str) else ()

  case class MapStorable[A](position: Position2df, dimensions: Dimensions2df, item: A)

  def main(args: Array[String]): Unit = {
    val map2d = new Map2d[String](Dimensions2df(100.0f, 100.0f), Dimensions2df(5.0f, 5.0f))

    map2d.insert(MapStorable(Position2df(0, 0), Dimensions2df(4.9f, 4.9f), "ll"))
    map2d.insert(MapStorable(Position2df(0, 5.0f), Dimensions2df(4.9f, 4.9f), "ul"))
    map2d.insert(MapStorable(Position2df(5.0f, 0), Dimensions2df(4.9f, 4.9f), "lr"))
    map2d.insert(MapStorable(Position2df(5.0f, 5.0f), Dimensions2df(4.9f, 4.9f), "ur"))
    map2d.insert(MapStorable(Position2df(0, 0), Dimensions2df(10, 10), "a"))

    println(map2d.get(0.1f, 0.1f)) //ll, a
    println(map2d.get(4.9f, 4.9f)) // ll, a

    println(map2d.get(0.1f, 5.1f)) //ul, a
    println(map2d.get(4.9f, 9.9f)) //ul, a

    println(map2d.get(5.1f, 0.1f)) //lr, a
    println(map2d.get(9.9f, 4.9f)) //lr, a

    println(map2d.get(5.1f, 5.1f))
    println(map2d.get(9.9f, 9.9f))
  }

}

class Map2d[A](
  mapDimensions: Dimensions2df,
  nodeDimensions: Dimensions2df
) {
  assert(mapDimensions.width > 0)
  assert(mapDimensions.width >= nodeDimensions.width)
  assert(mapDimensions.height > 0)
  assert(mapDimensions.height  >= nodeDimensions.height)

  val horizontalNodeNr = Math.ceil(mapDimensions.width / nodeDimensions.width).toInt
  val verticalNodeNr = Math.ceil(mapDimensions.height / nodeDimensions.width).toInt

  val nodes = Array.ofDim[mutable.Set[MapStorable[A]]](horizontalNodeNr, verticalNodeNr)

  def insert(storable: MapStorable[A]): Unit = {
    val (startNodeX, startNodeY) = (
      Math.floor(storable.position.x / nodeDimensions.width).toInt,
      Math.floor(storable.position.y / nodeDimensions.height).toInt
    )

    val (endNodeX, endNodeY) = (
      Math.floor((storable.position.x + storable.dimensions.width) / nodeDimensions.width).toInt,
      Math.floor((storable.position.y + storable.dimensions.height) / nodeDimensions.height).toInt
    )

    for {
      xAt <- startNodeX to endNodeX
      yAt <- startNodeY to endNodeY
    } yield {
      printlnd(s"Inserting $storable at $xAt, $yAt")
      if (nodes(xAt)(yAt) == null)
        nodes(xAt)(yAt) = new mutable.HashSet()
      nodes(xAt)(yAt) += storable
    }
  }

  def remove(storable: MapStorable[A]): Unit = {
    val (startNodeX, startNodeY) = (
      Math.floor(storable.position.x / nodeDimensions.width).toInt,
      Math.floor(storable.position.y / nodeDimensions.height).toInt
    )

    val (endNodeX, endNodeY) = (
      Math.ceil((storable.position.x + storable.dimensions.width) / nodeDimensions.width).toInt,
      Math.ceil((storable.position.y + storable.dimensions.height) / nodeDimensions.height).toInt
    )

    for {
      xAt <- startNodeX to endNodeX
      yAt <- startNodeY to endNodeY
    } yield {
      nodes(xAt)(yAt) -= storable
    }
  }

  def get(x: Float, y: Float): mutable.Set[MapStorable[A]] = {
    printlnd(s"Get $x, $y returns ${(x / nodeDimensions.width).toInt}, ${(y / nodeDimensions.height).toInt}")
    nodes((x / nodeDimensions.width).toInt)((y / nodeDimensions.height).toInt)
  }
}
