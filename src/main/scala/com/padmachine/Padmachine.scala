package com.padmachine

import javafx.scene.paint.Color

object Padmachine extends App {
  val pad = new Pad
  pad.color = PadColor(Color.RED)
  println(pad.color)
}
