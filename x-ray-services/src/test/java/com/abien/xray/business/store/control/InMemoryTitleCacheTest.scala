package com.abien.xray.business.store.control

import org.junit._
import Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar
import com.abien.xray.business.store.entity.Post.EMPTY;


class InMemoryTitleCacheTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:InMemoryTitleCache = _

  @Before
  def setUp: Unit = {
    cut = new InMemoryTitleCache()
    cut.initialize()
  }


  @Test
  def crud = {
    def uri = "localhost"
    def title = "this is localhost"
    cut.put(uri,title)
    cut.get(uri) should be (title)
    cut.remove(uri)
    cut.get(uri) should be (null)
  }
  
  @Test
  def getURIsWithoutTitle = {
    def uri = "localhost"
    def title = "this is localhost"
    var uris = cut.getURIsWithoutTitle()
    uris.isEmpty should be (true)
    cut.put(uri,EMPTY)
    uris = cut.getURIsWithoutTitle()
    uris.isEmpty should be (false)

  }

}
