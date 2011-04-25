package com.abien.xray.roller

import java.util.HashMap
import org.junit._
import Assert._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit


class XRayModelTest extends JUnitSuite with MockitoSugar with ShouldMatchersForJUnit{

    var cut:XRayModel = _

    @Before
    def setUp: Unit = {
      cut = new XRayModel();
    }


    @Test
    def initWithNullMap = {
      cut.init(null)
      def xray = cut.getXray
      xray should not be (null)
      val baseUrl = xray.getBaseUrl
      baseUrl should be (XRayModel.URL)
    }

    @Test
    def initWithMap = {
      def map = new HashMap()
      cut.init(map)
      def xray = cut.getXray
      xray should not be (null)
      val baseUrl = xray.getBaseUrl
      baseUrl should be (XRayModel.URL)
    }

    @Test
    def initWithConfiguredMap = {
      var map = new HashMap[String,String]()
      def customURL = "custom url"
      map.put(XRayModel.XRAYURL,customURL)
      cut.init(map)
      def xray = cut.getXray
      xray should not be (null)
      val baseUrl = xray.getBaseUrl
      baseUrl should be (customURL)
    }

    @Test
    def extractUrl = {
      var map = new HashMap[String,String]()
      def customURL = "custom url"
      map.put(XRayModel.XRAYURL,customURL)
      val url = this.cut.extractUrl(map);
      url should be (customURL)
    }

    @Test
    def modelName = {
      val modelName = cut.getModelName
      modelName should be (XRayModel.XRAYMODEL_NAME)
    }
}
