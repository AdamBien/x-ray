package com.abien.xray.business.monitoring.boundary

/**
 * User: blog.adam-bien.com
 * Date: 14.02.11
 * Time: 19:50
 */
import org.junit._
import Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.mockito.Mockito._
import com.abien.xray.business.monitoring.entity.Invocation

class MonitoringResourceTest extends JUnitSuite with ShouldMatchersForJUnit{

  var cut: MonitoringResource = _

  @Before
  def setUp: Unit = {
     cut = new MonitoringResource()
  }

  @Test
  def addAndGetSorted = {
    val slow = new Invocation("slow",2)
    val fast = new Invocation("fast",1)
    val veryFast = new Invocation("very fast",0)

    cut.add(veryFast);
    cut.add(slow);
    cut.add(fast);

    def methods = cut.getSlowestMethods(50)
    methods.size should be (3)

    var iterator = methods.iterator()
    def slowActual = iterator.next()
    def fastActual = iterator.next()
    def veryFastActual = iterator.next()

    slowActual should be (slow)
    fastActual should be (fast)
    veryFastActual should be (veryFast)
  }

  @Test
  def ignoreDuplicates = {
     val first = new Invocation("method",1)
     val second = new Invocation("method",2)
     cut.add(first)
     cut.add(second)

     def methods = cut.getSlowestMethods(50)
     methods.size should be (1)
  }

  @Test
  def addSlowestonly = {
     val first = new Invocation("method",100)
     val second = new Invocation("method",200)
     val third = new Invocation("method",1)
     cut.add(first)
     cut.add(second)
     cut.add(third)

     def methods = cut.getSlowestMethods(50)
     methods.size should be (1)
     def method = methods.get(0)
     method.getInvocationPerformance should be (200)
  }

  @Test
  def limitResult = {
     val first = new Invocation("method",1)
     val second = new Invocation("method2",2)
     cut.add(first)
     cut.add(second)

     def methods = cut.getSlowestMethods(1)
     methods.size should be (1)
  }

}