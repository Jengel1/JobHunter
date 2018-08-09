/*
 * Copyright (c) 2011-2017, ScalaFX Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ScalaFX Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE SCALAFX PROJECT OR ITS CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections._
import scalafx.event.ActionEvent
import scalafx.geometry.{Orientation, Pos, Insets}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.stage.FileChooser

/**
  * Created by Jengel1 on 7/24/2018.
  */
object JobHunterTestObj extends JFXApp {
    case class Company(
        companyName: String,
        contactName: String,
        contactPhone: String,
        contactEmail: String,
        status: String,
        companyType: String,
        notes: String
    )

    //ArrayBuffer holding all Companies
    val comArray = ArrayBuffer[Company]()

    //ArrayBuffers sorted into tech companies and recruiters
    val techArray = ArrayBuffer[Company]()
    val recArray = ArrayBuffer[Company]()

    //variables to hold index values for selected companies
    var techIndex = 0
    var recIndex = 0

    //ListViews of tech companies and recruiters
    var techList = new ListView(techArray.map(_.companyName))
    techList.selectionModel.apply.selectedItems.onChange {
        techIndex = techList.selectionModel.apply.getSelectedIndex
        val company = techArray(techIndex)
        companyName.text = prop2Str(company.companyName)
        contactName.text = prop2Str(company.contactName)
        contactPhone.text = prop2Str(company.contactPhone)
        contactEmail.text = prop2Str(company.contactEmail)
        status.selectionModel.apply().select(company.status)
        techRB.selected = true  //set company type
        notes.text = prop2Str(company.notes)
        clearSelections(recList)  //clear selections in recList
    }
    var recList = new ListView(recArray.map(_.companyName))
    recList.selectionModel.apply.selectedItems.onChange {
        recIndex = recList.selectionModel.apply.getSelectedIndex
        val company = recArray(recIndex)
        companyName.text = prop2Str(company.companyName)
        contactName.text = prop2Str(company.contactName)
        contactPhone.text = prop2Str(company.contactPhone)
        contactEmail.text = prop2Str(company.contactEmail)
        status.selectionModel.apply().select(company.status)
        recRB.selected = true  //set company type
        notes.text = prop2Str(company.notes)
        clearSelections(techList)  //clear selections in techList
    }

    /*
    Utility methods
     */
    //sort and divide contents of comArray based on companyType attribute
    def sortCompanies() = {
        for(c <- comArray){
            if(c.companyType == "tech"){
                techArray.append(c)
            } else {
                recArray.append(c)
            }
        }
    }
    //empty all current contents of comArray and add contents of techArray and recArray
    def combinedCompanies() = {
        comArray.clear()
        for(c <- techArray){
            comArray.append(c)
        }
        for(c <- recArray){
            comArray.append(c)
        }
    }
    //clear all data fields, return buttons to default setting
    def clearData() = {
        companyName.text = null
        contactName.text = null
        contactPhone.text = null
        contactEmail.text = null
        status.selectionModel.apply.clearSelection
        techRB.selected = true  //default setting
        notes.text = null
    }
    //takes in a String representation of a read only StringProperty and returns a String of just the property value
    def prop2Str(prop:String):String = {
        var result = ""
        val index = prop.lastIndexOf(":")
        result = prop.substring(index + 2, prop.length - 1)
        result
    }
    //refresh ListViews
    def refreshLV() = {
        val techOB = new ObservableBuffer[String]()
        val recOB = new ObservableBuffer[String]()
        for(c <- techArray){
            techOB.append(prop2Str(c.companyName))
        }
        for(c <- recArray){
            recOB.append(prop2Str(c.companyName))
        }
        techList.setItems(techOB)
        recList.setItems(recOB)
    }
    //clear ListView selections
    def clearSelections(lv:ListView[String]) = {
        val index = lv.selectionModel.apply().getSelectedIndex
        lv.selectionModel.apply().clearSelection(index)
    }
    //get company type from RadioButtons
    //used workaround to get past ClassCastException
    //substring of read only property string
    def getType():String = {
        var result = ""
        val btnText = tg.selectedToggle.getValue.toString  //RadioButton@3d5a7cbd[styleClass=radio-button]'Tech Company '
        val index = btnText.indexOf("'")  //45
        val subBtnText = btnText.substring(index + 1, btnText.length - 1)  //Tech Company
        if(subBtnText.equals("Tech Company")){
            result = "tech"
        } else {
            result = "recruiter"
        }
        result
    }

