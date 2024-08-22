package ch.makery.address.view

import ch.makery.address.MainApp
import ch.makery.address.model.Absence
import scalafx.scene.control.{Label, Tab}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.layout.{VBox}

import scala.util.Try

@sfxml
class AbsenceController(
                         private val tabLabelDAY1: Tab,
                         private val tabLabelDAY2: Tab,
                         private val tabLabelDAY3: Tab,
                         private val convocationLabelContainerDAY1: VBox,
                         private val convocationLabelContainerDAY2: VBox,
                         private val convocationLabelContainerDAY3: VBox,
                         private val convocationValueContainerDAY1: VBox,
                         private val convocationValueContainerDAY2: VBox,
                         private val convocationValueContainerDAY3: VBox
                       ) {

  showEventDetails

  private def showEventDetails(): Unit = {
    // Clear existing labels and values for all containers
    convocationValueContainerDAY1.children.clear()
    convocationValueContainerDAY2.children.clear()
    convocationValueContainerDAY3.children.clear()

    // Populate DAY1 event details
    populateEventDetails(tabLabelDAY1.text.value, convocationLabelContainerDAY1, convocationValueContainerDAY1)

    // Populate Cybersecurity event details
    populateEventDetails(tabLabelDAY2.text.value, convocationLabelContainerDAY2, convocationValueContainerDAY2)

    // Populate DAY3 event details
    populateEventDetails(tabLabelDAY3.text.value, convocationLabelContainerDAY3, convocationValueContainerDAY3)
  }

  private def populateEventDetails(eventName: String, convocationLabelContainer: VBox, convocationValueContainer: VBox): Unit = {
    // Fetch the list of (studentName, convocationValue) from getAllEvents
    val convocationData = Absence.getAllEvents(eventName)

    // Populate the labels with student names and corresponding convocation values
    convocationData.foreach { case (studentName, convocationValue) =>
      // Add student name label
      val nameLabel = new Label(studentName) {
        styleClass += "label-bright"
      }
      convocationLabelContainer.children += nameLabel

      // Add convocation value label
      val valueLabel = new Label(convocationValue) {
        styleClass += "label-bright"
      }
      convocationValueContainer.children += valueLabel
    }
  }

  def goToWelcome(action: ActionEvent) = {
    MainApp.showWelcome();
  }
}

