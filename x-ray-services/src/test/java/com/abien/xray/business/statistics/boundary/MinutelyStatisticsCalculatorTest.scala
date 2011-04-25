package com.abien.xray.business.statistics.boundary

import com.abien.xray.business.store.boundary.Hits
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import javax.enterprise.event.Event

class MinutelyStatisticsCalculatorTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit {

  var cut: MinutelyStatisticsCalculator = _

  @Before
  def setUp: Unit = {
    cut = new MinutelyStatisticsCalculator()
    cut.hits = mock[Hits]
    cut.minutelyEvent = mock[Event[java.lang.Long]]
  }


  @Test
  def computeRatePerMinute = {
    when(cut.hits.totalHits).thenReturn(10)
    cut.computeStatistics()
    cut.getHitsPerMinute should be("10")
    when(cut.hits.totalHits).thenReturn(12)
    cut.computeStatistics()
    cut.getHitsPerMinute should be("2")
  }

  @Test
  def computeMaxRatePerMinute = {
    when(cut.hits.totalHits).thenReturn(10)
    cut.computeStatistics()
    cut.getMaxHitsPerMinute should be("10")
    when(cut.hits.totalHits).thenReturn(22)
    cut.computeStatistics()
    cut.getMaxHitsPerMinute should be("12")

    when(cut.hits.totalHits).thenReturn(23)
    cut.computeStatistics()
    cut.getMaxHitsPerMinute should be("12")
  }
}
