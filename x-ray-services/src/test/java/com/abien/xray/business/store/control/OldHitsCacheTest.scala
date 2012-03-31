package com.abien.xray.business.store.control

import org.scalatest.junit.{JUnitSuite, ShouldMatchersForJUnit}
import java.util.HashMap
import java.util.concurrent.atomic.AtomicLong
import org.junit.{Test, Before}

/**
 * User: blog.adam-bien.com
 * Date: 17.02.11
 * Time: 21:32
 */
class OldHitsCacheTest extends JUnitSuite with ShouldMatchersForJUnit{
  var cut:HitsCache = _

  @Before
  def setUp: Unit = {
    cut = new HitsCache(new HashMap[String,AtomicLong]())
  }


  @Test
  def computeChanges = {
    val action = "42"
    cut.increase(action)
    var actual = cut.getChangedEntriesAndClear
    actual.size should be (1)
    actual = cut.getChangedEntriesAndClear
    actual.size should be (0)
  }

  @Test
  def computeChangesWithDuplicates = {
    val action = "42"
    cut.increase(action)
    cut.increase(action)
    cut.increase(action)
    var actual = cut.getChangedEntriesAndClear
    actual.size should be (1)
    actual = cut.getChangedEntriesAndClear
    actual.size should be (0)
  }

  @Test
  def computeChangesWithMultipleEntries = {
    val action = "42"
    val another = "23"
    cut.increase(action)
    cut.increase(another)
    var actual = cut.getChangedEntriesAndClear
    actual.size should be (2)
    actual = cut.getChangedEntriesAndClear
    actual.size should be (0)
  }

  @Test
  def clear = {
    val action = "42"
    val another = "23"
    cut.increase(action)
    cut.increase(another)
    cut.clear
    var actual = cut.getChangedEntriesAndClear
    actual.size should be (0)
    actual = cut.getCache
    actual.size should be (0)
  }

}