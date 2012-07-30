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

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.util.concurrent.TimeUnit
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.Clock
import org.openqa.selenium.support.ui.Sleeper
import org.openqa.selenium.support.ui.ExpectedCondition
import scala.collection.mutable.Buffer
import scala.collection.JavaConversions._
import org.openqa.selenium.Cookie
import java.util.Date
import org.scalatest.time.Span
import org.scalatest.time.Milliseconds
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.OutputType
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import org.openqa.selenium.Alert
import org.openqa.selenium.support.ui.Select
//import org.scalatest.Assertions.fail
import org.scalatest.exceptions.TestFailedException
import org.scalatest.exceptions.StackDepthException

/**
 * Trait that provides a domain specific language (DSL) for writing browser-based tests using <a href="http://seleniumhq.org">Selenium</a>.  
 *
 * To use ScalaTest's Selenium DSL, mix trait <code>WebBrowser</code> into your test class. This trait provides the DSL in its
 * entirety except for one missing piece: an implicit <code>org.openqa.selenium.WebDriver</code>. One way to provide the missing
 * implicit driver is to declare one as a member of your test class, like this:
 * 
 * <pre class="stHighlight">
 * class BlogSpec extends FlatSpec with ShouldMatchers with WebBrowser {
 *
 *   implicit val webDriver: WebDriver = new HtmlUnitDriver
 *
 *   "The blog app home page" should "have the correct title" in {
 *     go to (host + "index.html")
 *     title should be ("Awesome Blog")
 *   }
 * }
 * </pre>
 * 
 * <p>
 * For convenience, however, ScalaTest provides a <code>WebBrowser</code> subtrait containing an implicit <code>WebDriver</code> for each
 * driver provided by Selenium. 
 * Thus a simpler way to use the <code>HtmlUnit</code> driver, for example, is to extend
 * ScalaTest's <a href="HtmlUnit.html"><code>HtmlUnit</code></a> trait, like this:
 * </p>
 * 
 * <pre class="stHighlight">
 * class BlogSpec extends FlatSpec with ShouldMatchers with HtmlUnit {
 *
 *   "The blog app home page" should "have the correct title" in {
 *     go to (host + "index.html")
 *     title should be ("Awesome Blog")
 *   }
 * }
 * </pre>
 * 
 * <p>
 * The web driver traits provided by ScalaTest are:
 * </p>
 * 
 * <table style="border-collapse: collapse; border: 1px solid black">
 * <tr><th style="background-color: #CCCCCC; border-width: 1px; padding: 3px; text-align: center; border: 1px solid black"><strong>Driver</strong></th><th style="background-color: #CCCCCC; border-width: 1px; padding: 3px; text-align: center; border: 1px solid black"><strong><code>WebBrowser</code> subtrait</strong></th></tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * Google Chrome
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <a href="Chrome.html"><code>Chrome</code></a>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * Mozilla Firefox
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <a href="Firefox.html"><code>Firefox</code></a>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * HtmlUnit
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <a href="HtmlUnit.html"><code>HtmlUnit</code></a>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * Microsoft Internet Explorer
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <a href="InternetExplorer.html"><code>InternetExplorer</code></a>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * Apple Safari
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <a href="Safari.html"><code>Safari</code></a>
 * </td>
 * </tr>
 * </table>
 *
 * <h2>Navigation</h2>
 *
 * <p>
 * You can ask the browser to retrieve a page (go to a URL) like this:
 * </p>
 * 
 * <pre class="stHighlight">
 * go to "http://www.artima.com"
 * </pre>
 * 
 * <p>
 * Note: If you are using the <em>page object pattern</em>, you can also go to a page using the <code>Page</code> instance, as
 * illustrated in the section on <a href="pageObjects">page objects</a> below.
 * </p>
 *
 * <p>
 * Once you have retrieved a page, you can fill in and submit forms, query for the values of page elements, and make assertions.  
 * In the following example, selenium will go to <code>http://www.google.com</code>, fill in the text box with
 * <code>Cheese!</code>, press the submit button, and wait for result returned from an AJAX call:
 * </p>
 * 
 * <pre class="stHighlight">
 * go to "http://www.google.com"
 * click on "q"
 * textField("q").value = "Cheese!"
 * submit()
 * // Google's search is rendered dynamically with JavaScript.
 * eventually { title should be ("Cheese! - Google Search") }
 * </pre>
 * 
 * <p>
 * In the above example, the <code>"q"</code> used in &ldquo;<code>click on "q"</code>&rdquo; and  &ldquo;<code>textField("q")</code>&rdquo;
 * can be either the id or name of an element. ScalaTest's Selenium DSL will try to lookup by id first. If it cannot find 
 * any element with an id equal to <code>&quot;q&quot;</code>, it will then try lookup by name <code>&quot;q&quot;</code>.
 * </p>
 * 
 * <p>
 * Alternatively, you can be more specific:
 * </p>
 * 
 * <pre class="stHighlight">
 * click on id("q")   // to lookup by id "q" 
 * click on name("q") // to lookup by name "q" 
 * </pre>
 * 
 * <p>
 * In addition to <code>id</code> and <code>name</code>, you can use the following approaches to lookup elements, just as you can do with
 * Selenium's <code>org.openqa.selenium.By</code> class:
 * </p>
 * 
 * <ul>
 *   <li><code>xpath</code></li>
 *   <li><code>className</code></li>
 *   <li><code>cssSelector</code></li>
 *   <li><code>linkText</code></li>
 *   <li><code>partialLinkText</code></li>
 *   <li><code>tagName</code></li>
 * </ul>
 * 
 * <p>
 * For example, you can select by link text with:
 * </p>
 *
 * <pre class="stHighlight">
 * click on linkText("click here!")
 * </pre>
 * 
 * <p>
 * If an element is not found via any form of lookup, evaluation will complete abruptly with a <code>TestFailedException</code>.
 * <p>
 *
 * <h2>Getting and setting input element values</h2>
 * 
 * <p>
 * ScalaTest's Selenium DSL provides a clear, simple syntax for accessing and updating the values of input elements such as
 * text fields, radio buttons, checkboxes, and selection lists. If a requested element is not found, or if it is found but is
 * not of the requested type, an exception will immediately result causing the test to fail.
 * <p>
 *
 * <h3>Text fields and text areas</h3>
 * 
 * <p>
 * You can change a text field's value by assigning it via the <code>=</code> operator, like this:
 * </p>
 * 
 * <pre class="stHighlight">
 * textField("q").value = "Cheese!"
 * </pre>
 * 
 * <p>
 * And you can access a text field's value by simply invoking <code>value</code> on it:
 * </p>
 * 
 * <pre class="stHighlight">
 * textField("q").value should be ("Cheese!")
 * </pre>
 * 
 * <p>
 * If the text field is empty, <code>value</code> will return an empty string (<code>""</code>).
 * </p>
 * 
 * <p>
 * You can use the same syntax with text areas by replacing <code>textField</code> with <code>textArea</code>, as in:
 * </p>
 * 
 * <pre class="stHighlight">
 * textArea("body").value = "I saw something cool today!"
 * textArea("body").value should be ("I saw something cool today!")
 * </pre>
 * 
 * <h3>Radio buttons</h3>
 * 
 * <p>
 * Radio buttons work together in groups. For example, you could have a group of radio buttons, like this:
 * </p>
 * 
 * <pre>
 * &lt;input type="radio" name="group1" value="Option 1"&gt; Option 1&lt;input&gt;
 * &lt;input type="radio" name="group1" value="Option 2"&gt; Option 2&lt;input&gt;
 * &lt;input type="radio" name="group1" value="Option 3"&gt; Option 3&lt;input&gt;
 * </pre>
 * 
 * <p>
 * You can select an option in either of two ways:
 * </p>
 *
 * <pre class="stHighlight">
 * radioButton("group1").value = "Option 2"
 * radioButton("group1").selection = Some("Option 2")
 * </pre>
 *
 * <p>
 * Likewise, you can read the currently selected value of a group of radio buttons in two ways:
 * </p>
 *
 * <pre class="stHighlight">
 * radioButton("group1").value should be ("Option 2")
 * radioButton("group1").selection should be (Some("Option 2"))
 * </pre>
 * 
 * <p>
 * If the radio button has no selection at all, <code>selection</code> will return <code>None</code> whereas <code>value</code>
 * will throw a <code>TestFailedException</code>. By using <code>value</code>, you are indicating you expect a selection, and if there
 * isn't a selection that should result in a failed test.
 * </p>
 * 
 * <h3>Checkboxes</h3>
 * 
 * <p>
 * A checkbox in one of two states: selected or cleared. Here's how you select a checkbox:
 * </p>
 * 
 * <pre class="stHighlight">
 * checkbox("cbx1").select()
 * </pre>
 * 
 * <p>
 * And here's how you'd clear one:
 * </p>
 * 
 * <pre class="stHighlight">
 * checkbox("cbx1").clear()
 * </pre>
 * 
 * <p>
 * You can access the current state of a checkbox with <code>isSelected</code>:
 * </p>
 * 
 * <pre class="stHighlight">
 * checkbox("cbx1").isSelected should be (true)
 * </pre>
 * 
 * <h3>Single-selection dropdown lists</h3>
 * 
 * <p>
 * Given the following single-selection dropdown list:
 * </p>
 * 
 * <pre>
 * &lt;select id="select1"&gt;
 *  &lt;option value="option1"&gt;Option 1&lt;/option&gt;
 *  &lt;option value="option2"&gt;Option 2&lt;/option&gt;
 *  &lt;option value="option3"&gt;Option 3&lt;/option&gt;
 * &lt;/select&gt;
 * </pre>
 * 
 * <p>
 * You could select <code>Option 2</code> in either of two ways:
 * </p>
 * 
 * <pre class="stHighlight">
 * singleSel("select1").value = "option2"
 * singleSel("select1").selection = Some("option2")
 * </pre>
 * 
 * <p>
 * To clear the selection, either invoke <code>clear</code> or set <code>selection</code> to <code>None</code>:
 * </p>
 *
 * <pre class="stHighlight">
 * singleSel.clear()
 * singleSel("select1").selection = None
 * </pre>
 * 
 * <p>
 * You can read the currently selected value of a single-selection list in the same manner as radio buttons:
 * </p>
 *
 * <pre class="stHighlight">
 * singleSel("select1").value should be ("option2")
 * singleSel("select1").selection should be (Some("option2"))
 * </pre>
 * 
 * <p>
 * If the single-selection list has no selection at all, <code>selection</code> will return <code>None</code> whereas <code>value</code>
 * will throw a <code>TestFailedException</code>. By using <code>value</code>, you are indicating you expect a selection, and if there
 * isn't a selection that should result in a failed test.
 * </p>
 * 
 * <h3>Multiple-selection lists</h3>
 * 
 * <p>
 * Given the following multiple-selection list:
 * </p>
 * 
 * <pre>
 * &lt;select name="select2" multiple="multiple"&gt;
 *  &lt;option value="option4"&gt;Option 4&lt;/option&gt;
 *  &lt;option value="option5"&gt;Option 5&lt;/option&gt;
 *  &lt;option value="option6"&gt;Option 6&lt;/option&gt;
 * &lt;/select&gt;
 * </pre>
 * 
 * <p>
 * You could select <code>Option 5</code> and <code>Option 6</code> like this:
 * </p>
 * 
 * <pre class="stHighlight">
 * multiSel("select2").values = Seq("option5", "option6")
 * </pre>
 * 
 * <p>
 * The previous command would essentially clear all selections first, then select <code>Option 5</code> and <code>Option 6</code>.
 * If instead you want to <em>not</em> clear any existing selection, just additionally select <code>Option 5</code> and <code>Option 6</code>,
 * you can use the <code>+=</code> operator, like this.
 * </p>
 * 
 * <pre class="stHighlight">
 * multiSel("select2").values += "option5"
 * multiSel("select2").values += "option6"
 * </pre>
 * 
 * <p>
 * To clear a specific option, pass its name to <code>clear</code>:
 * </p>
 * 
 * <pre class="stHighlight">
 * selectList("select2").clear("option5")
 * </pre>
 * 
 * <p>
 * To clear all selections, call <code>clearAll</code>:
 * </p>
 * 
 * <pre class="stHighlight">
 * selectList("select2").clearAll()
 * </pre>
 * 
 * <p>
 * You can access the current selections with <code>values</code>, which returns an <code>IndexedSeq[String]</code>:
 * </p>
 * 
 * <pre class="stHighlight">
 * multiSel("select2").values should have size 2
 * multiSel("select2").values(0) should be ("option5")
 * multiSel("select2").values(1) should be ("option6")
 * </pre>
 * 
 * <h3>Clicking and submitting</h3>
 * 
 * <p>
 * You can click on any element with &ldquo;<code>click on</code>&rdquo; as shown previously:
 * </p>
 * 
 * <pre class="stHighlight">
 * click on "aButton"
 * click on name("aTextField")
 * </pre>
 * 
 * <p>
 * If the requested element is not found, <code>click on</code> will throw an exception, failing the test.
 * </p>
 * 
 * <pre class="stHighlight">
 * </pre>
 * 
 * <p>
 * Clicking on a input element will give it the,focus. If current focus is in on an input element within a form, you can submit the form by 
 * calling <code>submit</code>:
 * </p>
 * 
 * <pre class="stHighlight">
 * submit()
 * </pre>
 * 
 * <h2>Switching</h2>
 * 
 * <p>
 * You can switch to a popup alert using the following code:
 * </p>
 * 
 * <pre class="stHighlight">
 * switch to alert
 * </pre>
 * 
 * <p>
 * to switch to a frame, you could:
 * </p>
 * 
 * <pre class="stHighlight">
 * switch to frame(0) // switch by index
 * switch to frame("name") // switch by name
 * </pre>
 * 
 * <p>
 * If you have reference to a window handle (can be obtained from calling windowHandle/windowHandles), you can switch to a particular 
 * window by:
 * </p>
 * 
 * <pre class="stHighlight">
 * switch to window(windowHandle)
 * </pre>
 * 
 * <p>
 * Similar to what you got in Selenium, you can also switch to active element and default content:
 * </p>
 * 
 * <pre class="stHighlight">
 * switch to activeElement
 * switch to defaultContent
 * </pre>
 * 
 * <h2>Navigation history</h2>
 * 
 * <p>
 * In real web browser, you can press the 'Back' button to go back to previous page.  To emulate that action in your test, you can call <code>goBack</code>:
 * </p>
 * 
 * <pre class="stHighlight">
 * goBack()
 * </pre>
 * 
 * <p>
 * To emulate the 'Forward' button, you can call:
 * </p>
 * 
 * <pre class="stHighlight">
 * goForward()
 * </pre>
 * 
 * And to refresh or reload the current page, you can call:
 * 
 * <pre class="stHighlight">
 * reloadPage()
 * </pre>
 * 
 * <h2>Cookies!</h2>
 * 
 * <p>To create a new cookie, you'll say:</p>
 * 
 * <pre class="stHighlight">
 * add cookie ("cookie_name", "cookie_value")
 * </pre>
 * 
 * <p>
 * to read a cookie value, you do:
 * </p>
 * 
 * <pre class="stHighlight">
 * cookie("cookie_name").value should be ("cookie_value") // If value is undefined, throws TFE right then and there. Never returns null.
 * </pre>
 * 
 * <p>
 * In addition to the common use of name-value cookie, you can pass these extra fields when creating the cookie, available ways are:
 * </p>
 * 
 * <pre class="stHighlight"> // TODO: Use a single method with default values instead of overloading, if can copy Selenium's defaults
 * cookie(name: String, value: String)
 * cookie(name: String, value: String, path: String)
 * cookie(name: String, value: String, path: String, expiry: Date)
 * cookie(name: String, value: String, domain: String, path: String, expiry: Date)
 * cookie(name: String, value: String, domain: String, path: String, expiry: Date, secure: Boolean)
 * </pre>
 * 
 * and to read those extra fields:
 * 
 * <pre class="stHighlight">
 * cookie("cookie_name").value   // Read cookie's value
 * cookie("cookie_name").path    // Read cookie's path
 * cookie("cookie_name").expiry  // Read cookie's expiry
 * cookie("cookie_name").domain  // Read cookie's domain
 * cookie("cookie_name").isSecure  // Read cookie's isSecure flag
 * </pre>
 * 
 * <p>
 * In order to delete a cookie, you could use the following code: 
 * </p>
 * 
 * <pre class="stHighlight">
 * delete cookie "cookie_name"
 * </pre>
 * 
 * <p>
 * or to delete all cookies in the same domain:-
 * </p>
 * 
 * <pre class="stHighlight">
 * delete all cookies
 * </pre>
 * 
 * <h2>Implicit wait</h2>
 * 
 * <p>
 * To set the implicit wait, you can call implicitlyWait method:
 * </p>
 * 
 * <pre class="stHighlight">
 * implicitlyWait(Span(10, Seconds))
 * </pre>
 * 
 * <h2>Page source and current URL</h2>
 * 
 * <p>
 * It is possible to get the html source of currently loaded page, using:
 * </p>
 * 
 * <pre class="stHighlight">
 * pageSource
 * </pre>
 * 
 * <p>
 * and if needed, get the current URL of currently loaded page:
 * </p>
 * 
 * <pre class="stHighlight">
 * currentUrl
 * </pre>
 * 
 * <h2>Screen capture</h2>
 * 
 * <p>
 * You can capture screen using the following code:
 * </p>
 * 
 * <pre class="stHighlight">
 * val file = capture
 * </pre>
 * 
 * <p>
 * By default, the captured image file will be saved in temporary folder (returned by java.io.tmpdir property), with random file name 
 * ends with .png extension.  You can specify a fixed file name:
 * </p>
 * 
 * <pre class="stHighlight">
 * capture to "MyScreenShot.png"
 * </pre>
 * 
 * <p>
 * or
 * </p>
 * 
 * <pre class="stHighlight">
 * capture to "MyScreenShot"
 * </pre>
 * 
 * <p>
 * Both will result in a same file name <code>MyScreenShot.png</code>.
 * </p>
 * 
 * <p>
 * You can also change the target folder screenshot file is written to, by saying:
 * </p>
 * 
 * <pre class="stHighlight">
 * capture set "/home/your_name/screenshots"
 * </pre>
 *
 * <p>
 * If you mix in <code>ScreenshotFixture</code>, ScalaTest will capture a screenshot and store it to either the system temp directory
 * or a directory you choose, and send the filename to the report, associated with the failed test.
 * </p>
 * 
 * <h2>Using the page object pattern</h2>
 *
 * <p>
 * If you use the page object pattern, mixing trait <code>Page</code> into your page classes will allow you to use the <code>go to</code>
 * syntax with your page objects. Here's an example:
 * </p>
 *
 * <pre>
 * class HomePage extends Page {
 *   val url = "localhost:9000/index.html
 * }
 *
 * val homePage = new HomePage
 * go to homePage
 * </pre>
 *
 * <h2>Executing JavaScript</h2>
 *
 * <p>
 * To execute arbitrary JavaScript, for example, to test some JavaScript functions on your page, pass it to <code>executeScript</code>:
 * </p>
 *
 * <pre>
 * executeScript("modifyDOM();")
 * </pre>
 *
 * <h2>Querying for elements</h2>
 *
 * <pre>
 * val ele: Option[Element] = find("q")
 *
 * val eles: IndexedSeq[Element] = findAll(className("small"))
 * for (e <- eles; if e.tagName != "input")
 *   e should be ('displayed)
 * val textFields = eles filter { tf.isInstanceOf[TextField] }
 * <pre>
 *
 * @author Chua Chee Seng
 * @author Bill Venners
 */