    /*
    Company info TextFields
     */
    val companyName = new TextField
    val contactName = new TextField
    val contactPhone = new TextField
    val contactEmail = new TextField

    /*
    FlowPane holding RadioButtons and ToggleGroup
     */
    val techRB = new RadioButton("Tech Company")
    techRB.selected = true  //default selected radio btn
    val recRB = new RadioButton("Recruiter")
    val tg = new ToggleGroup
    tg.toggles = List(techRB, recRB)
    val flowPane = new FlowPane(Orientation.Horizontal){
        hgap = 10
    }
    flowPane.children = List(techRB, recRB)

    /*
    ComboBox
     */
    val status = new ComboBox(List("Follow Up Required", "Offer Extended", "Offer Declined")){
        promptText = "Select Status"
    }

    /*
    TextArea for notes about company
     */
    val notes = new TextArea

    /*
    GridPane holding Add, Modify, and Delete Btns with ActionEvents
     */
    val addBtn = new Button("Add")
    addBtn.onAction = (e:ActionEvent) => {
        val comName = companyName.text.toString()
        val conName = contactName.text.toString()
        val phone = contactPhone.text.toString()
        val email = contactEmail.text.toString()
        val stat = status.selectionModel.apply.getSelectedItem
        val comType = getType()
        val note = notes.text.toString()
        if(comType.equals("tech")){
            techArray.append(Company(comName, conName, phone, email, stat, comType, note))
        } else {
            recArray.append(Company(comName, conName, phone, email, stat, comType, note))
        }
        refreshLV()
        clearData()
    }
    val modBtn = new Button("Modify")
    modBtn.onAction = (e:ActionEvent) => {
        val comName = companyName.text.toString()
        val conName = contactName.text.toString()
        val phone = contactPhone.text.toString()
        val email = contactEmail.text.toString()
        val stat = status.selectionModel.apply.getSelectedItem
        val comType = getType()
        val note = notes.text.toString()
        if(comType.equals("tech")){
            techArray(techIndex) = Company(comName, conName, phone, email, stat, comType, note)
        } else {
            recArray(recIndex) = Company(comName, conName, phone, email, stat, comType, note)
        }
        refreshLV()
    }
    val delBtn = new Button("Delete")
    delBtn.onAction = (e:ActionEvent) => {
        if(getType() == "tech"){
            techArray.remove(techIndex)
            clearSelections(techList)  //clear the selected index of deleted item
        } else {
            recArray.remove(recIndex)
            clearSelections(recList)  //clear the selected index of deleted item
        }
        refreshLV()
        clearData()
    }
    val btnGP = new GridPane{  //3 columns, 1 row
        hgap = 2
    }
    btnGP.add(addBtn,1,1)
    btnGP.add(modBtn,2,1)
    btnGP.add(delBtn,3,1)

