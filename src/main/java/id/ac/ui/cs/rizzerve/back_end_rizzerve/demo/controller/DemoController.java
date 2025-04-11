package id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.model.Item;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class DemoController {

    private final DemoService DemoService;

    @Autowired
    public DemoController(DemoService DemoService) {
        this.DemoService = DemoService;
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = DemoService.createItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Item> getAllItems() {
        return DemoService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = DemoService.getItemById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/name/{name}")
    public List<Item> getItemsByName(@PathVariable String name) {
        return DemoService.findItemsByName(name);
    }

    @GetMapping("/price/greater-than/{price}")
    public List<Item> getItemsWithPriceGreaterThan(@PathVariable double price) {
        return DemoService.findItemsWithPriceGreaterThan(price);
    }

    @GetMapping("/most-expensive")
    public ResponseEntity<Item> getMostExpensiveItem() {
        Optional<Item> item = DemoService.findMostExpensiveItem();
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/price/greater-than-native/{price}")
    public List<Item> getItemsWithPriceGreaterThanNative(@PathVariable double price) {
        return DemoService.findItemsWithPriceGreaterThanNative(price);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        DemoService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
