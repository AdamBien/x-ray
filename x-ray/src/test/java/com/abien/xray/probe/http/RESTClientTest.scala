package com.abien.xray.probe.http

import java.net.URL
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import java.lang.String

class RESTClientTest extends JUnitSuite with ShouldMatchersForJUnit{

    @Before
    def setUp: Unit = {
    }

    @Test
    def construction = {
      val url = new URL("http://localhost:8080/x-ray-testservlet-nomaven/roller/abien/entry/hugo")
      def restClient = new RESTClient(url)
      restClient.getPath should be ("/x-ray-testservlet-nomaven/roller/abien/entry/hugo")
      restClient.getPort should be (8080)
      restClient.getInetAddress().getHostName().toString should be ("localhost")
    }

     @Test
     def headerFormatting = {
       val url = new URL("http://localhost:8080/x-ray-testservlet-nomaven/roller/abien/entry/hugo")
       def restClient = new RESTClient(url)
       val key = "Content-Type"
       val value = "text/plain"
       val expected = "Content-Type: text/plain\r\n"
       val actual: String = restClient.getFormattedHeader(key, value)
       actual should be (expected)
     }
}
