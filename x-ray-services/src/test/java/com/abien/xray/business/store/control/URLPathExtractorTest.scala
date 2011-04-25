package com.abien.xray.business.store.control

import org.junit._
import Assert._
import org.junit.Assert._

class URLPathExtractorTest {

  var extractor:URLPathExtractor = _

  @Before
  def setUp: Unit = {
    extractor =  new URLPathExtractor()
  }

  @Test
  def extractArticleTitleFromURL:Unit={
    def actual = "http://www.adam-bien.com/roller/abien/entry/mail_of_the_week_what"
    def expected = "/entry/mail_of_the_week_what"
    def result = extractor.extractPathSegmentFromURL(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractArticleTitleFromURLWithComment:Unit={
    def actual = "http://www.adam-bien.com/roller/abien/entry/mail_of_the_week_what#comments"
    def expected = "/entry/mail_of_the_week_what";
    def result = extractor.extractPathSegmentFromURL(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractArticleTitleFromURLWithConcreteComment:Unit={
    def actual = "http://www.adam-bien.com/roller/abien/entry/mail_of_the_week_what#comment-1282142068399"
    def expected = "/entry/mail_of_the_week_what";
    def result = extractor.extractPathSegmentFromURL(actual)
    assertEquals(expected,result)
  }

   @Test
  def extractDirectEntry:Unit={
    def actual = "http://www.adam-bien.com/roller/abien/"
    def expected = "/";
    def result = extractor.extractPathSegmentFromURL(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractDirectEntryWithoutSlash:Unit={
    def actual = "http://www.adam-bien.com/roller/abien"
    def expected = "/";
    def result = extractor.extractPathSegmentFromURL(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractMalformedURL:Unit={
    def actual = "/x-ray/something/hugotest"
    def expected = actual;
    def result = extractor.extractPathSegmentFromURL(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractURIWithReferer:Unit={
    def actual = "/x-ray/something/hugotest|blog.adam-bien.com"
    def expected = "/x-ray/something/hugotest";
    def result = extractor.extractURI(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractRefererWithReferer:Unit={
    def actual = "/x-ray/something/hugotest|blog.adam-bien.com"
    def expected = "blog.adam-bien.com";
    def result = extractor.extractReferer(actual)
    assertEquals(expected,result)
  }

  @Test
  def extractRefererWithoutReferer:Unit={
    def actual = "/x-ray/something/hugotest"
    def expected = null;
    def result = extractor.extractReferer(actual)
    assertEquals(expected,result)
  }

  @Test
  def dropPlusGetSuffix:Unit={
    def actual ="/entry/field_vs_property_based_access++GET+http://www.java-framework.com/roller/abien/entry/field_vs_property_based_access+%5B0,15749,46211%5D+-%3E+"
    def expected ="/entry/field_vs_property_based_access"
    def result = extractor.dropPlusGetSuffix(actual)
    assertEquals(expected,result)
  }

  @Test
  def dropPlusSuffix:Unit={
    def actual ="/entry/my_javaone_session_ts_3559+%5B0,11622,41666%5D+-%3E+"
    def expected ="/entry/my_javaone_session_ts_3559"
    def result = extractor.dropPlusGetSuffix(actual)
    assertEquals(expected,result)
  }

  @Test
  def dropTrailingSlash:Unit={
    def actual ="/entry/field_vs_property_based_access/"
    def expected ="/entry/field_vs_property_based_access"
    def result = extractor.dropTrailingSlash(actual)
    assertEquals(expected,result)
  }

  @Test
  def doNotDropTheOnlySlash:Unit={
    def actual ="/"
    def expected ="/"
    def result = extractor.dropTrailingSlash(actual)
    assertEquals(expected,result)
  }

}

