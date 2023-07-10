package com.assignment.ordermanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testFindByInstrumentIdAndType() {
        // Create test orders
        Order order1 = new Order();
        order1.setInstrumentId("AAPL");
        order1.setType(OrderType.BUY);
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setInstrumentId("GOOGL");
        order2.setType(OrderType.SELL);
        entityManager.persist(order2);

        Order order3 = new Order();
        order3.setInstrumentId("AAPL");
        order3.setType(OrderType.BUY);
        entityManager.persist(order3);

        // Flush the changes to the database
        entityManager.flush();

        // Invoke the repository method
        List<Order> result = orderRepository.findByInstrumentIdAndType("AAPL", OrderType.BUY);

        // Verify the results
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order3));
    }
}
