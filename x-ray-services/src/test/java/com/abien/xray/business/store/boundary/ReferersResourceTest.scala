package com.abien.xray.business.store.boundary

import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.control._

class ReferersResourceTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:ReferersResource = _

  @Before
  def initialize = {
      cut = new ReferersResource()
      cut.hits = mock[Hits]
  }

  @Test
  def topReferersWithExcludeIsNull(){
    def maxNumber = 10
    cut.topReferers(null, maxNumber)
    verify(cut.hits,times(1)).topReferers(maxNumber)
  }
  
  @Test
  def topReferersWithExcludeIsNotNull(){
    def maxNumber = 10
    def excludeString = "abien"
    cut.topReferers(excludeString, maxNumber)
    verify(cut.hits,times(1)).topReferers(excludeString,maxNumber)
  }

}
