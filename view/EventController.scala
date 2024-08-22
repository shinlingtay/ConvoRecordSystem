package ch.makery.address.view

import ch.makery.address.MainApp
import ch.makery.address.model.Event
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafxml.core.macros.sfxml

@sfxml
class EventController(
                       private val eventTable: TableView[Event],
                       private val eventColumn: TableColumn[Event, String],
                       private val timeColumn: TableColumn[Event, String],
                       private val eventNameLabel: Label,
                       private val timeLabel: Label,
                       private val descriptionLabel: Label,
                       private val emceeLabel: Label,
                       private val asstemceeLabel: Label,
                       private val speakerLabel: Label
                     ) {

  // Initialize table view and columns
  eventTable.items = MainApp.eventData
  eventColumn.cellValueFactory = _.value.eventname
  timeColumn.cellValueFactory = _.value.time

  private def showEventDetails(event: Option[Event]): Unit = {
    event match {
      case Some(e) =>
        eventNameLabel.text <== e.eventname
        timeLabel.text <== e.time
        descriptionLabel.text = e.description.value
        emceeLabel.text = e.emcee.value
        asstemceeLabel.text = e.asstemcee.value
        speakerLabel.text = e.speaker.value

      case None =>
        List(eventNameLabel, timeLabel, descriptionLabel, emceeLabel, asstemceeLabel, speakerLabel).foreach { label =>
          label.text.unbind()
          label.text = ""
        }
    }
  }

  showEventDetails(None)

  // Update event details when a new event is selected
  eventTable.selectionModel().selectedItem.onChange((_, _, newValue) => showEventDetails(Option(newValue)))

  def handleDeleteEvent(action: ActionEvent): Unit = {
    val selectedIndex = eventTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      MainApp.eventData.remove(selectedIndex).delete()
    } else {
      new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Event Selected"
        contentText = "Please select an event in the table."
      }.showAndWait()
    }
  }

  def handleAddEvent(action: ActionEvent): Unit = {
    val event = new Event("")
    if (MainApp.showEventEditDialog(event)) {
      MainApp.eventData += event
      event.save()
    }
  }

  def handleEditEvent(action: ActionEvent): Unit = {
    val selectedEvent = eventTable.selectionModel().selectedItem.value
    if (selectedEvent != null) {
      if (MainApp.showEventEditDialog(selectedEvent)) {
        showEventDetails(Some(selectedEvent))
        selectedEvent.save()
        eventTable.refresh()
      }
    } else {
      new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Event Selected"
        contentText = "Please select an event in the table."
      }.showAndWait()
    }
  }

  def goToWelcome(action: ActionEvent): Unit = {
    MainApp.showWelcome()
  }
}
