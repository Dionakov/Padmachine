package com.padmachine

import javafx.scene.paint.Color

object PadColor {

  def apply(color: Color) = new PadColor(
    math.min(63, (color.getRed * 64).toInt), 
    math.min(63, (color.getGreen * 64).toInt), 
    math.min(63, (color.getBlue * 64).toInt)
  )
}

case class PadColor(r: Int, g: Int, b: Int) {
  private def isValidColorComponent(c: Int) = c >= 0 && c <= 63
  println(r)
  println(g)
  println(b)
  assert(isValidColorComponent(r))
  assert(isValidColorComponent(g))
  assert(isValidColorComponent(b))

  def ==(that: Color) = this.toFxColor equals that
  def toFxColor = new Color(r / 64, g / 64, b / 64, 1.0)
} 
