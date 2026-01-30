import React, { useState, useEffect } from 'react';
import { ShoppingCart, Package, Trash2, Plus, Minus, CheckCircle, XCircle } from 'lucide-react';

const API_BASE = 'http://localhost/api';
const API_PRODUCT = `${API_BASE}/products`;
const API_ORDER = `${API_BASE}/orders`;


export default function ECommerceApp() {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [cart, setCart] = useState([]);
  const [activeTab, setActiveTab] = useState('products');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);

  useEffect(() => {
    fetchProducts();
    fetchOrders();
  }, []);

  // All orders as fetched from backend
  const allOrders = orders;

  // Active orders (not cancelled)
  const activeOrders = orders.filter(order => order.status !== 'CANCELLED');

  // Cancelled orders
  const cancelledOrders = orders.filter(order => order.status === 'CANCELLED');



  const fetchProducts = async () => {
    try {
      const res = await fetch(API_PRODUCT);
      const data = await res.json();
      setProducts(data);
    } catch (err) {
      showMessage('Failed to fetch products', 'error');
    }
  };

  const fetchOrders = async () => {
  try {
    const res = await fetch(API_ORDER);
    const data = await res.json();

    // Enrich order items with product names
    const enrichedOrders = data.map(order => ({
      ...order,
      items: order.items.map(item => {
        const product = products.find(p => p.productId === item.productId);
        return {
          ...item,
          name: product ? product.name : `Product ${item.productId}`
        };
      })
    }));

    setOrders(enrichedOrders);
  } catch (err) {
    showMessage('Failed to fetch orders', 'error');
  }
};


  const showMessage = (text, type = 'success') => {
    setMessage({ text, type });
    setTimeout(() => setMessage(null), 3000);
  };

  const addToCart = (product) => {
    const existing = cart.find(item => item.productId === product.productId);
    if (existing) {
      setCart(cart.map(item =>
        item.productId === product.productId
          ? { ...item, quantity: item.quantity + 1 }
          : item
      ));
    } else {
      setCart([...cart, { ...product, quantity: 1 }]);
    }
    showMessage(`Added ${product.name} to cart`);
  };

  const updateCartQuantity = (productId, delta) => {
    setCart(cart.map(item =>
      item.productId === productId
        ? { ...item, quantity: Math.max(1, item.quantity + delta) }
        : item
    ).filter(item => item.quantity > 0));
  };

  const removeFromCart = (productId) => {
    setCart(cart.filter(item => item.productId !== productId));
    showMessage('Removed from cart');
  };

  const placeOrder = async () => {
    if (cart.length === 0) {
      showMessage('Cart is empty', 'error');
      return;
    }

    setLoading(true);
    try {
      const orderData = {
        userId: Math.floor(Math.random() * 100) + 1,
        items: cart.map(item => ({
          productId: item.productId,
          quantity: item.quantity
        }))
      };

      const res = await fetch(API_ORDER, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData)
      });

      if (res.ok) {
        showMessage('Order placed successfully!');
        setCart([]);
        fetchOrders();
        fetchProducts();
        setActiveTab('orders');
      } else {
        showMessage('Failed to place order', 'error');
      }
    } catch (err) {
      showMessage('Error placing order', 'error');
    } finally {
      setLoading(false);
    }
  };

  const cancelOrder = async (orderId) => {
    try {
      const res = await fetch(`${API_ORDER}/${orderId}/cancel`, {
        method: 'POST'
      });

      if (res.ok) {
        showMessage('Order cancelled');
        fetchOrders();
        fetchProducts();
      } else {
        showMessage('Failed to cancel order', 'error');
      }
    } catch (err) {
      showMessage('Error cancelling order', 'error');
    }
  };

  const cartTotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {message && (
        <div className={`fixed top-4 right-4 px-6 py-3 rounded-lg shadow-lg z-50 flex items-center gap-2 ${
          message.type === 'error' ? 'bg-red-500' : 'bg-green-500'
        } text-white`}>
          {message.type === 'error' ? <XCircle size={20} /> : <CheckCircle size={20} />}
          {message.text}
        </div>
      )}

      <div className="container mx-auto px-4 py-8">
        <header className="mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">MAMA Ltd Store</h1>
          <p className="text-gray-600">Your one-stop online shop</p>
        </header>

        <div className="flex gap-4 mb-6">
          <button
            onClick={() => setActiveTab('products')}
            className={`px-6 py-3 rounded-lg font-semibold transition flex items-center gap-2 ${
              activeTab === 'products'
                ? 'bg-indigo-600 text-white shadow-lg'
                : 'bg-white text-gray-700 hover:bg-gray-50'
            }`}
          >
            <Package size={20} />
            Products
          </button>

          <button
            onClick={() => setActiveTab('orders')}
            className={`px-6 py-3 rounded-lg font-semibold transition flex items-center gap-2 ${
              activeTab === 'orders'
                ? 'bg-indigo-600 text-white shadow-lg'
                : 'bg-white text-gray-700 hover:bg-gray-50'
            }`}
          >
            <ShoppingCart size={20} />
            Orders ({orders.length})
          </button>

          <button
            onClick={() => setActiveTab('history')}
            className={`px-6 py-3 rounded-lg font-semibold transition flex items-center gap-2 ${
              activeTab === 'history'
                ? 'bg-indigo-600 text-white shadow-lg'
                : 'bg-white text-gray-700 hover:bg-gray-50'
            }`}
          >
            <Package size={20} />
            History ({orders.length})
          </button>


          <button
            onClick={() => setActiveTab('cart')}
            className={`px-6 py-3 rounded-lg font-semibold transition flex items-center gap-2 relative ${
              activeTab === 'cart'
                ? 'bg-indigo-600 text-white shadow-lg'
                : 'bg-white text-gray-700 hover:bg-gray-50'
            }`}
          >
            <ShoppingCart size={20} />
            Cart
            {cart.length > 0 && (
              <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-6 h-6 flex items-center justify-center">
                {cart.length}
              </span>
            )}
          </button>
        </div>

        {activeTab === 'products' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {products.map(product => (
              <div key={product.productId} className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition">
                <div className="p-6">
                  <div className="flex justify-between items-start mb-4">
                    <h3 className="text-xl font-bold text-gray-800">{product.name}</h3>
                    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                      product.stockQuantity > 10 ? 'bg-green-100 text-green-800' : 'bg-orange-100 text-orange-800'
                    }`}>
                      {product.stockQuantity} in stock
                    </span>
                  </div>
                  <p className="text-gray-600 mb-4">{product.description}</p>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-gray-500">{product.category}</p>
                      <p className="text-2xl font-bold text-indigo-600">£{product.price.toFixed(2)}</p>
                    </div>
                    <button
                      onClick={() => addToCart(product)}
                      disabled={product.stockQuantity === 0}
                      className="bg-indigo-600 text-white px-6 py-2 rounded-lg hover:bg-indigo-700 transition disabled:bg-gray-300 disabled:cursor-not-allowed flex items-center gap-2"
                    >
                      <ShoppingCart size={18} />
                      Add to Cart
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {activeTab === 'cart' && (
          <div className="bg-white rounded-xl shadow-md p-6">
            {cart.length === 0 ? (
              <div className="text-center py-12">
                <ShoppingCart size={64} className="mx-auto text-gray-300 mb-4" />
                <p className="text-gray-500 text-lg">Your cart is empty</p>
              </div>
            ) : (
              <>
                <div className="space-y-4 mb-6">
                  {cart.map(item => (
                    <div key={item.productId} className="flex items-center gap-4 p-4 border rounded-lg">
                      <div className="flex-1">
                        <h4 className="font-semibold text-lg">{item.name}</h4>
                        <p className="text-gray-600">£{item.price.toFixed(2)} each</p>
                      </div>
                      <div className="flex items-center gap-3">
                        <button
                          onClick={() => updateCartQuantity(item.productId, -1)}
                          className="bg-gray-200 p-2 rounded-lg hover:bg-gray-300"
                        >
                          <Minus size={16} />
                        </button>
                        <span className="font-semibold text-lg w-8 text-center">{item.quantity}</span>
                        <button
                          onClick={() => updateCartQuantity(item.productId, 1)}
                          className="bg-gray-200 p-2 rounded-lg hover:bg-gray-300"
                        >
                          <Plus size={16} />
                        </button>
                      </div>
                      <p className="font-bold text-lg w-24 text-right">
                        £{(item.price * item.quantity).toFixed(2)}
                      </p>
                      <button
                        onClick={() => removeFromCart(item.productId)}
                        className="text-red-500 hover:text-red-700"
                      >
                        <Trash2 size={20} />
                      </button>
                    </div>
                  ))}
                </div>
                <div className="border-t pt-6">
                  <div className="flex justify-between items-center mb-6">
                    <span className="text-2xl font-bold">Total:</span>
                    <span className="text-3xl font-bold text-indigo-600">£{cartTotal.toFixed(2)}</span>
                  </div>
                  <button
                    onClick={placeOrder}
                    disabled={loading}
                    className="w-full bg-green-600 text-white py-4 rounded-lg text-lg font-semibold hover:bg-green-700 transition disabled:bg-gray-400"
                  >
                    {loading ? 'Placing Order...' : 'Place Order'}
                  </button>
                </div>
              </>
            )}
          </div>
        )}

        {activeTab === 'history' && (
          <div className="space-y-4">
            {allOrders.length === 0 ? (
              <div className="bg-white rounded-xl shadow-md p-12 text-center">
                <Package size={64} className="mx-auto text-gray-300 mb-4" />
                <p className="text-gray-500 text-lg">No orders yet</p>
              </div>
            ) : (
              allOrders.map(order => (
                <div key={order.orderId} className="bg-white rounded-xl shadow-md p-6">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <h3 className="text-xl font-bold">Order #{order.orderId}</h3>
                      <p className="text-gray-600">{new Date(order.orderDate).toLocaleString()}</p>
                    </div>
                    <div className="text-right">
                      <span className={`px-4 py-2 rounded-full font-semibold ${
                        order.status === 'NEW' ? 'bg-blue-100 text-blue-800' :
                        order.status === 'PROCESSING' ? 'bg-yellow-100 text-yellow-800' :
                        order.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {order.status}
                      </span>
                    </div>
                  </div>

                  {/* Order items */}
                  <div className="border-t pt-4 mb-4">
                    {order.items.map(item => {
                      const product = products.find(p => p.productId === item.productId);
                      const name = product ? product.name : `Product ${item.productId}`;
                      return (
                        <div key={item.orderItemId} className="flex justify-between py-2">
                          <span>{name} × {item.quantity}</span>
                          <span className="font-semibold">£{(item.unitPrice * item.quantity).toFixed(2)}</span>
                        </div>
                      );
                    })}
                  </div>

                  {/* Total and cancel button */}
                  <div className="flex justify-between items-center border-t pt-4">
                    <span className="text-xl font-bold">Total: £{order.totalPrice.toFixed(2)}</span>
                    {order.status === 'NEW' && (
                      <button
                        onClick={() => cancelOrder(order.orderId)}
                        className="bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition"
                      >
                        Cancel Order
                      </button>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        )}

        {activeTab === 'orders' && (
          <div className="space-y-4">
            {activeOrders.length === 0 ? (
              <div className="bg-white rounded-xl shadow-md p-12 text-center">
                <Package size={64} className="mx-auto text-gray-300 mb-4" />
                <p className="text-gray-500 text-lg">No active orders</p>
              </div>
            ) : (
              activeOrders.map(order => (
                <div key={order.orderId} className="bg-white rounded-xl shadow-md p-6">
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <h3 className="text-xl font-bold">Order #{order.orderId}</h3>
                      <p className="text-gray-600">{new Date(order.orderDate).toLocaleString()}</p>
                    </div>
                    <div className="text-right">
                      <span className={`px-4 py-2 rounded-full font-semibold ${
                        order.status === 'NEW' ? 'bg-blue-100 text-blue-800' :
                        order.status === 'PROCESSING' ? 'bg-yellow-100 text-yellow-800' :
                        order.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {order.status}
                      </span>
                    </div>
                  </div>

                  <div className="border-t pt-4 mb-4">
                    {order.items.map(item => {
                      const product = products.find(p => p.productId === item.productId);
                      const name = product ? product.name : `Product ${item.productId}`;
                      return (
                        <div key={item.orderItemId} className="flex justify-between py-2">
                          <span>{name} × {item.quantity}</span>
                          <span className="font-semibold">£{(item.unitPrice * item.quantity).toFixed(2)}</span>
                        </div>
                      );
                    })}
                  </div>

                  <div className="flex justify-between items-center border-t pt-4">
                    <span className="text-xl font-bold">Total: £{order.totalPrice.toFixed(2)}</span>
                    {order.status === 'NEW' && (
                      <button
                        onClick={() => cancelOrder(order.orderId)}
                        className="bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition"
                      >
                        Cancel Order
                      </button>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        )}

      </div>
    </div>
  );
}

