package com.abien.xray.probe.http

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.junit._
import javax.servlet.FilterConfig
import java.util.HashMap
import java.util.Map

class HTTPRequestRESTInterceptorTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:HTTPRequestRESTInterceptor = _

  @Before
  def setUp: Unit = {
    cut = new HTTPRequestRESTInterceptor()
  }


  @Test
  def createMessageWithoutReferer = {
    val uri = "/hello/world"
    val referer = null
    val message = cut.createMessage(uri,referer)
    message should be (uri)
  }

  @Test
  def createMessageWithReferer = {
    val uri = "/hello/world"
    val referer = "adam-bien.com"
    val message = cut.createMessage(uri,referer)
    message should be (uri+HTTPRequestRESTInterceptor.DELIMITER+referer)
  }
  
  @Test
  def initWithServiceURL = {
    var filterConfig = mock[FilterConfig]
    def expected:String = "http://localhost:8080"
    when(filterConfig.getInitParameter(HTTPRequestRESTInterceptor.SERVICE_URL_KEY)).thenReturn(expected)
    cut.init(filterConfig)
    cut.url.toExternalForm should equal (expected)
    cut.client should not be (null)
  }

  @Test
  def initWithOutServiceURL = {
    var filterConfig = mock[FilterConfig]
    cut.init(filterConfig)
    cut.url should be (null)
    cut.client should be (null) //malformed URL
  }
  
  @Test
  def getInstrumentedRunnable = {
    def uri = "/hello"
    def headers = new HashMap[String,String]()
    cut.getInstrumentedRunnable(uri, headers) should not be (null)
  }
    
  @Test
  def sendAsync = {
    val uri = "http://hugo.com"
    def ref = "localhot"
    var headers = new HashMap[String,String]()
    headers.put("referer",ref);
    cut.client = mock[RESTClient]
    cut.sendAsync(uri, headers)
    verify(cut.client,times(1)).put(cut.createMessage(uri, ref),headers);
    HTTPRequestRESTInterceptor.getNrOfRejectedJobs should be (0)
  }
    
  @Test(timeout=500)
  def sendAsyncAndOverload = {
    val uri = "http://hugo.com"
    val ref = "localhost"
    cut.client = new RESTClient(){
        
      override def put(content:String,headers:Map[String,String]):String = {
        Thread.sleep(200)
        return "--"
      }
        
    }
    def headers = new HashMap[String,String]()
    val overload:Int = 10
    for(i <- 0 until (HTTPRequestRESTInterceptor.QUEUE_CAPACITY + HTTPRequestRESTInterceptor.NR_OF_THREADS +overload)){
      cut.sendAsync(uri + i, headers)
    }
    HTTPRequestRESTInterceptor.getNrOfRejectedJobs should be (overload)
  }
}