trait WebBrowser { 

  case class Point(x: Int, y: Int)
  case class Dimension(width: Int, height: Int)

  trait Element {
    def location: Point = Point(underlying.getLocation.getX, underlying.getLocation.getY)
    def size: Dimension = Dimension(underlying.getSize.getWidth, underlying.getSize.getHeight)
    def isDisplayed: Boolean = underlying.isDisplayed
    def isEnabled: Boolean = underlying.isEnabled
    def isSelected: Boolean = underlying.isSelected
    def tagName: String = underlying.getTagName
    def underlying: WebElement
  }

  // TODO: go to a Page instance. if Page, just ask for Url by accessing url.
  trait Page {
    val url: String
  }

  // fluentLinium has a doubleClick. Wonder how they are doing that?

  class CookieWrapper(cookie: Cookie) 
    extends Cookie(cookie.getName, cookie.getValue, cookie.getDomain, cookie.getPath, cookie.getExpiry, cookie.isSecure) {
    override def equals(o: Any): Boolean = cookie.equals(o)
    def domain: String = cookie.getDomain 
    def expiry: Date = cookie.getExpiry 
    def name: String = cookie.getName
    def path: String = cookie.getPath 
    def value: String = cookie.getValue
    override def hashCode: Int = cookie.hashCode
    def secure: Boolean = cookie.isSecure
    override def toString: String = cookie.toString 
  }

