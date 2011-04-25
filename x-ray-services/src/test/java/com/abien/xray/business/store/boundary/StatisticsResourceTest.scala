package com.abien.xray.business.store.boundary

import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.control._

class StatisticsResourceTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:StatisticsResource = _

  @Before
  def initialize = {
      cut = new StatisticsResource()
      cut.hits = mock[PersistentHitStore]
  }

  @Test
  def topReferersWithExcludeIsNull(){
    def maxNumber = 10
    cut.totalHitsAsString(null, maxNumber)
    verify(cut.hits,times(1)).getMostPopularURLs(maxNumber)
  }
  
  @Test
  def topReferersWithExcludeIsNotNull(){
    def maxNumber = 10
    def excludeString = "abien"
    cut.totalHitsAsString(excludeString, maxNumber)
    verify(cut.hits,times(1)).getMostPopularURLs(excludeString,maxNumber)
  }

}
