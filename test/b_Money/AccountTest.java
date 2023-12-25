package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("abc", 1, 1, new Money(10000, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("abc"));
		
		testAccount.removeTimedPayment("abc");
		assertFalse(testAccount.timedPaymentExists("abc"));
		
		assertFalse(testAccount.timedPaymentExists("xyz"));
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.addTimedPayment("Hans", "qwe", 1, 1, new Money(10000, SEK), SweBank, "Alice");
		SweBank.addTimedPayment("Alice", "123", 1, 1, new Money(10000, SEK), SweBank, "Hans");
	}

	@Test
	public void testAddWithdraw() {
		testAccount.withdraw(new Money(10000000, SEK));
		assertEquals(0, testAccount.getBalance().getAmount(), 1);
		
		testAccount.withdraw(new Money(-100, SEK));
		assertEquals(100, testAccount.getBalance().getAmount(), 1);
		
		testAccount.withdraw(new Money(0, SEK));
		assertEquals(100, testAccount.getBalance().getAmount(), 1);
	}
	
	@Test
	public void testGetBalance() {
		assertEquals("10000000 SEK", testAccount.getBalance().toString());
	}
}