  class CookiesNoun

  sealed abstract class SwitchTarget[T] {
    def switch(driver: WebDriver): T
  }
    
  final class ActiveElementTarget extends SwitchTarget[WebElement] {
    def switch(driver: WebDriver): WebElement = {
      driver.switchTo.activeElement
    }
  }
  
  final class AlertTarget extends SwitchTarget[Alert] {
    def switch(driver: WebDriver): Alert = { 
      driver.switchTo.alert
    }
  }
  
  final class DefaultContentTarget extends SwitchTarget[WebDriver] {
    def switch(driver: WebDriver): WebDriver = {
      driver.switchTo.defaultContent
    }
  }
    
  final class FrameIndexTarget(index: Int) extends SwitchTarget[WebDriver] {
    def switch(driver: WebDriver): WebDriver = 
      try {
        driver.switchTo.frame(index)
      }
      catch {
        case e: org.openqa.selenium.NoSuchFrameException => 
          throw new TestFailedException(
                     sde => Some("Frame at index '" + index + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "switch", 1)
                   )
      }
  }
  
  final class FrameNameOrIdTarget(nameOrId: String) extends SwitchTarget[WebDriver] {
    def switch(driver: WebDriver): WebDriver = 
      try {
        driver.switchTo.frame(nameOrId)
      }
      catch {
        case e: org.openqa.selenium.NoSuchFrameException => 
          throw new TestFailedException(
                     sde => Some("Frame with name or ID '" + nameOrId + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "switch", 1)
                   )
      }
  }
  
