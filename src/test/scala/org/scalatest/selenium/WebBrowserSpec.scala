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
      radio.selection should be (None) // Radio button works in a group.
    }
    it("should, when multiple matching radio buttons exist, return the first one") {
      go to (host + "find-radio.html")
      val radio = radioButton("group2")
      radio.selection should be (None) // Radio button works in a group.
    }
  }

  describe("checkbox") {
    it("should throw TFE with valid stack depth if specified item not found") (pending)
    it("should throw TFE with valid stack depth if specified is found but is not a checkbox") (pending)
    it("should, when a valid text field is found, return a Checkbox instance") (pending)
    it("should, when multiple matching checkboxes exist, return the first one") (pending)
  }

  describe("singleSel") {
    it("should throw TFE with valid stack depth if specified item not found") (pending)
    it("should throw TFE with valid stack depth if specified is found but is not a single-selection list") (pending)
    it("should, when a valid text field is found, return a SingleSel instance") (pending)
    it("should, when multiple matching single-selection lists exist, return the first one") (pending)
  }

  describe("multiSel") {
    it("should throw TFE with valid stack depth if specified item not found") (pending)
    it("should throw TFE with valid stack depth if specified is found but is not a multiple-selection list") (pending)
    it("should, when a valid text field is found, return a MultiSel instance") (pending)
    it("should, when multiple matching multiple-selection lists exist, return the first one") (pending)
  }

  describe("click on") {
    it("should throw TFE with valid stack depth if specified item not found") (pending)
  }

  describe("switch to") {
    it("should throw TFE with valid stack depth if specified frame not found") (pending)
    it("should throw TFE with valid stack depth if specified window handle not found") (pending)
  }

  describe("goBack") {
    it("should throw TFE with valid stack depth if already at oldest page") (pending)
  }

  describe("goForward") {
    it("should throw TFE with valid stack depth if already at newest page") (pending)
  }

  describe("cookie") {
    it("should throw TFE with valid stack depth if specified cookie is not found") (pending)
  }

  describe("find") {
    it("should return None if specified item not found") (pending)
    it("should return a defined Option[Element] containing an instance of TextField if specified item is found to be a text field") (pending)
    it("should return a defined Option[Element] containing an instance of TextArea if specified item is found to be a text area") (pending)
    it("should return a defined Option[Element] containing an instance of RadioButton if specified item is found to be a radio button") (pending)
    it("should return a defined Option[Element] containing an instance of Checkbox if specified item is found to be a checkbox") (pending)
    it("should return a defined Option[Element] containing an instance of SingleSel if specified item is found to be a single-selection list") (pending)
    it("should return a defined Option[Element] containing an instance of MultiSel if specified item is found to be a multiple-selection list") (pending)
    it("should return a defined Option[Element] containing an instance of Element if specified item is found but is not one of the items for which we have defined an Element subclass") (pending)
  }

  describe("findAll") {
    it("should return an empty IndexedSeq if specified item not found") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of TextField if specified item is found to be a text field") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of TextArea if specified item is found to be a text area") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of RadioButton if specified item is found to be a radio button") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of Checkbox if specified item is found to be a checkbox") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of SingleSel if specified item is found to be a single-selection list") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of MultiSel if specified item is found to be a multiple-selection list") (pending)
    it("should return a defined IndexedSeq[Element] containing an instance of Element if specified item is found but is not one of the items for which we have defined an Element subclass") (pending)
  }

  describe("executeScript") {
    it("should execute the passed JavaScript") (pending)
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
    
    it("should get and set radio button correctly.") {
      go to (host + "radio.html")
      title should be ("Radio Button")
      
      radioButton("group1").selection should be (None)
      intercept[TestFailedException] {
        radioButton("group1").value
      }
      
      radioButton("group1").value = "Option 1"
      radioButton("group1").value should be ("Option 1")
      
      radioButton("group1").value = "Option 2"
      radioButton("group1").value should be ("Option 2")
      
      radioButton("group1").value = "Option 3"
      radioButton("group1").value should be ("Option 3")
      
      intercept[org.openqa.selenium.NoSuchElementException] {
        radioButton("group1").value = "Invalid value"
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
      
      selectList("select1").value should be ("option1")
      selectList("select1").value = "option2"
      selectList("select1").value should be ("option2")
      selectList("select1").value = "option3"
      selectList("select1").value should be ("option3")
      selectList("select1").value = "option1"
      selectList("select1").value should be ("option1")
      intercept[org.openqa.selenium.NoSuchElementException] {
        selectList("select1").value = "other"
      }
      selectList("select1").values should have size 1
      selectList("select1").values(0) should be ("option1")
      selectList("select1").clear("option2")
      selectList("select1").value should be ("option1")
      selectList("select1").clear("option1")
      selectList("select1").values should have size 1  // single-select cannot be de-selected
      intercept[UnsupportedOperationException] {
        selectList("select1").clearAll() // single-select cannot be de-selecte all
      }
      selectList("select1").values should have size 1
      selectList("select1").selections should be (Some(IndexedSeq("option1")))
      selectList("select1").selections.get should have size 1
      
      // No options selected
      selectList("select2").selections should be (None)
      selectList("select2").values should have size 0
      selectList("select2").values += "option4"
      selectList("select2").value should be ("option4")
      selectList("select2").values += "option5"
      selectList("select2").value should be ("option4")
      selectList("select2").values should have size 2
      selectList("select2").values(0) should be ("option4")
      selectList("select2").values(1) should be ("option5")
      selectList("select2").values += "option6"
      selectList("select2").value should be ("option4")
      selectList("select2").values should have size 3
      selectList("select2").values(0) should be ("option4")
      selectList("select2").values(1) should be ("option5")
      selectList("select2").values(2) should be ("option6")
      selectList("select2").selections should be (Some(IndexedSeq("option4", "option5", "option6")))
      intercept[org.openqa.selenium.NoSuchElementException] {
        selectList("select2").values += "other"
      }
      selectList("select2").values -= "option5"
      selectList("select2").values should have size 2
      selectList("select2").values(0) should be ("option4")
      selectList("select2").values(1) should be ("option6")
      selectList("select2").clearAll()
      selectList("select2").selections should be (None)
      selectList("select2").values should have size 0
      
      // Test the alternative way to clear
      selectList("select2").values += "option6"
      selectList("select2").values should have size 1
      selectList("select2").values(0) should be ("option6")
      selectList("select2") clear "option6"
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
      cookie("name2") should be (null)
      cookie("name1").value should be ("value1")
      cookie("name3").value should be ("value3")
      
      delete all cookies
      cookie("name1") should be (null)
      cookie("name3") should be (null)
    }
    
    it("should support implicitlyWait method") {
      implicitlyWait(Span(10, Seconds))
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
