package ch.makery.address.view

import ch.makery.address.model.Event
import scalafx.scene.control.{Alert, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent

@sfxml
class EventEditDialogController(
                                 private val eventField: TextField,
                                 private val timeField: TextField,
                                 private val descriptionField: TextField,
                                 private val emceeField: TextField,
                                 private val asst_emceeField: TextField,
                                 private val speakerField: TextField
                               ) {

  var dialogStage: Stage = _
  private var _event: Event = _
  var okClicked: Boolean = false

  def event: Event = _event
  def event_=(event: Event): Unit = {
    _event = event
    eventField.text = _event.eventname.value
    timeField.text = _event.time.value
    descriptionField.text = _event.description.value
    emceeField.text = _event.emcee.value
    asst_emceeField.text = _event.asstemcee.value
    speakerField.text = _event.speaker.value
  }

  def handleOk(action: ActionEvent): Unit = {
    if (isInputValid()) {
      _event.eventname <== eventField.text
      _event.time = timeField.text
      _event.description <== descriptionField.text
      _event.emcee = emceeField.text
      _event.asstemcee <== asst_emceeField.text
      _event.speaker = speakerField.text
      okClicked = true
      dialogStage.close()
    }
  }

  def handleCancel(action: ActionEvent): Unit = {
    dialogStage.close()
  }

  private def isInputValid(): Boolean = {
    val errorMessage = new StringBuilder

    if (eventField.text.value.isEmpty)
      errorMessage ++= "Invalid event name!\n"
    if (timeField.text.value.isEmpty)
      errorMessage ++= "Invalid time!\n"

    if (errorMessage.isEmpty) {
      true
    } else {
      new Alert(Alert.AlertType.Error) {
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage.toString
      }.showAndWait()
      false
    }
  }
}
