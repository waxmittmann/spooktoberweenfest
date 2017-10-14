package mwittmann.spooktober.pipeline.state

import org.specs2.Specification

object ViewStateSpec extends Specification {

  override def is =
    s2"""ViewState
        translate followed by untranslate should be identity $translateAndUntranslateShouldBeIdentity
        translate should correct $translateShouldCorrect
      """

  def translateAndUntranslateShouldBeIdentity = {
    val view = ViewState(
      gameX = 13.5f,
      gameY = 26.2f,
      gameWidth = 200.0f,
      gameHeight = 300.0f,
      screenX = 50.0f,
      screenY = 33.0f,
      screenWidth = 1000.0f,
      screenHeight = 1000.0f
    )

    println(view.translateX(85f))
    println(view.untranslateX(view.translateX(85f).toInt))

    view.untranslateX(view.translateX(85f).toInt) mustEqual 85f
    view.untranslateY(view.translateY(85f).toInt) mustEqual 85f
  }

  def translateShouldCorrect = {
    val view = ViewState(
      gameX = 13.5f,
      gameY = 26.2f,
      gameWidth = 200.0f,
      gameHeight = 300.0f,
      screenX = 50.0f,
      screenY = 30.0f,
      screenWidth = 1000.0f,
      screenHeight = 1000.0f
    )

    view.translateX(85f) mustEqual 407.5
    view.translateY(85f) mustEqual 226
  }

}
