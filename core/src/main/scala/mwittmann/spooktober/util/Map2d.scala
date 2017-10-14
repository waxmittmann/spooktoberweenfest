package mwittmann.spooktober.util

import com.badlogic.gdx.math.Rectangle
import mwittmann.spooktober.entity.{Player, Zombie}
import mwittmann.spooktober.pipeline.state.ViewState
import mwittmann.spooktober.unit.{Dimensions2df, Position2df}
import mwittmann.spooktober.util.MathUtils.Angle

import scala.collection.{immutable, mutable}
import scala.reflect.ClassTag

case class MapStorable[+S](
  position: Position2df,
  dimensions: Dimensions2df,
  rotation: Float = 0.0f,
  item: S
)(implicit ev: ClassTag[S]) {
  lazy val asRectangle = new Rectangle(
    position.x, position.y,
    dimensions.width, dimensions.height
  )
}

class Map2d[A](
  mapDimensions: Dimensions2df,
  nodeDimensions: Dimensions2df
)(implicit ct: ClassTag[A]) {

  def setRotation(item: A, newAngle: Float): Unit = {
    val cur: MapStorable[A] = getEntityUnsafe(item)
    assert(remove(item))
    insert(cur.copy(rotation = newAngle))
  }

  def insertIfPossible(value: MapStorable[A]): Boolean = {
    if (inBounds(value) && checkCollision(value).isEmpty) {
      insert(value)
      true
    } else false
  }

  def moveIfPossible(entity: A, newLoc: Position2df) = {
    val curStorable = getEntity(entity).getOrElse(throw new Exception("Can't move, no such entity on map"))
    val newStorable = curStorable.copy(position = newLoc)

    if (inBounds(newStorable) && checkCollision(newStorable).isEmpty) {
      move(newStorable)
      true
    } else {
      false
    }
  }

  assert(mapDimensions.width > 0)
  assert(mapDimensions.width >= nodeDimensions.width)
  assert(mapDimensions.height > 0)
  assert(mapDimensions.height >= nodeDimensions.height)

  val horizontalNodeNr = Math.ceil(mapDimensions.width / nodeDimensions.width).toInt
  val verticalNodeNr = Math.ceil(mapDimensions.height / nodeDimensions.width).toInt

  val nodes = Array.ofDim[Set[MapStorable[A]]](horizontalNodeNr, verticalNodeNr)

  val itemToNodes: mutable.HashMap[A, Set[NodeIndex]] = new mutable.HashMap[A, Set[NodeIndex]]()
  val itemToMapStorable: mutable.HashMap[A, MapStorable[A]] = new mutable.HashMap[A, MapStorable[A]]()

  case class NodeIndex(x: Int, y: Int)

  def checkCollision[S](storable: MapStorable[A])(implicit ev: ClassTag[S]): Set[MapStorable[A]] = {
    getNodes(storable).filter(_.asRectangle.overlaps(storable.asRectangle))
  }

  def inBounds(newZombiePos: Position2df, dimensions: Dimensions2df): Boolean = {
    newZombiePos.x >= 0 &&
      newZombiePos.y >= 0 &&
      (newZombiePos.x + dimensions.width) < mapDimensions.width &&
      (newZombiePos.y + dimensions.height) < mapDimensions.height
  }

  def inBounds(mapStorable: MapStorable[A]): Boolean = {
    inBounds(mapStorable.position, mapStorable.dimensions)
  }

  def move(newLoc: MapStorable[A]): Unit = {
    assert(remove(newLoc.item))
    insert(newLoc)
  }

  def move(entity: A, newLoc: Position2df, angle: Option[Angle] = None): Unit = {
    val itms = itemToMapStorable.size
    val itn = itemToNodes.size

    itemToMapStorable.get(entity).map { (mapStorable: MapStorable[A]) => {
      val newStorable = {
        val ms = mapStorable.copy(position = newLoc)
        angle.map(a => ms.copy(rotation = a)).getOrElse(ms)
      }
      assert(remove(entity))
      insert(newStorable)
    }}.getOrElse(throw new Exception(s"Can't move $entity because it's not on the map!"))

    assert(itms == itemToMapStorable.size)
    assert(itn == itemToNodes.size)
  }

  def getEntity(player: A): Option[MapStorable[A]] = {
    itemToMapStorable.get(player)
  }

  def getEntityUnsafe[S <: A](item: S): MapStorable[S] = {
    itemToMapStorable(item).asInstanceOf[MapStorable[S]] // I guess this is safe, but :(
  }

  def insert(item: A, position2df: Position2df, dimensions2df: Dimensions2df): Unit =
    insert(MapStorable(position2df, dimensions2df, 0, item))

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

    if (startNodeX < 0 || startNodeY < 0 || endNodeX >= horizontalNodeNr || endNodeY >= verticalNodeNr) {
      val msg =
        s"""Can't add, outside node range $startNodeX, $startNodeY, $endNodeX, $endNodeY
           |Storable: ${storable}
         """.stripMargin
      throw new Exception(msg)
    }

    val nodesPartOf: immutable.Seq[NodeIndex] =
      for {
        xAt <- startNodeX to endNodeX
        yAt <- startNodeY to endNodeY
      } yield {
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

  def getNode(x: Float, y: Float): Set[MapStorable[A]] = {
    nodes((x / nodeDimensions.width).toInt)((y / nodeDimensions.height).toInt)
  }

  def getNodes(storable: MapStorable[A]): Set[MapStorable[A]] = {
    val rect = storable.asRectangle
    getNodes(storable.position.x, storable.position.y, storable.dimensions.width, storable.dimensions.height)
      .filter(other => other.item != storable.item && other.asRectangle.overlaps(rect))
  }

  def getNodes(view: ViewState): Set[MapStorable[A]] =
    getNodes(view.viewPositionGame, view.viewDimensionsGame)

  def getNodes(xStart: Float, yStart: Float, width: Float, height: Float): Set[MapStorable[A]] =
    getNodes(Position2df(xStart, yStart), Dimensions2df(width, height))

  def getNodes(position: Position2df, dimensions2df: Dimensions2df): Set[MapStorable[A]] = {
    val (xStart, yStart) = (position.x, position.y)
    val (width, height) = (dimensions2df.width, dimensions2df.height)

    val adjustedXStart = if (xStart >= 0) xStart else 0
    val adjustedYStart = if (yStart >= 0) yStart else 0

    val xLimit = Math.min(
      ((xStart + width) / nodeDimensions.width).toInt,
      nodes.length - 1
    )

    val yLimit = Math.min(
      ((yStart + height) / nodeDimensions.height).toInt,
      nodes(0).length - 1
    )

    (for {
      x <- (adjustedXStart / nodeDimensions.width).toInt to xLimit
      y <- (adjustedYStart / nodeDimensions.height).toInt to yLimit
    } yield {
      if (nodes(x)(y) == null) Set.empty[MapStorable[A]] else nodes(x)(y)
    }).toSet.flatten
  }

}
