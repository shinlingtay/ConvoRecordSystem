package ch.makery.address.view
import ch.makery.address.MainApp
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent

@sfxml
class WelcomeController {

  def goToStudent(action: ActionEvent) = {
    MainApp.showStudent();
  }

  def goToEvent(action: ActionEvent) = {
    MainApp.showEvent()
  }

  def goToAbsence(action: ActionEvent) = {
    MainApp.showAbsence();
  }

  def goToLogin(action: ActionEvent) = {
    MainApp.showLogin();
  }

}
