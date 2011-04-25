package com.abien.xray

import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import java.lang.String

class XRayTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

    def error = "--"
    def malformedUrl = "http://localhost:8080/";
  
    @After
    def tearDown: Unit = {
    }

    @Test
    def fetchHitsPerHourWithConnectionError = {
      def xray = new XRay(malformedUrl);
      def hits = xray.getHitsPerHour()
      hits should be (error)
    }

    @Test
    def fetchHitsPerMinuteWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getHitsPerMinute()
      hits should be (error)
    }

    @Test
    def fetchMostPopularAsHtmlWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getMostPopularAsHtml()
      hits should be (error)
    }

    @Test
    def fetchTodayHitsWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getTodayHits()
      hits should be (error)
    }

    @Test
    def fetchYesterdayHitsWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getYesterdayHits()
      hits should be (error)
    }

    @Test
    def fetchTotalHitsWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getTotalHits()
      hits should be (error)
    }

    @Test
    def fetchTrendingAsHtmlWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getTrendingAsHtml()
      hits should be (error)
    }

    @Test
    def fetchHitsForPostWithConnectionError = {
      val url = "http://localhost:8080/";
      def xray = new XRay(malformedUrl);
      def hits = xray.getHitsForPost("-")
      hits should be (error)
    }
}
