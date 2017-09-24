package mwittmann.spooktober.util

import mwittmann.spooktober.util.Map2dO.{printlnd, MapStorable}

import scala.collection.mutable

object Map2dO {
  val debug = false

  def printlnd(str: String) = if (debug) println(str) else ()

  trait HasPosition {
    val x: Float
    val y: Float
  }

  trait HasDimensions {
    val width: Float
    val height: Float
  }

  case class Position(x: Float, y: Float) extends HasPosition
  case class Dimensions(width: Float, height: Float) extends HasDimensions

  case class MapStorable[A](position: HasPosition, dimensions: HasDimensions, item: A)


  def main(args: Array[String]): Unit = {
    val map2d = new Map2d[String](100.0f, 100.0f, 5.0f, 5.0f)

    map2d.insert(MapStorable(Position(0, 0), Dimensions(4.9f, 4.9f), "ll"))
    map2d.insert(MapStorable(Position(0, 5.0f), Dimensions(4.9f, 4.9f), "ul"))
    map2d.insert(MapStorable(Position(5.0f, 0), Dimensions(4.9f, 4.9f), "lr"))
    map2d.insert(MapStorable(Position(5.0f, 5.0f), Dimensions(4.9f, 4.9f), "ur"))
    map2d.insert(MapStorable(Position(0, 0), Dimensions(10, 10), "a"))

    println(map2d.get(0.1f, 0.1f)) //ll, a
    println(map2d.get(4.9f, 4.9f)) // ll, a

    println(map2d.get(0.1f, 5.1f)) //ul, a
    println(map2d.get(4.9f, 9.9f)) //ul, a

    println(map2d.get(5.1f, 0.1f)) //lr, a
    println(map2d.get(9.9f, 4.9f)) //lr, a

    println(map2d.get(5.1f, 5.1f))
    println(map2d.get(9.9f, 9.9f))


    //    map2d.insert(MapStorable(Position(0, 0), Dimensions(20, 20), "a"))
//    map2d.insert(MapStorable(Position(15, 15), Dimensions(5, 5), "b"))
//    map2d.insert(MapStorable(Position(10, 10), Dimensions(1, 1), "c"))

//    println(map2d.get(0.0f, 0.0f))
//    println(map2d.get(10.0f, 10.0f))
//    println(map2d.get(0.0f, 0.0f))
//    println(map2d.get(11.0f, 11.0f))
//    println(map2d.get(15.0f, 15.0f))
//    println(map2d.get(16.0f, 16.0f))
  }

}

class Map2d[A](
  mapWidth: Float,
  mapHeight: Float,
  nodeWidth: Float,
  nodeHeight: Float
) {
  assert(mapWidth > 0)
  assert(mapWidth >= nodeWidth)
  assert(mapHeight > 0)
  assert(mapHeight >= nodeHeight)

  val horizontalNodeNr = Math.ceil(mapWidth / nodeWidth).toInt
  val verticalNodeNr = Math.ceil(mapWidth / nodeWidth).toInt

  val nodes = Array.ofDim[mutable.Set[MapStorable[A]]](horizontalNodeNr, verticalNodeNr)

  def insert(storable: MapStorable[A]): Unit = {
    val (startNodeX, startNodeY) = (
      Math.floor(storable.position.x / nodeWidth).toInt,
      Math.floor(storable.position.y / nodeHeight).toInt
    )

    val (endNodeX, endNodeY) = (
      Math.floor((storable.position.x + storable.dimensions.width) / nodeWidth).toInt,
      Math.floor((storable.position.y + storable.dimensions.height) / nodeHeight).toInt
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
      Math.floor(storable.position.x / nodeWidth).toInt,
      Math.floor(storable.position.y / nodeHeight).toInt
    )

    val (endNodeX, endNodeY) = (
      Math.ceil((storable.position.x + storable.dimensions.width) / nodeWidth).toInt,
      Math.ceil((storable.position.y + storable.dimensions.height) / nodeHeight).toInt
    )

    for {
      xAt <- startNodeX to endNodeX
      yAt <- startNodeY to endNodeY
    } yield {
      nodes(xAt)(yAt) -= storable
    }
  }

  def get(x: Float, y: Float): mutable.Set[MapStorable[A]] = {
    printlnd(s"Get $x, $y returns ${(x / nodeWidth).toInt}, ${(y / nodeHeight).toInt}")
    nodes((x / nodeWidth).toInt)((y / nodeHeight).toInt)
  }
}
