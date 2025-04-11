package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, MenuRepository menuRepository) {

    }

    @Override
    public Cart getOrCreateCart(Long userId) {

    }

    @Override
    public CartItem addItemToCart(Long userId, Long menuId) {

    }

    @Override
    public CartItem updateCartItemQuantity(Long userId, Long menuId, int quantityChange) {

    }

    @Override
    public void removeItemFromCart(Long userId, Long menuId) {

    }

    @Override
    public List<CartItem> getCartItems(Long userId) {

    }

    @Override
    public void clearCart(Long userId) {

    }
}