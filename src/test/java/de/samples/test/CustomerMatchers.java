package de.samples.test;

import de.samples.domain.Customer;
import de.samples.domain.CustomerState;
import org.mockito.ArgumentMatcher;

// @UtilityClass??
public final class CustomerMatchers {

  private CustomerMatchers() {
  }

  /**
   * Argument matcher that checks the state of the customer.
   *
   * @param state the expeted state
   * @return the argument matcher
   */
  public static ArgumentMatcher<Customer> hasState(CustomerState state) {
    return customer -> customer.getState() == state;
  }

}