  final class FrameWebElementTarget(element: WebElement) extends SwitchTarget[WebDriver] {
    def switch(driver: WebDriver): WebDriver = 
      try {
        driver.switchTo.frame(element)
      }
      catch {
        case e: org.openqa.selenium.NoSuchFrameException => 
          throw new TestFailedException(
                     sde => Some("Frame element '" + element + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "switch", 1)
                   )
      }
  }

  final class WindowTarget(nameOrHandle: String) extends SwitchTarget[WebDriver] {
    def switch(driver: WebDriver): WebDriver =
      try {
        driver.switchTo.window(nameOrHandle)
      }
      catch {
        case e: org.openqa.selenium.NoSuchWindowException => 
          throw new TestFailedException(
                     sde => Some("Window with nameOrHandle '" + nameOrHandle + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "switch", 1)
                   )
      }
  }
  
  private def isTextField(webElement: WebElement): Boolean = 
    webElement.getTagName.toLowerCase == "input" && webElement.getAttribute("type").toLowerCase == "text"

  private def isTextArea(webElement: WebElement): Boolean = 
    webElement.getTagName.toLowerCase == "textarea"
  
  private def isCheckBox(webElement: WebElement): Boolean = 
    webElement.getTagName.toLowerCase == "input" && webElement.getAttribute("type").toLowerCase == "checkbox"
      
