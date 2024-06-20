import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Pos
import scalafx.Includes._
import javax.print.DocFlavor.INPUT_STREAM
import java.io.FileInputStream
import java.io.InputStream
import javafx.stage.WindowEvent
import scala.compiletime.uninitialized
import scalafx.scene.image.Image
import scalafx.scene.ImageCursor

class GUI(controller: ControllerInterface) extends JFXApp3 with util.Observer {
  controller.add(this)

  private var preGameScene: PreGameScene = uninitialized
  private var gameScene: GameScene = uninitialized
  private var resultScene: ResultScene = uninitialized

  private val windowWidth = 1500
  private val windowHeight = 800

  private val minWindowWidth = 500
  private val minWindowHeight = 300
  private val cursorImage = new Image(new FileInputStream(s"src/main/scala/resources/Chip.png"))

  override def start(): Unit = {
    preGameScene = PreGameScene(controller, windowWidth, windowHeight, () => stage.setScene(gameScene))
    gameScene = GameScene(controller, windowWidth, windowHeight, () => stage.setScene(resultScene))
    gameScene.setCursor(new ImageCursor(cursorImage))
    resultScene = ResultScene(windowWidth, windowHeight, () => stage.setScene(preGameScene))

    // val iconImage = new Image(getClass.getResourceAsStream("/Users/simonkann/Documents/Se/BlackJack/src/main/scala/resources/icon.png"))
    stage = new JFXApp3.PrimaryStage {
      height = windowHeight
      width = windowWidth
      scene = preGameScene
      resizable = true
      title = "Blackjack"
      onCloseRequest = (e: WindowEvent) => {
        println("Window closed")
        System.exit(0)
      }
    }
    controller.newGame()
  }
  def update: Unit = {
    Platform.runLater {
      if (gameScene != null) {
        gameScene.updateGameUI()
      }
    }

  }
}
