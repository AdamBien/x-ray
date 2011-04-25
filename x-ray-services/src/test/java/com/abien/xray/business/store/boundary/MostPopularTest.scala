package com.abien.xray.business.store.boundary
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.control.TitleCache
import com.abien.xray.business.store.entity.Hit

class MostPopularTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:MostPopular = _
  var hits:Hits = _

  @Before
  def initialize = {
    cut = new MostPopular()
    hits = mock[Hits]
  }

  @Test
  def convert ={
    def action = "dukeuri"
    def hit = new Hit(action,0)
    def post = cut.convert(hit)
    post.getUri should be (action)
    post.getNumberOfHits should be (0)
  }

  @Test
  def totalHitsAsString = {


  }
  
}