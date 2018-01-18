package com.padmachine

import javafx.scene.paint.Color

class Pad {
  var color: PadColor = PadColor(Color.BLACK)
  var pressed: Boolean = false

  def lit = color == Color.BLACK
} 
