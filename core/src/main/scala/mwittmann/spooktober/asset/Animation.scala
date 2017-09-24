package mwittmann.spooktober.asset

import com.badlogic.gdx.graphics.g2d.TextureRegion


object Animation {
  val ANIMATION_LOOPING = 0
  val ANIMATION_NONLOOPING = 1
}

class Animation(val frameDuration: Float, val keyFrames: Seq[TextureRegion]) {
  def getKeyFrame(stateTime: Float, mode: Int): TextureRegion = {
    var frameNumber = (stateTime / frameDuration).toInt
    if (mode == Animation.ANIMATION_NONLOOPING) frameNumber = Math.min(keyFrames.length - 1, frameNumber)
    else frameNumber = frameNumber % keyFrames.length
    keyFrames(frameNumber)
  }
}
