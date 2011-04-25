package com.abien.xray.business.store.control

import org.scalatest.mock.MockitoSugar
import org.junit.{Test, Before}
import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import java.util.HashMap
import javax.ws.rs.core.HttpHeaders


/**
 * User: blog.adam-bien.com
 * Date: 06.01.11
 * Time: 20:21
 */

class HttpHeaderFilterTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

    var cut:HttpHeaderFilter = _

    @Before
    def setUp: Unit = {
      cut = new HttpHeaderFilter()
    }

    @Test
    def rejectRobots = {
      val headers = new HashMap[String,String]();
      headers.put(HttpHeaderFilter.HEADER_PREFIX + HttpHeaders.USER_AGENT.toLowerCase(),"TVersity Media Robot");
      val result = cut.ignore(headers)
      result should be (true)
    }

    @Test
    def ignoresNull = {
      val result = cut.ignore(null)
      result should be (true)
    }
    @Test
    def ignoresEmpty ={
      val result = cut.ignore(new HashMap[String,String]())
      result should be (true)
    }
}