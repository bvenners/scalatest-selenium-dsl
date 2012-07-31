/*
 * Copyright 2001-2012 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest.selenium

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import java.util.concurrent.TimeUnit
import org.scalatest.time.SpanSugar
import org.scalatest.ParallelTestExecution
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.time.Span
import org.scalatest.time.Seconds
import org.scalatest.exceptions.TestFailedException

class WebBrowserSpec extends JettySpec with ShouldMatchers with SpanSugar with WebBrowser with HtmlUnit {

  describe("textField") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "find-textfield.html")
      val caught = intercept[TestFailedException] {
        textField("unknown")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified is found but is not a text field") {
      go to (host + "find-textfield.html")
      val caught = intercept[TestFailedException] {
        textField("area1")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should, when a valid text field is found, return a TestField instance") {
      go to (host + "find-textfield.html")
      val text1 = textField("text1")
      text1.value should be ("value1")
    }
    it("should, when multiple matching text fields exist, return the first one") {
      go to (host + "find-textfield.html")
      val text2 = textField("text2")
      text2.value should be ("value2")
    }
  }

  describe("textArea") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "find-textarea.html")
      val caught = intercept[TestFailedException] {
        textArea("unknown")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified is found but is not a text area") {
      go to (host + "find-textarea.html")
      val caught = intercept[TestFailedException] {
        textArea("opt1")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should, when a valid text area is found, return a TestArea instance") {
      go to (host + "find-textarea.html")
      val textarea1 = textArea("textarea1")
      textarea1.text should be ("value1")
    }
    it("should, when multiple matching text areas exist, return the first one") {
      go to (host + "find-textarea.html")
      val text2 = textArea("textarea2")
      text2.text should be ("value2")
    }
  }

  describe("radioButton") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "find-radio.html")
      val caught = intercept[TestFailedException] {
        radioButton("unknown")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified is found but is not a radio button") {
      go to (host + "find-radio.html")
      val caught = intercept[TestFailedException] {
        radioButton("text1")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should, when a valid radio button is found, return a RadioButton instance") {
      go to (host + "find-radio.html")
      val radio = radioButton("group1")
      radio.value should be ("value1")
    }
    it("should, when multiple matching radio buttons exist, return the first one") {
      go to (host + "find-radio.html")
      val radio = radioButton("group2")
      radio.value should be ("value2")
    }
  }

  describe("checkbox") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "find-checkbox.html")
      val caught = intercept[TestFailedException] {
        checkbox("unknown")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified is found but is not a checkbox") {
      go to (host + "find-checkbox.html")
      val caught = intercept[TestFailedException] {
        checkbox("text1")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should, when a valid text field is found, return a Checkbox instance") {
      go to (host + "find-checkbox.html")
      val checkbox1 = checkbox("opt1")
      checkbox1.isSelected should be (true)
    }
    it("should, when multiple matching checkboxes exist, return the first one") {
      go to (host + "find-checkbox.html")
      val checkbox2 = checkbox("opt2")
      checkbox2.isSelected should be (false)
    }
  }

  describe("singleSel") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "find-select.html")
      val caught = intercept[TestFailedException] {
        singleSel("unknown")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified is found but is not a single-selection list") {
      go to (host + "find-select.html")
      val caught = intercept[TestFailedException] {
        singleSel("select2")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should, when a valid single-selection list is found, return a SingleSel instance") {
      go to (host + "find-select.html")
      val select1 = singleSel("select1")
      select1.value should be ("option2")
    }
    it("should, when multiple matching single-selection lists exist, return the first one") {
      go to (host + "find-select.html")
      val select3 = singleSel("select3")
      select3.value should be ("option3")
    }
    it("should throw TFE with valid stack depth if invalid option is set") {
      go to (host + "find-select.html")
      val select1 = singleSel("select1")
      val caught = intercept[TestFailedException] {
        select1.value = "something else"
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
  }

  describe("multiSel") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "find-select.html")
      val caught = intercept[TestFailedException] {
        multiSel("unknown")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified is found but is not a multiple-selection list") {
      go to (host + "find-select.html")
      val caught = intercept[TestFailedException] {
        multiSel("select1")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should, when a valid text field is found, return a MultiSel instance") {
      go to (host + "find-select.html")
      val select2 = multiSel("select2")
      select2.values should be (IndexedSeq("option4", "option5"))
    }
    it("should, when multiple matching multiple-selection lists exist, return the first one") {
      go to (host + "find-select.html")
      val select4 = multiSel("select4")
      select4.values should be (IndexedSeq("option1", "option3"))
    }
    it("should throw TFE with valid stack depth if invalid option is set") {
      go to (host + "find-select.html")
      val select2 = multiSel("select2")
      val caught = intercept[TestFailedException] {
        select2.values = Seq("option6", "something else")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
  }

  describe("click on") {
    it("should throw TFE with valid stack depth if specified item not found") {
      go to (host + "index.html")
      val caught = intercept[TestFailedException] {
        click on "unknown"
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
  }

  describe("switch to") {
    it("should switch frame correctly and throw TFE with valid stack depth if specified frame not found") {
      go to (host + "frame.html")
      val win = windowHandle
      switch to frame("frame1")
      switch to window(win)
      switch to frame("frame2")
      
      switch to window(win)
      switch to frame(0)
      switch to window(win)
      switch to frame(1)
      
      switch to window(win)
      switch to frame(id("frame1"))
      switch to window(win)
      switch to frame(id("frame2"))
      
      switch to window(win)
      val caught1= intercept[TestFailedException] {
        switch to frame("frame3")
      }
      caught1.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught1.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
      
      val caught2 = intercept[TestFailedException] {
        switch to frame(2)
      }
      caught2.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught2.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
      
      val caught3 = intercept[TestFailedException] {
        switch to frame(id("text1"))
      }
      caught3.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught3.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
    it("should throw TFE with valid stack depth if specified window handle not found") {
      go to (host + "window.html")
      val handle = windowHandle
      switch to window(handle) // should be ok
      val caught = intercept[TestFailedException] {
        switch to window("Something else")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
  }

  describe("goBack") {
    it("should have no effect if already at oldest page") {
      for (i <- 0 to 1000)
        goBack()
    }
  }

  describe("goForward") {
    it("should have no effect if already at newest page") {
      for (i <- 0 to 1000)
        goForward()
    }
  }

  describe("cookie") {
    it("should throw TFE with valid stack depth if specified cookie is not found") {
      go to (host + "index.html")
      val caught = intercept[TestFailedException] {
        cookie("other")
      }
      caught.failedCodeLineNumber should be (Some(thisLineNumber - 2))
      caught.failedCodeFileName should be (Some("WebBrowserSpec.scala"))
    }
  }

  describe("find") {
    it("should return None if specified item not found") {
      go to (host + "index.html")
      find("something") should be (None)
    }
    it("should return a defined Option[Element] containing an instance of TextField if specified item is found to be a text field") {
      go to (host + "find-textfield.html")
      find("text1") match {
        case Some(textField: TextField) =>
          textField.value should be ("value1")
        case other =>
          fail("Expected Some(textField: TextField), but got: " + other)
      }
    }
    it("should return a defined Option[Element] containing an instance of TextArea if specified item is found to be a text area") {
      go to (host + "find-textarea.html")
      find("textarea1") match {
        case Some(textArea: TextArea) =>
          textArea.text should be ("value1")
        case other => 
          fail("Expected Some(textArea: TextArea), but got: " + other)
      }
    }
    it("should return a defined Option[Element] containing an instance of RadioButton if specified item is found to be a radio button") {
      go to (host + "find-radio.html")
      find("group2") match {
        case Some(radio: RadioButton) =>
          radio.value should be ("value2")
        case other => 
          fail("Expected Some(radio: RadioButton), but got: " + other)
      }
    }
    it("should return a defined Option[Element] containing an instance of Checkbox if specified item is found to be a checkbox") {
      go to (host + "find-checkbox.html")
      find("opt1") match {
        case Some(checkbox: Checkbox) => 
          checkbox.isSelected should be (true)
        case other => 
          fail("Expected Some(checkbox: Checkbox), but got: " + other)
      }
    }
    it("should return a defined Option[Element] containing an instance of SingleSel if specified item is found to be a single-selection list") {
      go to (host + "find-select.html")
      find("select1") match {
        case Some(singleSel: SingleSel) => 
          singleSel.value should be ("option2")
        case other => 
          fail("Expected Some(singleSel: SingleSel), but got: " + other)
      }
    }
    it("should return a defined Option[Element] containing an instance of MultiSel if specified item is found to be a multiple-selection list") {
      go to (host + "find-select.html")
      find("select2") match {
        case Some(multiSel: MultiSel) => 
          multiSel.values should be (IndexedSeq("option4", "option5"))
        case other =>
          fail("Expected Some(multiSel: MultiSel), but got: " + other)
      }
    }
    it("should return a defined Option[Element] containing an instance of Element if specified item is found but is not one of the items for which we have defined an Element subclass") {
      go to (host + "image.html")
      find("anImage") match {
        case Some(element: Element) => 
          element.tagName should be ("img")
        case other => 
          fail("Expected Some(element: Element), but got: " + other)
      }
    }
  }

  describe("findAll") {
    it("should return an empty IndexedSeq if specified item not found") {
      go to (host + "index.html")
      findAll("something") should be (IndexedSeq.empty)
    }
    it("should return a defined IndexedSeq[Element] containing an instance of TextField if specified item is found to be a text field") {
      go to (host + "find-textfield.html")
      findAll("text1") match {
        case IndexedSeq(textField: TextField) => 
          textField.value should be ("value1")
        case other => 
          fail("Expected IndexedSeq(element: TextField), but got: " + other)
      }
    }
    it("should return a defined IndexedSeq[Element] containing an instance of TextArea if specified item is found to be a text area") {
      go to (host + "find-textarea.html")
      findAll("textarea1") match {
        case IndexedSeq(textArea: TextArea) =>
          textArea.text should be ("value1")
        case other =>
          fail("Expected IndexedSeq(textArea: TextArea), but got: " + other)
      }
    }
    it("should return a defined IndexedSeq[Element] containing an instance of RadioButton if specified item is found to be a radio button") {
      go to (host + "find-radio.html")
      findAll("group1") match {
        case IndexedSeq(radio: RadioButton) =>
          radio.value should be ("value1")
        case other =>
          fail("Expected IndexedSeq(radio: RadioButton), but got: " + other)
      }
    }
    it("should return a defined IndexedSeq[Element] containing an instance of Checkbox if specified item is found to be a checkbox") {
      go to (host + "find-checkbox.html")
      findAll("opt1") match {
        case IndexedSeq(checkbox: Checkbox) =>
          checkbox.value should be ("Option 1")
        case other =>
          fail("Expected IndexedSeq(checkbox: Checkbox), but got: " + other)
      }
    }
    it("should return a defined IndexedSeq[Element] containing an instance of SingleSel if specified item is found to be a single-selection list") {
      go to (host + "find-select.html")
      findAll("select1") match {
        case IndexedSeq(singleSel: SingleSel) => 
          singleSel.value should be ("option2")
        case other =>
          fail("Expected IndexedSeq(singleSel: SingleSel), but got: " + other)
      }
    }
    it("should return a defined IndexedSeq[Element] containing an instance of MultiSel if specified item is found to be a multiple-selection list") {
      go to (host + "find-select.html")
      findAll("select2") match {
        case IndexedSeq(multiSel: MultiSel) => 
          multiSel.values should be (IndexedSeq("option4", "option5"))
        case other =>
          fail("Expected IndexedSeq(multiSel: MultiSel), but got: " + other)
      }
    }
    it("should return a defined IndexedSeq[Element] containing an instance of Element if specified item is found but is not one of the items for which we have defined an Element subclass") {
      go to (host + "image.html")
      findAll("anImage") match {
        case IndexedSeq(image: Element) =>
          image.tagName should be ("img")
        case other =>
          fail("Expected IndexedSeq(image: Element), but got: " + other)  
      }
    }
  }

  describe("executeScript") {
    it("should execute the passed JavaScript") {
      go to (host + "index.html")
      val result1 = executeScript("return document.title;")
      result1 should be ("Test Title")
      val result2 = executeScript("return 'Hello ' + arguments[0]", "ScalaTest")
      result2 should be ("Hello ScalaTest")
    }
  }
  
  describe("executeAsyncScript") {
    it("should execute the passed JavaScript with asynchronous call") {
      go to (host + "index.html")
      val script = """
        var callback = arguments[arguments.length - 1];
        window.setTimeout(function() {callback('Hello ScalaTest')}, 500);
        """
      setScriptTimeout(1 second)
      val result = executeAsyncScript(script)
      result should be ("Hello ScalaTest")
    }
  }

  describe("Web Browser") {

    it("should go to web page by using url and get its title correctly.") {
      go to (host + "index.html")
      title should be ("Test Title")
    }
    
    it("should go to web page by using Page and get its title correctly.") {
      class IndexPage extends Page {
        val url = host + "radio.html"
      }
      val indexPage = new IndexPage
      go to indexPage
      title should be ("Radio Button")
    }

    it("should get and set text field value correctly.") {
      go to (host + "textfield.html")
      title should be ("Text Field")

      textField("text1").value should be ("")                   
                                                                // textField("text1") should have ('value(""), 'attribute(""))
      textField("text1").attribute("value") should be ("")           // textField("text1").attribute("value") should be ("")    // ok as is
      textField("text1").value = "value 1"                          // set textField "text1" to "value 1"
      textField("text1").value should be ("value 1")              // textField("text1").text should be ("value 1")
      textField("text1").attribute("value") should be ("value 1")    // textField("text1").attribute("value") should be ("value 1")

      textField("text2").value should be ("")
      textField("text2").attribute("value") should be ("")
      textField("text2").value = "value 2"
      textField("text2").value should be ("value 2")
      textField("text2").attribute("value") should be ("value 2")
    }
    
    it("should get and set text area value correctly.") {
      go to (host + "textarea.html")
      title should be ("Text Area")
      
      textArea("area1").value should be ("")
      textArea("area1").attribute("value") should be ("")
      textArea("area1").value = "area 1 - line 1\narea 1 - line 2"
      textArea("area1").value should be ("area 1 - line 1\narea 1 - line 2")
      textArea("area1").attribute("value") should be ("area 1 - line 1\narea 1 - line 2")
      
      textArea("area2").value should be ("")
      textArea("area2").attribute("value") should be ("")
      textArea("area2").value = "area 2 - line 1\narea 2 - line 2"
      textArea("area2").value should be ("area 2 - line 1\narea 2 - line 2")
      textArea("area2").attribute("value") should be ("area 2 - line 1\narea 2 - line 2")
    }
    
    it("should get and set radio button group correctly.") {
      go to (host + "radio.html")
      title should be ("Radio Button")
      
      radioButtonGroup("group1").selection should be (None)
      intercept[TestFailedException] {
        radioButtonGroup("group1").value
      }
      
      radioButtonGroup("group1").value = "Option 1"
      radioButtonGroup("group1").value should be ("Option 1")
      
      radioButtonGroup("group1").value = "Option 2"
      radioButtonGroup("group1").value should be ("Option 2")
      
      radioButtonGroup("group1").value = "Option 3"
      radioButtonGroup("group1").value should be ("Option 3")
      
      intercept[org.openqa.selenium.NoSuchElementException] {
        radioButtonGroup("group1").value = "Invalid value"
      }
    }
    
    it("should read, select and clear check box correctly.") {
      go to (host + "checkbox.html")
      title should be ("Check Box")
      
      checkbox("opt1").isSelected should be (false)
      checkbox("opt1").select()
      checkbox("opt1").isSelected should be (true)
      checkbox("opt1").clear()
      checkbox("opt1").isSelected should be (false)
      
      checkbox("opt2").isSelected should be (false)
      checkbox("opt2").select()
      checkbox("opt2").isSelected should be (true)
      checkbox("opt2").clear()
      checkbox("opt2").isSelected should be (false)
    }
    
    it("should read, select and clear dropdown list (select) correctly.") {
      go to (host + "select.html")
      title should be ("Select")
      
      singleSel("select1").value should be ("option1")
      singleSel("select1").value = "option2"
      singleSel("select1").value should be ("option2")
      singleSel("select1").value = "option3"
      singleSel("select1").value should be ("option3")
      singleSel("select1").value = "option1"
      singleSel("select1").value should be ("option1")
      intercept[TestFailedException] {
        singleSel("select1").value = "other"
      }
      
      // No options selected
      multiSel("select2").selections should be (None)
      multiSel("select2").values should have size 0
      multiSel("select2").values += "option4"
      multiSel("select2").values should be (IndexedSeq("option4"))
      multiSel("select2").values += "option5"
      multiSel("select2").values should be (IndexedSeq("option4", "option5"))
      multiSel("select2").values should have size 2
      multiSel("select2").values(0) should be ("option4")
      multiSel("select2").values(1) should be ("option5")
      multiSel("select2").values += "option6"
      multiSel("select2").values should be (IndexedSeq("option4", "option5", "option6"))
      multiSel("select2").values should have size 3
      multiSel("select2").values(0) should be ("option4")
      multiSel("select2").values(1) should be ("option5")
      multiSel("select2").values(2) should be ("option6")
      multiSel("select2").selections should be (Some(IndexedSeq("option4", "option5", "option6")))
      intercept[TestFailedException] {
        multiSel("select2").values += "other"
      }
      multiSel("select2").values -= "option5"
      multiSel("select2").values should have size 2
      multiSel("select2").values(0) should be ("option4")
      multiSel("select2").values(1) should be ("option6")
      multiSel("select2").clearAll()
      multiSel("select2").selections should be (None)
      multiSel("select2").values should have size 0
      
      // Test the alternative way to clear
      multiSel("select2").values += "option6"
      multiSel("select2").values should have size 1
      multiSel("select2").values(0) should be ("option6")
      multiSel("select2") clear "option6"
      multiSel("select2").selections should be (None)
      multiSel("select2").values should have size 0
    }
    
    it("should submit form when submit is called on form's element.") {
      go to (host + "submit.html")
      title should be ("Submit")
      
      click on "name" // This set the focus
      textField("name").value = "Penguin"
      submit()
      // submit (name("name")) // This will work as well.
    }
    
    it("should submit form when submit button is clicked.") {
      go to (host + "submit.html")
      title should be ("Submit")
      
      textField("name").value = "Penguin"
      click on "submitButton"
    }
    
    it("should navigate to, back, forward and refresh correctly") {
      go to (host + "navigate1.html")
      title should be ("Navigation 1")
      go to (host + "navigate2.html")
      title should be ("Navigation 2")
      goBack()
      // click back button
      title should be ("Navigation 1")
      goForward()
      // click forward button
      title should be ("Navigation 2")
      reloadPage()
      // click refreshPage button
      // click submit button
      title should be ("Navigation 2")
    }
    
    it("should support goBack, goForward and reloadPage correctly") {
      go to (host + "navigate1.html")
      title should be ("Navigation 1")
      go to (host + "navigate2.html")
      title should be ("Navigation 2")
      goBack()
      title should be ("Navigation 1")
      goForward()
      title should be ("Navigation 2")
      reloadPage()
      title should be ("Navigation 2")
    }
    
    it("should create, read and delete cookie correctly") {
      go to (host + "index.html")
      
      add cookie("name1", "value1")
      cookie("name1").value should be ("value1")
      
      add cookie("name2", "value2")
      cookie("name2").value should be ("value2")
      
      add cookie("name3", "value3")
      cookie("name3").value should be ("value3")
      
      delete cookie "name2"
      intercept[TestFailedException] {
        cookie("name2") should be (null)
      }
      cookie("name1").value should be ("value1")
      cookie("name3").value should be ("value3")
      
      delete all cookies
      intercept[TestFailedException] {
        cookie("name1") should be (null)
      }
      intercept[TestFailedException] {
        cookie("name3") should be (null)
      }
    }
    
    it("should support implicitlyWait method") {
      implicitlyWait(Span(2, Seconds))
    }
  
    it("should support capturing screenshot") {
      go to "http://www.artima.com"
      try {
        capture
        capture to ("MyScreenShot.png")
      }
      catch {
        case unsupported: UnsupportedOperationException => 
          pending
      }
    }
    
    ignore("should support wait method") {
      // This example is taken from http://seleniumhq.org/docs/03_webdriver.html
      
      // Visit Google
      go to "http://www.google.com"
      // Alternatively the same thing can be done like this
      // navigate to "http://www.google.com"

      // Click on the text input element by its name 
      click on "q"
      // and enter "Cheese!"
      textField("q").value = "Cheese!"

      // Now submit the form
      submit()
        
      // Google's search is rendered dynamically with JavaScript.
      // Wait for the page to load, timeout after 10 seconds
      wait[Boolean](Span(10, Seconds)) {
        title.toLowerCase.startsWith("cheese!")
      }

      // Should see: "cheese! - Google Search"
      title should be ("Cheese! - Google Search")
    }
    
    ignore("should be able to use ScalaTest's eventually in place of Selenium's wait") {
      import org.scalatest.concurrent.Eventually._
      
      go to "http://www.google.com"
      click on "q"
      textField("q").value = "Cheese!"
      submit()
      
      eventually(timeout(10 seconds)) {
        title.toLowerCase.startsWith("cheese!") should be (true)
      }
      
      title should be ("Cheese! - Google Search")
    }
        
    // Some operation not supported in HtmlUnit driver, e.g. switch to alert.
    // Should be good enough to test the following dsl compiles.
    ignore("should support switch") {
      switch to activeElement
      switch to alert
      switch to defaultContent
      switch to frame(0)
      switch to frame("name")
      switch to window(windowHandle)
    }
  }
  
  def thisLineNumber = {
    val st = Thread.currentThread.getStackTrace

    if (!st(2).getMethodName.contains("thisLineNumber"))
      st(2).getLineNumber
    else
      st(3).getLineNumber
  }
}

class ParallelWebBrowserSpec extends WebBrowserSpec with ParallelTestExecution
