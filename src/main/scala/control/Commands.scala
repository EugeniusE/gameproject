import Decks._
import scala.util.Try
import scala.collection.mutable.ArrayBuffer

trait Command {
  def execute(): Try[Unit]
  def undo(): Try[Unit]
}

class HitCommand(player: Player, controller: Controller) extends Command {
  var card : Option[Card] = None
  var prevState: Ergebnis = controller.table.outcome
  override def execute(): Try[Unit] = Try{
    card  = Some(controller.drawNewCard())
    player.addCard(card.get)

    if (controller.game.evalStrat.evaluateHand(controller.table.player.getHand()) > 21) {
     controller.table.outcome = Ergebnis.DealerWin
    }
    
  }

  override def undo(): Try[Unit] = Try{

    card match{
      case Some(card) =>
        val index = controller.table.player.hand.indexOf(card)
        controller.table.player.hand.remove(index)
      case None => //keine karte entfernen
    }
    card = None
    controller.table.outcome = prevState
  }
  
}

class StandCommand(controller: Controller) extends Command {
  private var dealerInitHand: Option[List[Card]] = None
  var prevState: Ergebnis = controller.table.outcome
  override def execute(): Try[Unit] = Try{
    dealerInitHand = Some(controller.table.getDealerHand().toList)

    while (controller.game.evalStrat.evaluateHand(controller.table.getDealerHand()) < 17) {
      val card = controller.drawNewCard()
      controller.table.addDealerHand(card)
    }

    val dealerScore = controller.game.evalStrat.evaluateHand(controller.table.getDealerHand())
    val playerScore = controller.game.evalStrat.evaluateHand(controller.table.player.getHand())
    

    if (dealerScore > 21 || dealerScore < playerScore) {
      controller.table.outcome  = Ergebnis.PlayerWin
    } else if (dealerScore == playerScore) {
     controller.table.outcome =  Ergebnis.Draw
    } else {
      controller.table.outcome =  Ergebnis.DealerWin
    }

  }

  override def undo(): Try[Unit] = Try{
    dealerInitHand match{
      case Some(value) =>
         controller.table.clearDealerhand()
         for(card <- value){
          controller.table.addDealerHand(card)
         }

      case None => //kein undo 
    }
    dealerInitHand = None
    controller.table.outcome = prevState
  }
}