  final class TextField(webElement: WebElement) extends Element {
    
    if(!isTextField(webElement))
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not text field."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    
    def value: String = webElement.getAttribute("value")  
    def value_=(value: String) {
      webElement.clear()
      webElement.sendKeys(value)
    }
    def text: String = webElement.getText
    def attribute(name: String): String = webElement.getAttribute(name)
    def underlying: WebElement = webElement
  }
  
  final class TextArea(webElement: WebElement) extends Element {
    if(!isTextArea(webElement))
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not text area."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    
    def value: String = webElement.getAttribute("value")
    def value_=(value: String) {
      webElement.clear()
      webElement.sendKeys(value)
    }
    def text: String = webElement.getText
    def attribute(name: String): String = webElement.getAttribute(name)
    def underlying: WebElement = webElement
  }

  final class RadioButton(groupName: String, driver: WebDriver) extends Element {
    
    private val groupElements = driver.findElements(By.name(groupName)).toList
    if (groupElements.length == 0)
      throw new TestFailedException(
                     sde => Some("Radio Button with group name '" + groupName + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    if (!groupElements.forall(e => e.getTagName == "input" && e.getAttribute("type") == "radio"))
      throw new TestFailedException(
                     sde => Some("Not all elements with name '" + groupName + "' is radio button."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    
    def value: String = selection match {
      case Some(v) => v
      case None => 
        throw new TestFailedException(
                     sde => Some("The Option on which value was invoked was not defined."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "value", 1)
                   )
    }

    def selection: Option[String] = {
      val radios = driver.findElements(By.name(groupName)).toList
      radios.find(_.isSelected) match {
        case Some(radio) => 
          Some(radio.getAttribute("value"))
        case None =>
          None
      }
    }
    
    def value_=(value: String) {
      val radios = driver.findElements(By.name(groupName)).toList
      radios.find(_.getAttribute("value") == value) match {
        case Some(radio) => 
          radio.click()
        case None =>
          throw new org.openqa.selenium.NoSuchElementException("Radio button value '" + value + "' not found for group '" + groupName + "'.")
      }
    }
    
    def underlying: WebElement = {
      val radios = driver.findElements(By.name(groupName)).toList
      radios.find(_.isSelected) match {
        case Some(radio) => 
          radio
        case None =>
          null
      }
    }
  }

  final class Checkbox(webElement: WebElement) extends Element {
    if(!isCheckBox(webElement))
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not check box."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    
    def select() {
      if (!webElement.isSelected)
        webElement.click()
    }
    def clear() {
      if (webElement.isSelected())
        webElement.click()
    }
    def underlying: WebElement = webElement
  }
  
  // TODO: Do I throw an exception from += if it isn't there? Actually I think this may be wrong. Need
  // To actually do it to the ...
  class RichIndexedSeq(indexedSeq: IndexedSeq[String]) {
      def +(value: String): IndexedSeq[String] = indexedSeq :+ value
      def -(value: String): IndexedSeq[String] = indexedSeq.filter(_ != value)
  }
  
  implicit def vector2RichIndexedSeq(indexedSeq: IndexedSeq[String]): RichIndexedSeq = new RichIndexedSeq(indexedSeq)
  
  // Should never return null.
  class StSingleSelect(webElement: WebElement) extends Element {
    if(webElement.getTagName.toLowerCase != "select")
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not select."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    private val select = new Select(webElement)
    if (select.isMultiple)
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not a single-selection list."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    
    def selection = {
      val first = select.getFirstSelectedOption
      if (first == null)
        None
      else
        Some(first.getAttribute("value"))
    }
    
    def value: String = selection match {
      case Some(v) => v
      case None => 
        throw new TestFailedException(
                     sde => Some("The Option on which value was invoked was not defined."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "value", 1)
                   )
    }
    
    def value_=(value : String) {
      select.selectByValue(value)
    }
    
    def underlying: WebElement = webElement
  }

  class StMultiSelect(webElement: WebElement) extends Element {
    if(webElement.getTagName.toLowerCase != "select")
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not select."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    private val select = new Select(webElement)
    if (!select.isMultiple)
      throw new TestFailedException(
                     sde => Some("Element " + webElement + " is not a multi-selection list."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "this", 1)
                   )
    
    def clear(value: String) {
      select.deselectByValue(value)
    }
  
    def selections = {
      val elementSeq = select.getAllSelectedOptions.toIndexedSeq
      val valueSeq = elementSeq.map(_.getAttribute("value"))
      if (valueSeq.length > 0)
        Some(valueSeq)
      else
        None
    }

    def values: IndexedSeq[String] = selections match {
      case Some(v) => v
      case None => IndexedSeq.empty
    }
    
    def values_=(values: IndexedSeq[String]) {
      clearAll()
      values.foreach(select.selectByValue(_))
    }
    
    def clearAll() {
      select.deselectAll()
    }
    
    def underlying: WebElement = webElement
  }
  
  object go {
    def to(url: String)(implicit driver: WebDriver) {
      driver.get(url)
    }
    
    def to(page: Page)(implicit driver: WebDriver) {
      driver.get(page.url)
    }
  }
  
  def close(implicit driver: WebDriver) {
    driver.close()
  }
  
  def title(implicit driver: WebDriver): String = driver.getTitle
  
  def pageSource(implicit driver: WebDriver): String = driver.getPageSource
  
  def currentUrl(implicit driver: WebDriver): String = driver.getCurrentUrl
  
  def id(elementId: String)(implicit driver: WebDriver): WebElement = 
    try { 
      driver.findElement(By.id(elementId))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with id '" + elementId + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "id", 1)
                   )
    }
  
  def name(elementName: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.name(elementName))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with name '" + elementName + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "name", 1)
                   )
    }
  
