package com.abien.xray.business.store.boundary
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import com.abien.xray.business.store.entity.Post
import java.util.ArrayList

class TitleFilterTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  @Test
  def trim = {
    var cut = new TitleFilter()
    var first = new Post()
    var second = new Post()
    var actual = new ArrayList[Post]();
    actual.add(first)
    actual.add(second)
    var expected = cut.trim(actual,1)
    expected.size should be (1)
    expected.iterator.next should be (first)
  }
  //TODO - empty test
  
  def getPostsWithExistingTitle = {
    def firstNoTitle = new Post()
    def secondNoTitle = new Post()
    var thirdWithTitle = new Post()
    thirdWithTitle.setTitle("hugo")
    var listWithEmptyTitles = new ArrayList[Post]();
    listWithEmptyTitles.add(firstNoTitle)
    listWithEmptyTitles.add(secondNoTitle)
  }
}