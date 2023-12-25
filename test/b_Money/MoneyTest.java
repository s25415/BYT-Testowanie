package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals("0", 0, (int)SEK0.getAmount());
		assertEquals("100", 10000, (int)SEK100.getAmount());
		assertEquals("10", 1000, (int)EUR10.getAmount());
		assertEquals("-10000", -10000, (int)SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals("SEK", SEK, SEK100.getCurrency());
		assertEquals("EUR", EUR, EUR10.getCurrency());
		assertEquals("SEK", SEK, SEK0.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("0 SEK", "0 SEK", SEK0.toString());
		assertEquals("0 EUR", "0 EUR", EUR0.toString());
		assertEquals("10000 SEK", "10000 SEK", SEK100.toString());
		assertEquals("1000 EUR", "1000 EUR", EUR10.toString());
		assertEquals("-10000 SEK", "-10000 SEK", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals("SEK: 0.15", 0.15, SEK.getRate(), 0.001);
		assertEquals("DKK: 0.20", 0.20, DKK.getRate(), 0.001);
		assertEquals("EUR: 1.5", 1.5, EUR.getRate(), 0.001);
		assertEquals("10000 SEK: 0.15", 0.15, SEK100.getCurrency().getRate(), 0.001);
		assertEquals("0 EUR: 1.5", 1.5, EUR0.getCurrency().getRate(), 0.001);
	}

	@Test
	public void testEqualsMoney() {
		assertTrue("10000 SEK = 10000 SEK", SEK100.equals(SEK100));
		assertTrue("10000 SEK = 1000 EUR", SEK100.equals(EUR10));
		assertFalse("0 EUR =/= 1000 EUR",EUR0.equals(EUR10));
		assertFalse("10000 SEK =/= -10000 SEK", SEK100.equals(SEKn100));
	}

	@Test
	public void testAdd() {
		assertEquals("10000 SEK + 20000 SEK", "30000 SEK", SEK100.add(SEK200).toString());
		assertEquals("1000 EUR + 10000 SEK", "2000 EUR", EUR10.add(SEK100).toString());
		assertEquals("10000 SEK + 1000 EUR", "20000 SEK", SEK100.add(EUR10).toString());
		assertEquals("10000 SEK - 10000 SEK", "0 SEK", SEK100.add(SEKn100).toString());
	}

	@Test
	public void testSub() {
		assertEquals("20000 SEK - 10000 SEK", "10000 SEK", SEK200.sub(SEK100).toString());
		assertEquals("1000 EUR - 10000 SEK", "0 EUR", EUR10.sub(SEK100).toString());
		assertEquals("10000 SEK - 1000 EUR", "0 SEK", SEK100.sub(EUR10).toString());
		assertEquals("10000 SEK + 10000 SEK", "20000 SEK", SEK100.sub(SEKn100).toString());
	}

	@Test
	public void testIsZero() {
		assertTrue("0 EUR", EUR0.isZero());
		assertFalse("1000 EUR", EUR10.isZero());
		assertFalse("-10000 SEK", SEKn100.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals("1000 EUR", -1000, (int)EUR10.negate().getAmount());
		assertEquals("-10000 SEK", 10000, (int)SEKn100.negate().getAmount());
		assertEquals("0 EUR", 0, (int)EUR0.negate().getAmount());
	}

	@Test
	public void testCompareTo() {
		assertEquals("1000 EUR", -1000, (int)EUR10.negate().getAmount());
		assertEquals("-10000 SEK", 10000, (int)SEKn100.negate().getAmount());
		assertEquals("0 EUR", 0, (int)EUR0.negate().getAmount());
	}
}
