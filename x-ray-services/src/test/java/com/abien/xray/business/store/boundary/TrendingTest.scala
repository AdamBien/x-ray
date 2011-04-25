package com.abien.xray.business.store.boundary
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.control.TitleCache

class TrendingTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:Trending = _
  var titleCache:TitleCache = _
  var hits:Hits = _

  @Before
  def initialize = {
    cut = new Trending();
    titleCache = mock[TitleCache]
    hits = mock[Hits]
  }

  @Test
  def trendingWithTitle = {
  }
}