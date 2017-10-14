package mwittmann.spooktober.util

import mwittmann.spooktober.unit.{Position2df, Unit2d, Vector2df}

object MathUtils {

  type Angle = Float

  type Array2d[T] = Array[Array[T]]

  val upVector = Vector2df(0, 1)

  val origin = Position2df(0, 0)

  //def getAngle(unit2d: Unit2d[Float]): Float = getAngle(origin, unit2d)
//  def getAngle(unit2d: Unit2d[Float]): Float = {
//    val angle = Math.toDegrees(Math.acos(unit2d.xpart * unit2d.ypart)).toFloat
//
//    if (angle < 0)
//      angle + 360
//    else
//      angle
//  }

//  def getAngle2(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
//    val angle = Math.atan2(v2.xpart, v2.ypart) - Math.atan2(v1.xpart, v1.ypart)
//    Math.toDegrees(angle).toFloat
//  }

//  def getAngle2(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
//    val dp = dotProduct(v1, v2)
//    val m = magnitude(v1, v2)
//    println(s"For $v1, $v2, dp and m are $dp and $m")
//    Math.toDegrees(Math.acos(dp / m)).toFloat
//  }


//  def getAngle2(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
//    val dp = dotProduct(v1, v2)
//    val det = determinant(v1, v2)
//    val angle = Math.toDegrees(Math.atan2(det, dp)).toFloat
//    angle
//  }

  def getAngle2(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
    val a1 = Math.atan2(v1.xpart, v1.ypart)
    val a2 = Math.atan2(v2.xpart, v2.ypart)
    //val a = if (a1 - a2 >= 0)  a1 - a2 else (a1 - a2) + 360
    val a = Math.toDegrees(a1 - a2).toFloat

    if (a < 0)
      a + 360
    else a
  }

  def dotProduct(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
    v1.xpart * v2.xpart + v1.ypart * v2.ypart
  }

  def magnitude(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
    (Math.sqrt(v1.xpart * v1.xpart + v1.ypart * v1.ypart) * Math.sqrt(v2.xpart * v2.xpart + v2.ypart * v2.ypart)).toFloat
  }

  def determinant(v1: Unit2d[Float], v2: Unit2d[Float]): Float = {
    v1.xpart * v2.ypart - v1.ypart * v2.xpart
  }

  def getAngle(center: Unit2d[Float], mouse: Unit2d[Float]): Float = {
    var angle =
      Math.toDegrees(Math.atan2(mouse.ypart - center.ypart, mouse.xpart - center.xpart)).toFloat //- 90 // Because I want 'up' to be 0 degrees

    if (angle < 0)
      angle + 360
    else
      angle
  }

}
