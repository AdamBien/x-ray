package com.abien.xray.business.statistics.boundary

import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit


class TrendTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit {

  var cut: Trend = _

  @Before
  def setUp: Unit = {
    cut = new Trend()
  }

  @Test
  def onHourlyStatistic = {
    cut.onHourlyStatistic(1)
    def list = cut.hourly(5)
    list.size should be(1)
    cut.onHourlyStatistic(2)
    list.size should be(2)
  }

  @Test
  def hoursSmallerThanSize = {
    cut.onHourlyStatistic(1)
    var list = cut.hourly(1)
    list.size should be(1)

    cut.onHourlyStatistic(2)

    list = cut.hourly(1)
    list.size should be(1)
  }

  @Test
  def trendHoursGreaterThanSize = {
    cut.onHourlyStatistic(1)
    cut.onHourlyStatistic(2)
    def list = cut.hourly(5);
    var iterator = list.listIterator;
    iterator.next.getHit should be(2)
    iterator.next.getHit should be(1)
  }

  @Test
  def trendHoursLesserThanSize = {
    cut.onHourlyStatistic(1)
    cut.onHourlyStatistic(2)
    cut.onHourlyStatistic(3)
    def list = cut.hourly(2)
    list.size should be(2)
    var iterator = list.listIterator;
    iterator.next.getHit should be(3)
    iterator.next.getHit should be(2)
  }

  @Test
  def trendHoursEqualSize = {
    cut.onHourlyStatistic(1)
    cut.onHourlyStatistic(2)
    cut.onHourlyStatistic(3)
    def list = cut.hourly(3)
    list.size should be(3)
    var iterator = list.listIterator;
    iterator.next.getHit should be(3)
    iterator.next.getHit should be(2)
    iterator.next.getHit should be(1)
  }

}
