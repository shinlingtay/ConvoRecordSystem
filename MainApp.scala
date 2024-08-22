package ch.makery.address

import ch.makery.address.model.{Event, Login, Student}
import ch.makery.address.util.Database
import ch.makery.address.view._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp {

  // Initialize database
  Database.setupDB()

  val studentData = new ObservableBuffer[Student]()
  studentData ++= Student.getAllStudent

  val eventData = new ObservableBuffer[Event]()
  eventData ++= Event.getAllEvent

  val loginData = new ObservableBuffer[Login]()
  loginData ++= Login.getAllLogin

  private val rootResource = getClass.getResource("view/RootLayout.fxml")
  private val rootLoader = new FXMLLoader(rootResource, NoDependencyResolver)
  rootLoader.load()
  private val rootLayout = rootLoader.getRoot[jfxs.layout.BorderPane]

  // Initialize stage
  stage = new PrimaryStage {
    title = "ConvocationRecordSystem"
    scene = new Scene {
      stylesheets += getClass.getResource("view/ConvocationTheme.css").toString
      root = rootLayout
    }
    icons += new Image(getClass.getResourceAsStream("/images/sunwayuni_logo.png"))
  }

  // Display login window
  def showLogin(): Unit = {
    val loginResource = getClass.getResource("view/Login.fxml")
    val loginLoader = new FXMLLoader(loginResource, NoDependencyResolver)
    loginLoader.load()
    val loginLayout = loginLoader.getRoot[jfxs.layout.BorderPane]
    rootLayout.setCenter(loginLayout)
  }

  def showLoginEditDialog(login: Login): Boolean = {
    val dialogResource = getClass.getResourceAsStream("view/LoginEditDialog.fxml")
    val dialogLoader = new FXMLLoader(null, NoDependencyResolver)
    dialogLoader.load(dialogResource)
    val dialogLayout = dialogLoader.getRoot[jfxs.Parent]
    val controller = dialogLoader.getController[LoginEditDialogController#Controller]

    val dialogStage = new Stage {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        stylesheets += getClass.getResource("view/ConvocationTheme.css").toString
        root = dialogLayout
      }
    }

    controller.dialogStage = dialogStage
    controller.login = login
    dialogStage.showAndWait()
    controller.okClicked
  }

  def showWelcome(): Unit = {
    val welcomeResource = getClass.getResource("view/Welcome.fxml")
    val welcomeLoader = new FXMLLoader(welcomeResource, NoDependencyResolver)
    welcomeLoader.load()
    val welcomeLayout = welcomeLoader.getRoot[jfxs.layout.AnchorPane]
    rootLayout.setCenter(welcomeLayout)
  }

  def showStudent(): Unit = {
    val studentResource = getClass.getResource("view/Student.fxml")
    val studentLoader = new FXMLLoader(studentResource, NoDependencyResolver)
    studentLoader.load()
    val studentLayout = studentLoader.getRoot[jfxs.layout.AnchorPane]
    rootLayout.setCenter(studentLayout)
    studentLoader.getController[StudentController#Controller]
  }

  def showStudentEditDialog(student: Student): Boolean = {
    val dialogResource = getClass.getResourceAsStream("view/StudentEditDialog.fxml")
    val dialogLoader = new FXMLLoader(null, NoDependencyResolver)
    dialogLoader.load(dialogResource)
    val dialogLayout = dialogLoader.getRoot[jfxs.Parent]
    val controller = dialogLoader.getController[StudentEditDialogController#Controller]

    val dialogStage = new Stage {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        stylesheets += getClass.getResource("view/ConvocationTheme.css").toString
        root = dialogLayout
      }
    }

    controller.dialogStage = dialogStage
    controller.student = student
    dialogStage.showAndWait()
    controller.okClicked
  }

  def showAbsence(): Unit = {
    val absenceResource = getClass.getResource("view/Absence.fxml")
    val absenceLoader = new FXMLLoader(absenceResource, NoDependencyResolver)
    absenceLoader.load()
    val absenceLayout = absenceLoader.getRoot[jfxs.layout.AnchorPane]
    rootLayout.setCenter(absenceLayout)
    absenceLoader.getController[AbsenceController#Controller]
  }

  def showEvent(): Unit = {
    val eventResource = getClass.getResource("view/Event.fxml")
    val eventLoader = new FXMLLoader(eventResource, NoDependencyResolver)
    eventLoader.load()
    val eventLayout = eventLoader.getRoot[jfxs.layout.AnchorPane]
    rootLayout.setCenter(eventLayout)
    eventLoader.getController[EventController#Controller]
  }

  def showEventEditDialog(event: Event): Boolean = {
    val dialogResource = getClass.getResourceAsStream("view/EventEditDialog.fxml")
    val dialogLoader = new FXMLLoader(null, NoDependencyResolver)
    dialogLoader.load(dialogResource)
    val dialogLayout = dialogLoader.getRoot[jfxs.Parent]
    val controller = dialogLoader.getController[EventEditDialogController#Controller]

    val dialogStage = new Stage {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        stylesheets += getClass.getResource("view/ConvocationTheme.css").toString
        root = dialogLayout
      }
    }

    controller.dialogStage = dialogStage
    controller.event = event
    dialogStage.showAndWait()
    controller.okClicked
  }

  // Show the login screen at application start
  showLogin()
}