  def idOrName(elementIdOrName: String)(implicit driver: WebDriver): WebElement = 
    try {  
      try {
        driver.findElement(By.id(elementIdOrName))
      }
      catch {
        case _ => 
          driver.findElement(By.name(elementIdOrName))
      }
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with id or name '" + elementIdOrName + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "idOrName", 1)
                   )
    }
    
  def xpath(path: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.xpath(path))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with xpath '" + path + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "xpath", 1)
                   )
    }
  
  def className(className: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.className(className))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with className '" + className + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "className", 1)
                   )
    }
  
  def cssSelector(cssSelector: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.cssSelector(cssSelector))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with cssSelector '" + cssSelector + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "cssSelector", 1)
                   )
    }
  
  def linkText(linkText: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.linkText(linkText))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with linkText '" + linkText + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "linkText", 1)
                   )
    }
  
  def partialLinkText(partialLinkText: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.partialLinkText(partialLinkText))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with partialLinkText '" + partialLinkText + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "partialLinkText", 1)
                   )
    }
  
  def tagName(tagName: String)(implicit driver: WebDriver): WebElement = 
    try {
      driver.findElement(By.tagName(tagName))
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        throw new TestFailedException(
                     sde => Some("Element with tagName '" + tagName + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "tagName", 1)
                   )
    }
    
  def find(elementIdOrNameOrGroupName: String)(implicit driver: WebDriver): Option[Element] = {
    try {
      // Try with radio button first.
      val groupElements = driver.findElements(By.name(elementIdOrNameOrGroupName)).toList
      if (groupElements.length > 0 && groupElements.forall(e => e.getTagName == "input" && e.getAttribute("type") == "radio"))
        Some(new RadioButton(elementIdOrNameOrGroupName, driver))
      else {
        val element = try {
          driver.findElement(By.id(elementIdOrNameOrGroupName))
        }
        catch {
          case _ => 
            driver.findElement(By.name(elementIdOrNameOrGroupName))
        }
      
        if (isTextField(element))
          Some(new TextField(element))
        else if (isTextArea(element))
          Some(new TextArea(element))
        else if (isCheckBox(element))
          Some(new Checkbox(element))
        else if (element.getTagName.toLowerCase == "select") {
          val select = new Select(element)
          if (select.isMultiple)
            Some(new StMultiSelect(element))
          else
            Some(new StSingleSelect(element))
        }
        else
          Some(new Element() { def underlying = element })
      }
      
      /**/
    }
    catch {
      case e: org.openqa.selenium.NoSuchElementException => 
        None
    }
  }
  
  def textField(webElement: WebElement) = new TextField(webElement)
  
  def textField(elementIdOrName: String)(implicit driver: WebDriver) = new TextField(idOrName(elementIdOrName))
  
  def textArea(webElement: WebElement) = new TextArea(webElement)
  
  def textArea(elementIdOrName: String)(implicit driver: WebDriver) = new TextArea(idOrName(elementIdOrName))
  
  def radioButton(groupName: String)(implicit driver: WebDriver) = new RadioButton(groupName, driver)
  
  def checkbox(webElement: WebElement) = new Checkbox(webElement)
  
  def checkbox(elementIdOrName: String)(implicit driver: WebDriver) = new Checkbox(idOrName(elementIdOrName))
  
  def singleSel(webElement: WebElement) = new StSingleSelect(webElement)
  
  def singleSel(elementIdOrName: String)(implicit driver: WebDriver) = new StSingleSelect(idOrName(elementIdOrName))
  
  def multiSel(webElement: WebElement) = new StMultiSelect(webElement)
  
  def multiSel(elementIdOrName: String)(implicit driver: WebDriver) = new StMultiSelect(idOrName(elementIdOrName))
  
  def button(elementIdOrName: String)(implicit driver: WebDriver): WebElement = idOrName(elementIdOrName)
  
  object click {
    def on(element: WebElement) {
      element.click()
    }
  
    def on(elementIdOrName: String)(implicit driver: WebDriver) {
      on(idOrName(elementIdOrName))
    }
  }
  
  def submit()(implicit driver: WebDriver) {
    (switch to activeElement).submit()
  }
  
  def implicitlyWait(timeout: Span)(implicit driver: WebDriver) {
    driver.manage.timeouts.implicitlyWait(timeout.totalNanos, TimeUnit.NANOSECONDS)
  }
  
  def wait[T](timeout: Span, interval: Span = Span(500L, Milliseconds))(f: => T)(implicit driver: WebDriver) {
    new WebDriverWait(driver, timeout.totalNanos / 1000000000L, interval.totalNanos / 1000000)
      .until(new ExpectedCondition[T]() {
        override def apply(driver: WebDriver) = {
          f
        }
      })
  }
  
  def quit(implicit driver: WebDriver) {
    driver.quit()
  }
  
  def windowHandle(implicit driver: WebDriver): String = driver.getWindowHandle
  def windowHandles(implicit driver: WebDriver): Set[String] = driver.getWindowHandles.toSet
  
  object switch {
    def to[T](target: SwitchTarget[T])(implicit driver: WebDriver): T = {
      target.switch(driver)
    }
  }
  val activeElement = new ActiveElementTarget()
  val alert = new AlertTarget()
  val defaultContent = new DefaultContentTarget()
  def frame(index: Int) = new FrameIndexTarget(index)
  def frame(nameOrId: String) = new FrameNameOrIdTarget(nameOrId)
  def frame(element: WebElement) = new FrameWebElementTarget(element)
  def window(nameOrHandle: String) = new WindowTarget(nameOrHandle)
  
  def goBack()(implicit driver: WebDriver) {
    driver.navigate.back()
  }
  
  def goForward()(implicit driver: WebDriver) {
    driver.navigate.forward()
  }
  
  def reloadPage()(implicit driver: WebDriver) {
    driver.navigate.refresh()
  }
  
  // TODO: Use a single cookie method with default param values instead of overloading
  object add {
    private def addCookie(cookie: Cookie)(implicit driver: WebDriver) {
      driver.manage.addCookie(cookie)
    }
    
    def cookie(name: String, value: String)(implicit driver: WebDriver) {
      addCookie(new Cookie(name, value))
    }
    
    def cookie(name: String, value: String, path: String)(implicit driver: WebDriver) { 
      addCookie(new Cookie(name, value, path))
    }
    
    def cookie(name: String, value: String, path: String, expiry: Date)(implicit driver: WebDriver) { 
      addCookie(new Cookie(name, value, path, expiry))
    }
    
    def cookie(name: String, value: String, domain: String, path: String, expiry: Date)(implicit driver: WebDriver) { 
      addCookie(new Cookie(name, value, domain, path, expiry))
    }
    def cookie(name: String, value: String, domain: String, path: String, expiry: Date, secure: Boolean)(implicit driver: WebDriver) { 
      addCookie(new Cookie(name, value, domain, path, expiry, secure))
    }
  }
  
  def cookie(name: String)(implicit driver: WebDriver): CookieWrapper = {
    getCookie(name)
  }
  
  private def getCookie(name: String)(implicit driver: WebDriver): CookieWrapper = {
    driver.manage.getCookies.toList.find(_.getName == name) match {
      case Some(cookie) => 
        new CookieWrapper(cookie)
      case None =>
        throw new TestFailedException(
                     sde => Some("Cookie '" + name + "' not found."),
                     None,
                     getStackDepthFun("WebBrowser.scala", "getCookie", 1)
                   )
    }
  }
  
  object delete {
    private def deleteCookie(name: String)(implicit driver: WebDriver) {
      val cookie = getCookie(name)
      if (cookie == null)
        throw new org.openqa.selenium.NoSuchElementException("Cookie '" + name + "' not found.")
      driver.manage.deleteCookie(cookie)
    }
    
    def cookie(name: String)(implicit driver: WebDriver) {
      deleteCookie(name)
    }
    
    def all(cookies: CookiesNoun)(implicit driver: WebDriver) {
      driver.manage.deleteAllCookies()
    }
  }
  
  val cookies = new CookiesNoun
  
  object capture {
    
    private var targetDir = new File(System.getProperty("java.io.tmpdir"))
    
    def set(targetDirPath: String) {
      targetDir = 
        if (targetDirPath.endsWith(File.separator))
          new File(targetDirPath)
        else
          new File(targetDirPath + File.separator)
      if (!targetDir.exists)
        targetDir.mkdirs()
    }
    
    def to(fileName: String)(implicit driver: WebDriver) {
      driver match {
        case takesScreenshot: TakesScreenshot => 
          val tmpFile = takesScreenshot.getScreenshotAs(OutputType.FILE)
          val outFile = new File(targetDir, if (fileName.toLowerCase.endsWith(".png")) fileName else fileName + ".png")
          new FileOutputStream(outFile) getChannel() transferFrom(
          new FileInputStream(tmpFile) getChannel, 0, Long.MaxValue )
        case _ =>
          throw new UnsupportedOperationException("Screen capture is not support by " + driver.getClass.getName)
      }
    }
    
    def apply()(implicit driver: WebDriver): File = {
      driver match {
        case takesScreenshot: TakesScreenshot => 
          val tmpFile = takesScreenshot.getScreenshotAs(OutputType.FILE)
          val fileName = tmpFile.getName
          val outFile = new File(targetDir, if (fileName.toLowerCase.endsWith(".png")) fileName else fileName + ".png")
          new FileOutputStream(outFile) getChannel() transferFrom(
          new FileInputStream(tmpFile) getChannel, 0, Long.MaxValue )
          outFile
        case _ =>
          throw new UnsupportedOperationException("Screen capture is not support by " + driver.getClass.getName)
      }
    }
  }
  
  def withScreenshot(fun: => Unit)(implicit driver: WebDriver) {
    try {
      fun
    }
    catch {
      case e: org.scalatest.exceptions.ModifiableMessage[_] =>
        throw e.modifyMessage{ (currentMessage: Option[String]) => 
          val captureFile: File = capture.apply()
          currentMessage match {
            case Some(currentMsg) => 
              Some(currentMsg + "; screenshot captured in " + captureFile.getAbsolutePath)
            case None => 
              Some("screenshot captured in " + captureFile.getAbsolutePath)
          }
        }
    }
  }
  
  private def getStackDepthFun(fileName: String, methodName: String, adjustment: Int = 0): (StackDepthException => Int) = { sde =>
    getStackDepth(sde.getStackTrace, fileName, methodName, adjustment)
  }
  
  private def getStackDepth(stackTrace: Array[StackTraceElement], fileName: String, methodName: String, adjustment: Int = 0) = {
    val stackTraceList = stackTrace.toList

    val fileNameIsDesiredList: List[Boolean] =
      for (element <- stackTraceList) yield
        element.getFileName == fileName // such as "Checkers.scala"

    val methodNameIsDesiredList: List[Boolean] =
      for (element <- stackTraceList) yield
        element.getMethodName == methodName // such as "check"

    // For element 0, the previous file name was not desired, because there is no previous
    // one, so you start with false. For element 1, it depends on whether element 0 of the stack trace
    // had the desired file name, and so forth.
    val previousFileNameIsDesiredList: List[Boolean] = false :: (fileNameIsDesiredList.dropRight(1))

    // Zip these two related lists together. They now have two boolean values together, when both
    // are true, that's a stack trace element that should be included in the stack depth.
    val zipped1 = methodNameIsDesiredList zip previousFileNameIsDesiredList
    val methodNameAndPreviousFileNameAreDesiredList: List[Boolean] =
      for ((methodNameIsDesired, previousFileNameIsDesired) <- zipped1) yield
        methodNameIsDesired && previousFileNameIsDesired

    // Zip the two lists together, that when one or the other is true is an include.
    val zipped2 = fileNameIsDesiredList zip methodNameAndPreviousFileNameAreDesiredList
    val includeInStackDepthList: List[Boolean] =
      for ((fileNameIsDesired, methodNameAndPreviousFileNameAreDesired) <- zipped2) yield
        fileNameIsDesired || methodNameAndPreviousFileNameAreDesired

    val includeDepth = includeInStackDepthList.takeWhile(include => include).length
    val depth = if (includeDepth == 0 && stackTrace(0).getFileName != fileName && stackTrace(0).getMethodName != methodName) 
      stackTraceList.takeWhile(st => st.getFileName != fileName || st.getMethodName != methodName).length
    else
      includeDepth
    
    depth + adjustment
  }
}

object WebBrowser extends WebBrowser

trait HtmlUnit extends WebBrowser {
  implicit val webDriver = new HtmlUnitDriver()
}
object HtmlUnit extends HtmlUnit

trait Firefox extends WebBrowser {
  val firefoxProfile = new FirefoxProfile()
  implicit val webDriver = new FirefoxDriver(firefoxProfile)
}
object Firefox extends Firefox

trait Safari extends WebBrowser {
  implicit val webDriver = new SafariDriver()
}
object Safari extends Safari

trait Chrome extends WebBrowser {
  implicit val webDriver = new ChromeDriver()
}
object Chrome extends Chrome

trait InternetExplorer extends WebBrowser {
  implicit val webDriver = new InternetExplorerDriver()
}
object InternetExplorer extends InternetExplorer
