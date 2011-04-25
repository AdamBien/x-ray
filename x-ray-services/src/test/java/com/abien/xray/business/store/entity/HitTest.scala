package com.abien.xray.business.store.entity

import org.junit._
import Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.mock.MockitoSugar

class HitTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  @Test
  def titleWithTrailingSlash = {
    def origin = "A very long title/"
    def expected = "A very long title"
    val cut = new Hit(origin,0)
    def actual = cut.getActionId()
    actual should be (expected)
  }

  @Test
  def removeTrailingSlashWithNull = {
    def cut = new Hit()
    def actual = cut.trimTrailingSlash(null)
    actual should be (null)
  }

  @Test
  def removeTrailingSlashWithSlash = {
    def origin = "uri/"
    def expected = "uri"
    def cut = new Hit()
    def actual = cut.trimTrailingSlash(origin)
    actual should be (expected)
  }

 @Test
  def removeTrailingSlashWithoutSlash = {
    def origin = "uri"
    def expected = "uri"
    def cut = new Hit()
    def actual = cut.trimTrailingSlash(origin)
    actual should be (expected)
  }

}
