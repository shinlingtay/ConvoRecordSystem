package ch.makery.address.view

import ch.makery.address.model.Student
import ch.makery.address.util.DateUtil._
import scalafx.scene.control.{Alert, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.event.ActionEvent

@sfxml
class StudentEditDialogController(
                                   private val studentNameField: TextField,
                                   private val studentICField: TextField,
                                   private val dobField: TextField,
                                   private val genderField: TextField,
                                   private val nationalityField: TextField,
                                   private val contactNumberField: TextField,
                                   private val emailField: TextField
                                 ) {

  var dialogStage: Stage = _
  private var _student: Student = _
  var okClicked = false

  def student: Student = _student
  def student_=(student: Student): Unit = {
    _student = student
    studentNameField.text = student.StudentName.value
    studentICField.text = student.StudentIC.value
    dobField.text = student.dob.value.asString
    dobField.promptText = "dd.mm.yyyy"
    genderField.text = student.gender.value
    nationalityField.text = student.nationality.value
    contactNumberField.text = student.contactNumber.value
    emailField.text = student.email.value
  }

  def handleOk(action: ActionEvent): Unit = {
    if (isInputValid()) {
      _student.StudentName.value = studentNameField.text.value
      _student.StudentIC.value = studentICField.text.value
      _student.dob.value = dobField.text.value.parseLocalDate
      _student.gender.value = genderField.text.value
      _student.nationality.value = nationalityField.text.value
      _student.contactNumber.value = contactNumberField.text.value
      _student.email.value = emailField.text.value

      okClicked = true
      dialogStage.close()
    }
  }

  def handleCancel(action: ActionEvent): Unit = {
    dialogStage.close()
  }

  private def isNullOrEmpty(value: String): Boolean = Option(value).forall(_.isEmpty)

  private def isInputValid(): Boolean = {
    val errorMessages = Seq(
      if (isNullOrEmpty(studentNameField.text.value)) "Invalid Student name!" else "",
      if (isNullOrEmpty(studentICField.text.value)) "Invalid Student IC!" else "",
      if (isNullOrEmpty(dobField.text.value)) "Invalid date of birth!" else if (!dobField.text.value.isValid) "Invalid date format. Use dd.mm.yyyy!" else "",
      if (isNullOrEmpty(genderField.text.value)) "Invalid gender!" else "",
      if (isNullOrEmpty(nationalityField.text.value)) "Invalid nationality!" else "",
      if (isNullOrEmpty(contactNumberField.text.value)) "Invalid contact number!" else "",
      if (isNullOrEmpty(emailField.text.value)) "Invalid email!" else ""
    ).filter(_.nonEmpty)

    if (errorMessages.isEmpty) true
    else {
      new Alert(Alert.AlertType.Error) {
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessages.mkString("\n")
      }.showAndWait()
      false
    }
  }
}
