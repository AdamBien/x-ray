package com.abien.xray.business.statistics.boundary

import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.{JUnitSuite, ShouldMatchersForJUnit}
import org.junit.Before
import com.abien.xray.business.store.boundary.Hits
import org.junit._
import org.mockito.Mockito._
import javax.enterprise.event.Event


/**
 * User: blog.adam-bien.com
 * Date: 23.02.11
 * Time: 21:47
 */
class HourlyStatisticsCalculatorTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit {

  var cut: HourlyStatisticsCalculator = _

  @Before
  def setUp: Unit = {
    cut = new HourlyStatisticsCalculator()
    cut.hits = mock[Hits]
    cut.hourlyEvent = mock[Event[java.lang.Long]]
  }

  @Test
  def computeRatePerHour = {
    when(cut.hits.totalHits).thenReturn(10)
    cut.computeStatistics()
    cut.getHitsPerHour should be("10")
    when(cut.hits.totalHits).thenReturn(12)
    cut.computeStatistics()
    cut.getHitsPerHour should be("2")
  }

  @Test
  def computeMaxRatePerHour = {
    when(cut.hits.totalHits).thenReturn(10)
    cut.computeStatistics()
    cut.getMaxHitsPerHour should be("10")
    when(cut.hits.totalHits).thenReturn(22)
    cut.computeStatistics()
    cut.getMaxHitsPerHour should be("12")

    when(cut.hits.totalHits).thenReturn(23)
    cut.computeStatistics()
    cut.getMaxHitsPerHour should be("12")
  }
}