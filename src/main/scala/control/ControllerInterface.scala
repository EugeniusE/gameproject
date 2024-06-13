import Decks.Card
import Decks.Deck
import util.Observer
import util.Observable
import scala.collection.mutable.ArrayBuffer

trait ControllerInterface extends Observable {
  def newGame(): Unit
  def nextRound(): Unit
  def hit(): Unit
  def stand(): Unit
  def drawNewCard(): Card
  def evaluateHand(hand: ArrayBuffer[Card]): Int
  def executeCommand(command: Command): Unit
  def undoLastCommand(): Unit
  def redoLastUndoneCommand(): Unit
  def getOutcome(): Ergebnis
  def setOutcome(e: Ergebnis): Unit
  def getDealerHand(): ArrayBuffer[Card]
  def addDealerHand(card: Card): Unit
  def clearDealerhand(): Unit
  def getPlayerHand(): ArrayBuffer[Card]
  def addPlayerHand(card: Card): Unit
  def removePlayerHand(card: Card): Unit
  def getPlayerName(): String
}

