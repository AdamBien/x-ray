package com.abien.xray.business.statistics.boundary

import com.abien.xray.business.store.boundary.Hits
import org.junit._
import Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import javax.persistence.EntityManager
import javax.persistence.Query
import java.util.ArrayList
import java.util.List
import com.abien.xray.business.statistics.entity.DailyHits


class DailyStatisticsCalculatorTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut: DailyStatisticsCalculator = _
    
  @Before
  def setUp: Unit = {
    cut = spy(new DailyStatisticsCalculator())
    cut.hits = mock[Hits]
    cut.em = mock[EntityManager]
  }


  @Test
  def computeTodaysStatistics() {
    val first = 1;
    val second = 5;
    when(cut.hits.totalHits()).thenReturn(first)
    cut.computeStatistics()
    cut.getTodaysHit() should be (0.toString)
    when(cut.hits.totalHits()).thenReturn(second)
    cut.getTodaysHit() should be ((second-first).toString)
  }

  @Test
  def computeYesterdaysStatistics() {
    val first:Long = 1;
    val second:Long = 5;
    when(cut.hits.totalHits()).thenReturn(first)
    cut.computeStatistics()
    cut.getYesterdaysHit() should be (first.toString)
    when(cut.hits.totalHits()).thenReturn(second)
    cut.computeStatistics()
    cut.getYesterdaysHit() should be ((second-first).toString)
  }

  @Test
  def restoreStatistics() {
    val total:Long = 10
    val yesterday:Long = 3
    var list = new ArrayList[DailyHits]
    list.add(new DailyHits(yesterday))
    doReturn(list).when(cut).getHistory()
    when(cut.hits.totalHits()).thenReturn(total)

    cut.restoreStatistics();

    cut.getYesterdaysHit should be (3.toString)
    cut.getTodaysHit should be (0.toString)

    when(cut.hits.totalHits()).thenReturn(total+1)
    cut.getTodaysHit should be (1.toString)
    cut.computeStatistics()

    cut.getYesterdaysHit should be (1.toString)
    cut.getTodaysHit should be (0.toString)


  }

}
