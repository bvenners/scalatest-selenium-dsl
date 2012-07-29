name := "scalatest-seleniumdsl"
 
version := "0.1.0"
 
scalaVersion := "2.9.0"

libraryDependencies ++= Seq( 
  "org.seleniumhq.selenium" % "selenium-java" % "2.21.0",
  "org.scalatest" % "scalatest_2.9.0" % "1.8.RC2",  
  "org.eclipse.jetty" % "jetty-server" % "8.0.1.v20110908" % "test",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "test"
)

initialCommands := """import org.scalatest._
import selenium._
//import HtmlUnit.webDriver
import Firefox.webDriver
import WebBrowser._
import matchers.ShouldMatchers._
import concurrent.Eventually._
"""
