package com.abien.xray.business.store.boundary

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.control._
import java.lang.String
import com.abien.xray.business.store.entity.Hit
import javax.enterprise.event.Event
import com.abien.xray.business.monitoring.entity.Diagnostics
import javax.enterprise.event.Event
import java.util.HashMap
import com.abien.xray.business.logging.boundary.DevNullLogger

class HitsTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit {

  var cut: Hits = _

  @Before
  def setUp: Unit = {
    cut = new Hits()
    cut.LOG = new DevNullLogger()
    cut.hitStore = mock[PersistentHitStore];
    cut.monitoring = mock[Event[Diagnostics]];
    cut.refererStore = mock[PersistentRefererStore]
    cut.uriListener = mock[Event[String]]
    when(cut.hitStore.getHits).thenReturn(new ConcurrentHashMap[String, AtomicLong])
    when(cut.refererStore.getReferers).thenReturn(new ConcurrentHashMap[String, AtomicLong])
    cut.preloadCache()
  }

  @Test
  def updateStatistics = {
    cut.urlFilter = new URLFilter()
    cut.httpHeaderFilter = new HttpHeaderFilter()
    var uri = "/entry/java_ee_6_jboss_6"
    def referer = "locahost"
    var map = new HashMap[String, String]()
    map.put("referer", referer)
    map.put("xray_user-agent", "netscape")
    var before = cut.totalHits
    cut.updateStatistics(uri, referer, map)
    cut.totalHits should be(before + 1)

    uri = "/entry/newuri"
    before = cut.totalHits
    cut.updateStatistics(uri, referer, map)
    cut.totalHits should be(before + 1)

    uri = "/invalid"
    before = cut.totalHits
    cut.updateStatistics(uri, referer, map)
    cut.totalHits should be(before + 1)

  }

  @Test
  def storeURI = {
    val first = "duke";
    val second = "java";
    var firstCounter = cut.storeHitStatistics(first)
    firstCounter should be(1)
    var secondCounter = cut.storeHitStatistics(second)
    secondCounter should be(1)

    firstCounter = cut.storeHitStatistics(first)
    firstCounter should be(2)

    secondCounter = cut.storeHitStatistics(second)
    secondCounter should be(2)
  }

  def isRelevantForTrend = {
    var expected = cut.isRelevantForTrend("not")
    expected should be(false)
    expected = cut.isRelevantForTrend(null)
    expected should be(false)
    expected = cut.isRelevantForTrend("/entry/hugo")
    expected should be(true)
  }

  @Test
  def totalHits = {
    val first = "duke";
    val second = "java";
    cut.storeURI(first)
    cut.totalHits should be(1)
    cut.storeURI(first)
    cut.totalHits should be(2)
    cut.storeURI(second)
    cut.totalHits should be(3)
  }

  @Test
  def shortenRefererTooLong = {
    def actual = "123"
    def expected = "12"
    cut.shortenReferer(actual, 2) should be(expected)
  }

  @Test
  def shortenRefererTooShort = {
    def actual = "123"
    def expected = "123"
    cut.shortenReferer(actual, 5) should be(expected)
  }

  @Test
  def shortenRefererEqual = {
    def actual = "123"
    def expected = "123"
    cut.shortenReferer(actual, 3) should be(expected)
  }

  @Test
  def getHitsForURINonExisting = {
    val actual = cut.getHitsForURI("-")
    actual should be(0l)
  }

  @Test
  def getHitsForURIExistingURI = {
    val expected: Long = 42l
    val existingURI: String = "existing"
    var hit = new Hit()
    hit.setCount(expected)
    doReturn(hit).when(cut.hitStore).find(existingURI)
    val actual = cut.getHitsForURI(existingURI)
    actual should be(expected)
  }

  @Test
  def trending = {
    val first = "duke";
    val second = "java";
    cut.storeTrending(first) should be(1)
    cut.storeTrending(first) should be(2)
    cut.totalTrending should be(2)
    cut.storeTrending(second) should be(1)
    cut.totalTrending should be(3)
    cut.resetTrends
    cut.totalTrending should be(0)
  }

  @Test
  def sortTrending = {
    def popularUri = "popular"
    def unpopularUri = "unpopular"
    var unpopularHits = cut.storeTrending(unpopularUri)
    unpopularHits should be(1)

    var popularHits = cut.storeTrending(popularUri)
    popularHits should be(1)
    popularHits = cut.storeTrending(popularUri)
    popularHits should be(2)

    def trending = cut.getTrending
    trending.size should be(2)
    var iterator = trending.iterator
    var popular = iterator.next
    popular.getUri should be(popularUri)
    popular.getNumberOfHits should be(2)

    var unpopular = iterator.next
    unpopular.getUri should be(unpopularUri)
    unpopular.getNumberOfHits should be(1)
  }
}
