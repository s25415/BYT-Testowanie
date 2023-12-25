package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals("SEK", SweBank.getCurrency().getName());
		assertEquals("SEK", Nordea.getCurrency().getName());
		assertEquals("DKK", DanskeBank.getCurrency().getName());
	}

	@Test
	public void testPOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		// nie można było wpłacać pieniędzy na nowo otwarte konta
		SweBank.openAccount("Adam");
		Nordea.openAccount("Ewa");
		SweBank.deposit("Adam", new Money(1000, SEK));
		Nordea.deposit("Ewa", new Money(1000, SEK));
	}
	
	@Test(expected=AccountExistsException.class)
	public void testNOpenAccount() throws AccountExistsException {
		SweBank.openAccount("Adam");
		DanskeBank.openAccount("Gertrud");
	}

	
	@Test
	public void testPDeposit() throws AccountDoesNotExistException {
		// metoda deposit rzucała błąd AccountDoesNotExistException
		int balance;
		
		balance = SweBank.getBalance("Ulrika");
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		assertEquals(balance+1000, SweBank.getBalance("Ulrika"), 1);
		
		balance = DanskeBank.getBalance("Gertrud");
		DanskeBank.deposit("Gertrud", new Money(-100, DKK));
		assertEquals(balance-100, DanskeBank.getBalance("Gertrud"), 1);
		
		balance = Nordea.getBalance("Bob");
		Nordea.deposit("Bob", new Money(0, DKK));
		assertEquals(balance, Nordea.getBalance("Bob"), 1);
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testNDeposit() throws AccountDoesNotExistException {
		// metoda deposit nie rzucała błędu AccountDoesNotExistException
		Nordea.deposit("Gertrud", new Money(0, SEK));
		DanskeBank.deposit("Alfred", new Money(2000, DKK));
	}

	
	@Test
	public void testPWithdraw() throws AccountDoesNotExistException {
		// pieniądze były dodawane zamiast być odejmowane
		int balance;
		
		balance = SweBank.getBalance("Ulrika");
		SweBank.withdraw("Ulrika", new Money(1000, SEK));
		assertEquals(balance-1000, SweBank.getBalance("Ulrika"), 1);
		
		balance = DanskeBank.getBalance("Gertrud");
		DanskeBank.withdraw("Gertrud", new Money(-100, DKK));
		assertEquals(balance+100, DanskeBank.getBalance("Gertrud"), 1);
		
		balance = Nordea.getBalance("Bob");
		Nordea.withdraw("Bob", new Money(0, DKK));
		assertEquals(balance, Nordea.getBalance("Bob"), 1);
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testNWithdraw() throws AccountDoesNotExistException {
		Nordea.withdraw("Gertrud", new Money(0, SEK));
		DanskeBank.withdraw("Alfred", new Money(2000, DKK));
	}
	
	
	@Test
	public void testPGetBalance() throws AccountDoesNotExistException {
		assertEquals(0, SweBank.getBalance("Bob"), 1);
		
		assertNotNull(SweBank.getBalance("Ulrika"));
		assertNotNull(DanskeBank.getBalance("Gertrud"));
		assertNotNull(Nordea.getBalance("Bob"));
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testNGetBalance() throws AccountDoesNotExistException {
		Nordea.getBalance("Gertrud");
		DanskeBank.getBalance("Alfred");
	}
	
	
	@Test
	public void testPTransfer() throws AccountDoesNotExistException {
		int balance1, balance2;
		
		balance1 = SweBank.getBalance("Bob");
		balance2 = SweBank.getBalance("Ulrika");
		SweBank.transfer("Bob", "Ulrika", new Money(2000, SEK));
		assertEquals(balance1-2000, SweBank.getBalance("Bob"), 1);
		assertEquals(balance2+2000, SweBank.getBalance("Ulrika"), 1);
		
		balance1 = SweBank.getBalance("Ulrika");
		balance2 = DanskeBank.getBalance("Gertrud");
		SweBank.transfer("Ulrika", DanskeBank, "Gertrud", new Money(-200, DKK));
		assertEquals(balance1+267, SweBank.getBalance("Ulrika"), 1);
		assertEquals(balance2-200, DanskeBank.getBalance("Gertrud"), 1);
		
		balance1 = SweBank.getBalance("Ulrika");
		balance2 = SweBank.getBalance("Bob");
		SweBank.transfer("Ulrika", SweBank, "Bob", new Money(0, SEK));
		assertEquals(balance1, SweBank.getBalance("Ulrika"), 1);
		assertEquals(balance2, SweBank.getBalance("Bob"), 1);
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testNTransfer() throws AccountDoesNotExistException {
		SweBank.transfer("Hans", "Bjorn", new Money(10000, SEK));
		SweBank.transfer("Hans", SweBank, "Bjorn", new Money(-10000, SEK));
		DanskeBank.transfer("Gertrud", SweBank, "Gertrud", new Money(0, SEK));
		SweBank.transfer("Gertrud", DanskeBank, "Gertrud", new Money(350, DKK));
	}
	
	
	@Test
	public void testPTimedPayment() throws AccountDoesNotExistException {
		SweBank.addTimedPayment("Bob", "abc", 1, 1, new Money(10000, SEK), SweBank, "Ulrika");
		SweBank.addTimedPayment("Ulrika", "xyz", 100, 0, new Money(0, SEK), DanskeBank, "Gertrud");
		DanskeBank.addTimedPayment("Gertrud", "zyx", 4, -1, new Money(-299, SEK), SweBank, "Bob");
		
		SweBank.removeTimedPayment("Ulrika","xyz");
		DanskeBank.removeTimedPayment("Gertrud","zyx");
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testNTimedPayment() throws AccountDoesNotExistException {
		// metoda addTimedPayment nie rzuca AccountDoesNotExistException 
		SweBank.addTimedPayment("Hans", "abc", 1, 1, new Money(10000, SEK), SweBank, "Bjorn");
		SweBank.addTimedPayment("Gertrud", "xyz", 1, 1, new Money(0, SEK), DanskeBank, "Gertrud");
		DanskeBank.addTimedPayment("Gertrud", "zyx", 1, 1, new Money(-299, SEK), SweBank, "Gertrud");
		
		SweBank.removeTimedPayment("Hans","abc");
		Nordea.removeTimedPayment("Ulrika","123");
	}
}
