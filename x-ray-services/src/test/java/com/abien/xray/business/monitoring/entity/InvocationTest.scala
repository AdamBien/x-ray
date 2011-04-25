package com.abien.xray.business.monitoring.entity

import org.junit.Test
import org.scalatest.junit.{JUnitSuite, ShouldMatchersForJUnit}

/**
 * User: blog.adam-bien.com
 * Date: 19.02.11
 * Time: 22:27
 */
class InvocationTest extends JUnitSuite with ShouldMatchersForJUnit{

  @Test
  def isSlowerThan = {
    def fast = new Invocation("m",1)
    def slow = new Invocation("m",2)
    slow.isSlowerThan(fast) should be (true)
  }
}