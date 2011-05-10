package com.abien.xray.probe.monitoring

import com.abien.xray.probe.http.HTTPRequestRESTInterceptor
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.mockito.Mockito
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import javax.servlet.ServletRequest
import java.util.HashMap
import java.util.Map
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import org.scalatest.mock.MockitoSugar;


class XRayMonitoringTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{
  
  var filter:HTTPRequestRESTInterceptor = _
  var cut:XRayMonitoring = _
  var request:HttpServletRequest = _
  var response:ServletResponse = _

  
  @Before
  def setUp: Unit = {
    cut = new XRayMonitoring()
    filter = getFilterWithPerformance(0)
    request = mock[HttpServletRequest]
    response = mock[HttpServletResponse]
    cut.reset()
  }


  @Test
  def getNrOfRejectedJobs = {
    cut.getNrOfRejectedJobs() should be(0)
  }

  @Test
  def getApplicationPerformance = {
    var expected:Long = 50
    var chain = getFilterChainWithPerformance(expected)
    filter.doFilter(request,response,chain)
    cut.getApplicationPerformance should be >= (expected)
  }

  @Test
  def getXRayPerformance = {
    cut.getXRayPerformance should be(0)
  }
  
  @Test
  def getWorstApplicationPerformance:Unit ={
    var expected:Long = 50
    var chain = getFilterChainWithPerformance(expected)
    filter.doFilter(request,response,chain)
    cut.getWorstApplicationPerformance should be >= (expected)

    expected = 60;
    chain = getFilterChainWithPerformance(expected)
    filter.doFilter(request,response,chain)
    cut.getWorstApplicationPerformance should be >= (expected)

    chain = getFilterChainWithPerformance(20)
    filter.doFilter(request,response,chain)
    cut.getWorstApplicationPerformance should be >= (expected)
  }
  
  @Test
  def getWorstXRayPerformance = {
    var expected:Long = 100
    filter = getFilterWithPerformance(expected)
    def chain = mock[FilterChain]
    def headers = new HashMap[String,String]()
    filter.getInstrumentedRunnable("hugo",headers).run()
    cut.getWorstXRayPerformance should be >= (expected)
    
  }

  def getFilterChainWithPerformance(delay:Long):FilterChain = {
    return new FilterChain(){
    override def doFilter(request:ServletRequest,response:ServletResponse){
      Thread.sleep(delay+50);
    }
    }
  }
  
  def getFilterWithPerformance(delay:Long):HTTPRequestRESTInterceptor = {
    return new HTTPRequestRESTInterceptor(){
      override def sendAsync(uri:String,headers:Map[String,String])={
        Thread.sleep(delay+100);
      }
      
      override def send(uri:String,headers:Map[String,String])={
        Thread.sleep(delay+100);
      }
    }
  }
  
  @Test
  def reset = {
    cut.reset
    cut.getXRayPerformance should be(0)
    cut.getApplicationPerformance should be(0)
    cut.getNrOfRejectedJobs() should be(0)
  }
}
