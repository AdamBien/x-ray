package com.abien.xray.business.store.entity

import org.junit._
import Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar

class PostTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  @Test
  def shortenedTitle = {
    def title = "A very long title"
    val cut = new Post(null,title,0)
    def actual = cut.getShortenedTitle(10);
    actual should be ("A ver[...]")
  }

  @Test
  def shortenedTitleFullLength = {
    def title = "A very long title"
    var cut = new Post(null,title,0)
    def actual = cut.getShortenedTitle(500);
    actual should be (title)
  }

  @Test
  def comparable = {
    def unpopularTitle = "Unpopular"
    val unpopular = new Post(null,unpopularTitle,0)
    def title = "Popular"
    val popular = new Post(null,title,100)
    var result = unpopular.compareTo(popular)
    result should be (-1)
    result = popular.compareTo(unpopular)
    result should be (1)
    result = popular.compareTo(popular)
    result should be (0)
  }

  @Test
  def emptyTitle ={
    var empty = new Post();
    empty.isTitleEmpty should be (true)
    empty.setTitle(Post.EMPTY);
    empty.isTitleEmpty should be (true)
    empty.setTitle("something");
    empty.isTitleEmpty should be (false)

  }

}
