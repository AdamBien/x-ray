package com.abien.xray.business.store.control

import org.junit._
import Assert._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.mockito.Mockito._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar

class URLFilterTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

    var cut:URLFilter = _

    @Before
    def setUp: Unit = {
      cut = new URLFilter()
    }

    @Test
    def acceptsInteresting = {
      var result = cut.ignore("http://www.adam-bien.com/roller/abien/entry/mail_of_the_week_what")
      result should be (false)

      result = cut.ignore("/roller/abien/entry/mail_of_the_week_what")
      result should be (false)

      result = cut.ignore("/roller/abien/entry/")
      result should be (false)

      result = cut.ignore("/roller/abien/")
      result should be (false)

      result = cut.ignore("/roller/abien")
      result should be (false)

      result = cut.ignore("/roller/")
      result should be (false)

      result = cut.ignore("/roller")
      result should be (false)
    }

    @Test
    def ignoreBlackListed = {
      var result = cut.ignore("/roller/CommentAuthenticatorServlet")
      result should be (true)
    }


    @Test
    def ignoresNull = {
      val result = cut.ignore(null)
      result should be (true)
    }
    @Test
    def ignoresEmpty ={
      val result = cut.ignore("")
      result should be (true)
    }

    @Test
    def ingoreCSSEnding = {
      val result = cut.ignore("hugo.css");
      result should be (true)
  }

    @Test
    def ingoreJSEnding = {
      val result = cut.ignore(".js");
      result should be (true)
    }

}
