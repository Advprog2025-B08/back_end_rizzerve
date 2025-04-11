package id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.model.Item;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemoService {

    private final DemoRepository DemoRepository;

    @Autowired
    public DemoService(DemoRepository DemoRepository) {
        this.DemoRepository = DemoRepository;
    }

    public Item createItem(Item item) {
        return DemoRepository.save(item);
    }

    public List<Item> getAllItems() {
        return DemoRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return DemoRepository.findById(id);
    }

    public void deleteItem(Long id) {
        DemoRepository.deleteById(id);
    }

    // Custom Query Usage
    public List<Item> findItemsByName(String name) {
        return DemoRepository.findItemsByName(name);
    }

    public List<Item> findItemsWithPriceGreaterThan(double price) {
        return DemoRepository.findItemsWithPriceGreaterThan(price);
    }

    public Optional<Item> findMostExpensiveItem() {
        return DemoRepository.findMostExpensiveItem();
    }

    public List<Item> findItemsWithPriceGreaterThanNative(double price) {
        return DemoRepository.findItemsWithPriceGreaterThanNative(price);
    }
}
