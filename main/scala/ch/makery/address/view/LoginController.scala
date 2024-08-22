package ch.makery.address.view
import ch.makery.address.model.Login
import ch.makery.address.MainApp
import scalafx.scene.control.{Alert, TextField}
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent

@sfxml
class LoginController(private val userNameField: TextField,
                      private val passwordField: TextField) {


  def handleAddLogin(action: ActionEvent) = {
    val login = new Login("", "")
    val okClicked = MainApp.showLoginEditDialog(login);
    if (okClicked) {
      MainApp.loginData += login
      login.save()
    }
  }

  def goToWelcome(action: ActionEvent) = {
    if (Login.isExist(userNameS = userNameField.text.value, passwordS = passwordField.text.value) == true){
      MainApp.showWelcome();
    }
    else{
      val alert = new Alert(Alert.AlertType.Error) {
        initOwner(MainApp.stage)
        title = "Login error"
        headerText = "Username or password incorrect"
        contentText = "Please try again"
      }.showAndWait()
    }
  }

  def goToLogin(action: ActionEvent) = {
    MainApp.showLogin();
  }

}
