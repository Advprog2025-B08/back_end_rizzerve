package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    public CartController(CartService cartService, CartCommandInvoker commandInvoker) {

    }

    @PostMapping("/{userId}/items/{menuId}")
    public ResponseEntity<CartItem> addItemToCart(@PathVariable Long userId, @PathVariable Long menuId) {

    }

    @PutMapping("/{userId}/items/{menuId}")
    public ResponseEntity<?> updateCartItem(

    }

    @DeleteMapping("/{userId}/items/{menuId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long userId, @PathVariable Long menuId) {

    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {

    }
}