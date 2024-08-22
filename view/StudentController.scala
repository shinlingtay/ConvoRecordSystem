package ch.makery.address.view

import ch.makery.address.MainApp
import ch.makery.address.model.Student
import ch.makery.address.util.DateUtil.DateFormater
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafxml.core.macros.sfxml

@sfxml
class StudentController(
                         private val studentTable: TableView[Student],
                         private val studentNameColumn: TableColumn[Student, String],
                         private val studentICColumn: TableColumn[Student, String],
                         private val studentNameLabel: Label,
                         private val studentICLabel: Label,
                         private val dobLabel: Label,
                         private val genderLabel: Label,
                         private val nationalityLabel: Label,
                         private val contactNumberLabel: Label,
                         private val emailLabel: Label
                       ) {

  // Initialize TableView content
  studentTable.items = MainApp.studentData

  // Initialize columns' cell values
  studentNameColumn.cellValueFactory = _.value.StudentName
  studentICColumn.cellValueFactory = _.value.StudentIC

  // Display student details
  private def showStudentDetails(student: Option[Student]): Unit = {
    student match {
      case Some(s) =>
        studentNameLabel.text <== s.StudentName
        studentICLabel.text <== s.StudentIC
        dobLabel.text = s.dob.value.asString
        genderLabel.text <== s.gender
        nationalityLabel.text <== s.nationality
        contactNumberLabel.text <== s.contactNumber
        emailLabel.text <== s.email
      case None =>
        // Clear labels if no student is selected
        Seq(studentNameLabel, studentICLabel, dobLabel, genderLabel, nationalityLabel, contactNumberLabel, emailLabel).foreach { label =>
          label.text.unbind()
          label.text = ""
        }
    }
  }

  // Set initial empty details
  showStudentDetails(None)

  // Update student details when a new student is selected
  studentTable.selectionModel().selectedItem.onChange((_, _, newValue) => showStudentDetails(Option(newValue)))

  def handleDeleteStudent(action: ActionEvent): Unit = {
    val selectedIndex = studentTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      MainApp.studentData.remove(selectedIndex).delete()
    } else {
      showAlert("No Student Selected", "Please select a student in the table.")
    }
  }

  def handleAddStudent(action: ActionEvent): Unit = {
    val newStudent = new Student("", "")
    if (MainApp.showStudentEditDialog(newStudent)) {
      MainApp.studentData += newStudent
      newStudent.save()
    }
  }

  def handleEditStudent(action: ActionEvent): Unit = {
    val selectedStudent = studentTable.selectionModel().selectedItem.value
    if (selectedStudent != null) {
      if (MainApp.showStudentEditDialog(selectedStudent)) {
        showStudentDetails(Some(selectedStudent))
        selectedStudent.save()
      }
    } else {
      showAlert("No Student Selected", "Please select a student in the table.")
    }
  }

  def goToWelcome(action: ActionEvent): Unit = {
    MainApp.showWelcome()
  }

  private def showAlert(header: String, content: String): Unit = {
    new Alert(AlertType.Warning) {
      initOwner(MainApp.stage)
      title = "No Selection"
      headerText = header
      contentText = content
    }.showAndWait()
  }
}
