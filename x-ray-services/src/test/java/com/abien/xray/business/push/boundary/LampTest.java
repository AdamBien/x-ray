package com.abien.xray.business.push.boundary;

import com.abien.xray.business.push.control.Bulp;
import com.abien.xray.business.push.entity.Beam;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class LampTest {
    Lamp cut;
    
    @Before
    public void init(){
        this.cut = new Lamp();
        this.cut.bulp = new Bulp();
        this.cut.onInit();
    }

    @Test
    public void beamDelivery() {
        Beam beam = mock(Beam.class);
        this.cut.onNewBeam(beam);
        this.cut.onNewRequest("a");
        this.cut.onNewRequest("b");
        this.cut.pulse();
        verify(beam).on("a,b");
    }
}
