package com.abien.xray.business.store.control

import org.junit._
import Assert._
import org.scalatest.junit.ShouldMatchersForJUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar
import com.abien.xray.business.store.entity.Hit
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock

class PersistentHitStoreTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

  var cut:PersistentHitStore =_

  @Before
  def setUp: Unit = {
    cut = new PersistentHitStore();
  }

 
  @Test
  def persistCacheDeletesCache:Unit = {
    cut.em = mock[EntityManager]
    when(cut.em.merge(any(classOf[Hit]))).thenAnswer(new Answer[Object](){

      def answer(invocation:InvocationOnMock): Object = {
         return invocation.getArguments()(0)
     }
    })
    var map = new ConcurrentHashMap[String,AtomicLong]()
    val one:String = "1"
    val two:String = "2"
    val zero = new AtomicLong(0)
    
    map.put(one,new AtomicLong(1))
    map.put(two,new AtomicLong(2))

    cut.store(map)

    map.get(one).get should be (1)
    map.get(two).get should be (2)
  }
}
