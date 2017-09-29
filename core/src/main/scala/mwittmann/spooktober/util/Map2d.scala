package mwittmann.spooktober.util

import mwittmann.spooktober.entity.Player
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.Map2dO.printlnd

import scala.collection.{immutable, mutable}

object Map2dO {
  val debug = false
  def printlnd(str: String) = if (debug) println(str) else ()
}

case class MapStorable[+S](position: Position2df, dimensions: Dimensions2df, item: S)

class Map2d[A](
  mapDimensions: Dimensions2df,
  nodeDimensions: Dimensions2df
) {

  def inBounds(mapStorable: MapStorable[A]): Boolean = {
    (mapStorable.position.x >= 0 &&
      mapStorable.position.y >= 0 &&
      mapStorable.position.x + mapStorable.dimensions.width < mapDimensions.width &&
      mapStorable.position.y + mapStorable.dimensions.height <= mapDimensions.height)
  }

  def move(entity: A, newLoc: MapStorable[A]): Unit = {
    assert(remove(entity))
    insert(newLoc)
  }

  def getEntity(player: A): Option[MapStorable[A]] = {
    itemToMapStorable.get(player)
  }

  def getEntityUnsafe(player: A): MapStorable[A] = {
    itemToMapStorable(player)
  }

  assert(mapDimensions.width > 0)
  assert(mapDimensions.width >= nodeDimensions.width)
  assert(mapDimensions.height > 0)
  assert(mapDimensions.height  >= nodeDimensions.height)

  val horizontalNodeNr = Math.ceil(mapDimensions.width / nodeDimensions.width).toInt
  val verticalNodeNr = Math.ceil(mapDimensions.height / nodeDimensions.width).toInt

  val nodes = Array.ofDim[Set[MapStorable[A]]](horizontalNodeNr, verticalNodeNr)

  case class NodeIndex(x: Int, y: Int)

  val itemToNodes: mutable.HashMap[A, Set[NodeIndex]] = new mutable.HashMap[A, Set[NodeIndex]]()
  val itemToMapStorable: mutable.HashMap[A, MapStorable[A]] = new mutable.HashMap[A, MapStorable[A]]()

  def insert(storable: MapStorable[A]): Unit = {
    if (itemToNodes.contains(storable.item)) {
      throw new Exception(s"Map already contains ${storable.item}")
    }

    itemToMapStorable += (storable.item -> storable)

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

      itemToMapStorable -= item
      itemToNodes -= item

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
