package com.abien.xray.business.store.boundary

import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.control._

class HitsResourceTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut: HitsResource = _

    @Before
    def setUp: Unit = {
      cut = new HitsResource
    }

    @Test
    def checkWhetherURLIsEmpty = {
      cut.isEmpty(null) should be (true)
      cut.isEmpty("") should be (true)
      cut.isEmpty(" ") should be (true)
    }

   @Test
    def prependPrefix_PrefixExists = {
      val expected = "/entry/hugo"
      val actual: String = cut.prependPrefix(expected)
      actual should be (expected)
    }

    @Test
    def prependPrefix_PrefixNotExists = {
      val uri = "hugo"
      val expected = "/entry/hugo"
      val actual: String = cut.prependPrefix(uri)
      actual should be (expected)
    }

}