    /*
    GUI layout
     */
    stage = new PrimaryStage
    stage.title = "JobHunter1.0.3"
    stage.scene = new Scene(800, 600){
        stylesheets = List(getClass.getResource("styles.css").toExternalForm)
        /*
        MenuBar holding a single Menu
        3 MenuItems plus a separator
        ActionEvents for each MenuItem
         */
        val menuBar = new MenuBar
        val fileMenu = new Menu("File")
        val openItem = new MenuItem("Open")
        openItem.onAction = (e:ActionEvent) => {
            val chooser = new FileChooser()
            val selectedFile = chooser.showOpenDialog(stage)
            if(selectedFile != null){
                val source = Source.fromFile(selectedFile)
                val lines = source.getLines()
                while(lines.hasNext){
                    val comName = lines.next()
                    val conName = lines.next()
                    val phone = lines.next()
                    val email = lines.next()
                    val status = lines.next()
                    val comType = lines.next()
                    var notes = ""
                    var line = lines.next()
                    while(line != "--end notes--"){  //"--end notes--" indicates end of notes in text file
                        notes += line + "\n"
                        line = lines.next()
                    }
                    comArray.append(Company(comName, conName, phone, email, status, comType, notes))
                }
                source.close()
                sortCompanies()
                refreshLV()
            }
        }
        val saveItem = new MenuItem("Save")
        saveItem.onAction = (e:ActionEvent) => {
            val chooser = new FileChooser()
            val selectedFile = chooser.showSaveDialog(stage)
            if(selectedFile != null){
                val pw = new java.io.PrintWriter(selectedFile)
                combinedCompanies()
                for(c <- comArray){
                    pw.println(c.companyName)
                    pw.println(c.contactName)
                    pw.println(c.contactPhone)
                    pw.println(c.contactEmail)
                    pw.println(c.status)
                    pw.println(c.companyType)
                    pw.println(c.notes)
                    pw.println("--end notes--")
                }
                pw.close()
            }
        }
        val exitItem = new MenuItem("Exit")
        exitItem.onAction = (e:ActionEvent) => sys.exit(0)
        fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)
        menuBar.menus = List(fileMenu)
        /*
        root BorderPane holding MenuBar top, SplitPane center
            SplitPane holding SplitPane left, BorderPane right
                SplitPane holding BorderPane top, BorderPane bottom
                    Top & Bottom BorderPane holding Label top, ScrollPane center holding ListView
                BorderPane holding GridPane top, BorderPane center
                    GridPane holding TextFields, RadioButtonFlowPane, ComboBox, BtnGridPane
                    BorderPane holding Label top, ScrollPane center holding TextArea
         */
        //top ScrollPane
        val topScrP = new ScrollPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        topScrP.content = techList
        //bottom ScrollPane
        val bottomScrP = new ScrollPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        bottomScrP.content = recList
        //top BorderPane
        val topBP = new BorderPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        topBP.top = new Label("Tech Companies")
        topBP.center = topScrP
        //bottom BorderPane
        val bottomBP = new BorderPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        bottomBP.top = new Label("Recruiters")
        bottomBP.center = bottomScrP
        //left SplitPane
        val leftSP = new SplitPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        leftSP.orientation = Orientation.Vertical  //sets items vertically
        leftSP.items ++= List(topBP, bottomBP)
        //GridPane
        val topGP = new GridPane{
            hgap = 10
            vgap = 2
        }  //4 columns, 3 rows
        topGP.columnConstraints.add(new ColumnConstraints(110))  //column 1
        topGP.columnConstraints.add(new ColumnConstraints(150))  //column 2
        topGP.add(new Label{
            text = "Company Name: "
            alignmentInParent = Pos.CenterLeft
            padding = Insets.apply(0,0,0,5)
        },0,0)
        topGP.add(companyName,1,0)  //text field
        topGP.add(new Label{
            text = "Contact Name: "
            alignmentInParent = Pos.CenterLeft
            padding = Insets.apply(0,0,0,5)
        },0,1)
        topGP.add(contactName,1,1)  //text field
        topGP.add(new Label{
            text = "Contact Phone: "
            alignmentInParent = Pos.CenterLeft
            padding = Insets.apply(0,0,0,5)
        },0,2)
        topGP.add(contactPhone,1,2)  //text field
        topGP.add(new Label{
            text = "Contact Email: "
            alignmentInParent = Pos.CenterLeft
            padding = Insets.apply(0,0,0,5)
        },0,3)
        topGP.add(contactEmail,1,3)  //text field
        topGP.add(flowPane,2,0)  //radio buttons
        topGP.add(status,2,1)  //combo box
        topGP.add(btnGP,2,3)  //button group
        //notes BorderPane
        val notesBP = new BorderPane
        notesBP.top = new Label{
            text = "Notes:"
            padding = Insets.apply(0,0,0,5)
        }
        notesBP.center = notes
        //right BorderPane
        val rightBP = new BorderPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        rightBP.top = topGP
        rightBP.center = notesBP
        //main SplitPane
        val mainSP = new SplitPane{
            style = "-fx-background-color: #2e2c2f;"
        }
        mainSP.dividerPositions = 0.2
        mainSP.items ++= List(leftSP, rightBP)
        //root BorderPane
        val rootPane = new BorderPane
        rootPane.top = menuBar
        rootPane.center = mainSP
        root = rootPane
    }
}