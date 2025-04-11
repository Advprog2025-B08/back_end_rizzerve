package id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemoRepository extends JpaRepository<Item, Long> {

    // Using JPQL to find items by name
    @Query("SELECT i FROM Item i WHERE i.name = ?1")
    List<Item> findItemsByName(String name);

    // Using JPQL to find items with price greater than a specified value
    @Query("SELECT i FROM Item i WHERE i.price > ?1")
    List<Item> findItemsWithPriceGreaterThan(double price);

    // Using JPQL to find the most expensive item
    @Query("SELECT i FROM Item i WHERE i.price = (SELECT MAX(i2.price) FROM Item i2)")
    Optional<Item> findMostExpensiveItem();

    // Using native SQL for more complex queries (e.g., finding items by price)
    @Query(value = "SELECT * FROM item WHERE price > ?1", nativeQuery = true)
    List<Item> findItemsWithPriceGreaterThanNative(double price);
}
