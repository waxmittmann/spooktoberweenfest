package mwittmann.spooktober.util

import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.Map2dO.printlnd

import scala.collection.{immutable, mutable}

object Map2dO {
  val debug = false

  def printlnd(str: String) = if (debug) println(str) else ()

  def main(args: Array[String]): Unit = {
    val map2d = new Map2d[String](Dimensions2df(100.0f, 100.0f), Dimensions2df(5.0f, 5.0f))

    map2d.insert(map2d.MapStorable(Position2df(0, 0), Dimensions2df(4.9f, 4.9f), "ll"))
    map2d.insert(map2d.MapStorable(Position2df(0, 5.0f), Dimensions2df(4.9f, 4.9f), "ul"))
    map2d.insert(map2d.MapStorable(Position2df(5.0f, 0), Dimensions2df(4.9f, 4.9f), "lr"))
    map2d.insert(map2d.MapStorable(Position2df(5.0f, 5.0f), Dimensions2df(4.9f, 4.9f), "ur"))
    map2d.insert(map2d.MapStorable(Position2df(0, 0), Dimensions2df(10, 10), "a"))

    println(map2d.getNode(0.1f, 0.1f)) //ll, a
    println(map2d.getNode(4.9f, 4.9f)) // ll, a

    println(map2d.getNode(0.1f, 5.1f)) //ul, a
    println(map2d.getNode(4.9f, 9.9f)) //ul, a

    println(map2d.getNode(5.1f, 0.1f)) //lr, a
    println(map2d.getNode(9.9f, 4.9f)) //lr, a

    println(map2d.getNode(5.1f, 5.1f))
    println(map2d.getNode(9.9f, 9.9f))
  }

}

class Map2d[A](
  mapDimensions: Dimensions2df,
  nodeDimensions: Dimensions2df
) {
  case class MapStorable[S >: A](position: Position2df, dimensions: Dimensions2df, item: S)

  assert(mapDimensions.width > 0)
  assert(mapDimensions.width >= nodeDimensions.width)
  assert(mapDimensions.height > 0)
  assert(mapDimensions.height  >= nodeDimensions.height)

  val horizontalNodeNr = Math.ceil(mapDimensions.width / nodeDimensions.width).toInt
  val verticalNodeNr = Math.ceil(mapDimensions.height / nodeDimensions.width).toInt

  val nodes = Array.ofDim[Set[MapStorable[A]]](horizontalNodeNr, verticalNodeNr)

//  /type NodeIndex = (Int, Int)

  case class NodeIndex(x: Int, y: Int)

  val itemToNodes: mutable.HashMap[A, Set[NodeIndex]] = new mutable.HashMap[A, Set[NodeIndex]]()

  def insert[S <: A](storable: MapStorable[S]): Unit = {
    if (itemToNodes.contains(storable.item)) {
      throw new Exception(s"Map already contains ${storable.item}")
    }

    val (startNodeX, startNodeY) = (
      Math.floor(storable.position.x / nodeDimensions.width).toInt,
      Math.floor(storable.position.y / nodeDimensions.height).toInt
    )

    val (endNodeX, endNodeY) = (
      Math.floor((storable.position.x + storable.dimensions.width) / nodeDimensions.width).toInt,
      Math.floor((storable.position.y + storable.dimensions.height) / nodeDimensions.height).toInt
    )

    val nodesPartOf: immutable.Seq[NodeIndex] =
      for {
        xAt <- startNodeX to endNodeX
        yAt <- startNodeY to endNodeY
      } yield {
        printlnd(s"Inserting $storable at $xAt, $yAt")

        if (nodes(xAt)(yAt) == null)
          nodes(xAt)(yAt) = Set(storable)
        else
          nodes(xAt)(yAt) = nodes(xAt)(yAt) + storable

        NodeIndex(xAt, yAt)
      }

    itemToNodes.put(storable.item, nodesPartOf.toSet)
  }

  def remove(item: A): Boolean = {
    itemToNodes.get(item).exists(nodeIndices => {
      nodeIndices.foreach(nodeAt => {
        val withoutItem = nodes(nodeAt.x)(nodeAt.y).filterNot(_.item == item)

        if (withoutItem.size > nodes(nodeAt.x)(nodeAt.y).size - 1)
          throw new Exception("Failed to find item where expected")

        else if (withoutItem.size < nodes(nodeAt.x)(nodeAt.y).size - 1)
          throw new Exception("Removed more items than expected")

        nodes(nodeAt.x)(nodeAt.y) = withoutItem
      })
      true
    })
  }

//  def remove(storable: MapStorable[A]): Unit = {
//    val (startNodeX, startNodeY) = (
//      Math.floor(storable.position.x / nodeDimensions.width).toInt,
//      Math.floor(storable.position.y / nodeDimensions.height).toInt
//    )
//
//    val (endNodeX, endNodeY) = (
//      Math.ceil((storable.position.x + storable.dimensions.width) / nodeDimensions.width).toInt,
//      Math.ceil((storable.position.y + storable.dimensions.height) / nodeDimensions.height).toInt
//    )
//
//    for {
//      xAt <- startNodeX to endNodeX
//      yAt <- startNodeY to endNodeY
//    } yield {
//      nodes(xAt)(yAt) -= storable
//    }
//  }

  def getNode(x: Float, y: Float): Set[MapStorable[A]] = {
    printlnd(s"Get $x, $y returns ${(x / nodeDimensions.width).toInt}, ${(y / nodeDimensions.height).toInt}")
    nodes((x / nodeDimensions.width).toInt)((y / nodeDimensions.height).toInt)
  }

  def getNodes(xStart: Float, yStart: Float, width: Float, height: Float): Set[MapStorable[A]] = {
    val xLimit = Math.min(
      ((xStart + width) / nodeDimensions.width).toInt,
      nodes.length
    )

    val yLimit = Math.min(
      ((yStart + height) / nodeDimensions.height).toInt,
      nodes(0).length
    )

    val x2: immutable.Seq[Set[MapStorable[A]]] = for {
      x <- (xStart / nodeDimensions.width).toInt to xLimit
      y <- (yStart / nodeDimensions.height).toInt to yLimit
    } yield {
      val x1: Set[MapStorable[A]] =  nodes(x)(y)
      x1
    }

    val x3: Set[Set[MapStorable[A]]] = x2.toSet

    val x4: Set[MapStorable[A]] = x3.flatten

      //.toSet.flatten
    x4
  }

}
