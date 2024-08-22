package ch.makery.address.view

import ch.makery.address.model.{Login}
import scalafx.scene.control.{Alert, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.event.ActionEvent

@sfxml
class LoginEditDialogController (

                                    private val  username : TextField,
                                    private val   password : TextField,
                                  ){

  var dialogStage : Stage = null
  private var _login : Login = null
  var okClicked = false

  def login = _login
  def login_=(x : Login) {
    _login = x

    username.text = _login.userName.value
    password.text    = _login.password.value
  }

  def handleOk(action :ActionEvent){

    if (isInputValid()) {
      _login.userName <== username.text
      _login.password  <== password.text

      okClicked = true;
      dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
    dialogStage.close();
  }
  def isNullOrEmpty(x: String) = Option(x).forall(_.isEmpty)

  def isInputValid(): Boolean = {
    val errorMessage = Seq(
      if (isNullOrEmpty(username.text.value)) "Please enter username!" else "",
      if (isNullOrEmpty(password.text.value)) "Please enter password!" else ""
    ).filter(_.nonEmpty).mkString("\n")

    if (errorMessage.isEmpty) true
    else {
      new Alert(Alert.AlertType.Error) {
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()
      false
    }
  }
}
