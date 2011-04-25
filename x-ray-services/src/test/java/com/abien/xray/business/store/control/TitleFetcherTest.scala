package com.abien.xray.business.store.control

import org.junit._
import Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import com.abien.xray.business.logging.boundary.DevNullLogger

class TitleFetcherTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit {

  var cut: TitleFetcher = _

  @Before
  def setUp: Unit = {
    cut = new TitleFetcher()
    cut.LOG = new DevNullLogger()
    var cache = new InMemoryTitleCache()
    cache.initialize()
    cut.titles = cache
    cut.persistentHitStore = mock[PersistentHitStore];
    cut.initialize()
  }


  @Test
  def extractTitleWithNullContent = {
    var content = cut.extractTitle(null)
    content should be(null)
  }

  @Test
  def extractTitleWithLeadingTag = {
    var content = cut.extractTitle("<title>Hugo")
    content should be(null)
  }

  @Test
  def extractTitleWithTrailingTag = {
    var content = cut.extractTitle("Hugo</title>")
    content should be(null)
  }

  @Test
  def extractTitle = {
    def title = "Hugo";
    var content = cut.extractTitle("<title>" + title + "</title>")
    content should be(title)
  }

  @Test
  def constructUriWithoutSlash = {
    def expected = TitleFetcher.BASE_URL + "/part"
    def actual = cut.constructUri("part")
    actual should be(expected)
  }

  @Test
  def constructUriWithSlash = {
    def expected = TitleFetcher.BASE_URL + "/part"
    def actual = cut.constructUri("/part")
    actual should be(expected)
  }

  @Test
  def ignoreBogusTitles = {
    cut.isBogus(bogus) should be(false)
    def bogus = "bogus"
    cut.ignoreURI(bogus)
    cut.isBogus(bogus) should be(true)

  }
}
