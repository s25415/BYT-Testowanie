package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(0.15, SEK.getRate(), 0.01);
		assertEquals(0.20, DKK.getRate(), 0.01);
		assertEquals(1.5, EUR.getRate(), 0.01);
	}
	
	@Test
	public void testSetRate() {
		SEK.setRate(15.0);
		assertEquals(15.0, SEK.getRate(), 0.001);
		SEK.setRate(0.15);
		
		DKK.setRate(0.123);
		assertEquals(0.123, DKK.getRate(), 0.001);
		DKK.setRate(0.20);
		
		EUR.setRate(1.5);
		assertEquals(1.5, EUR.getRate(), 0.001);
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals(150000.0, SEK.universalValue(1000000), 0.001);
		assertEquals(90.0, EUR.universalValue(60), 0.001);
		assertEquals(1.0, DKK.universalValue(9), 0.001);
		assertEquals(0, EUR.universalValue(0), 0.001);
		assertEquals(-15, SEK.universalValue(-100), 0.001);
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(10, SEK.valueInThisCurrency(1, EUR), 1);
		assertEquals(1, EUR.valueInThisCurrency(10, SEK), 1);
		assertEquals(1, DKK.valueInThisCurrency(1, DKK), 1);
		assertEquals(0, EUR.valueInThisCurrency(1, DKK), 1);
		assertEquals(-3, DKK.valueInThisCurrency(-4, SEK), 1);
	}

}
