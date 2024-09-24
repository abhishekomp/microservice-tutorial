const BOOKSTORE_STATE_KEY = "BOOKSTORE_STATE";

const getCart = function(){
    let cart = localStorage.getItem(BOOKSTORE_STATE_KEY);
    //console.log(typeof cart);
    if(!cart){
        //console.log("Creating a new cart");
        //cart = JSON.stringify({items:[], totalAmount: 0});
        cart = JSON.stringify({items:[], totalAmount:0 });
        //console.log("getCart:", cart);
        localStorage.setItem(BOOKSTORE_STATE_KEY, cart);
    }
    return JSON.parse(cart);
}

const addProductToCart = function(product){
    let cart = getCart();
    //console.log("TypeOf cart in addProductToCart:", typeof cart);
    let cartItem = cart.items.find(itemModel => itemModel.code === product.code)
    if(cartItem){
        cartItem.quantity = parseInt(cartItem.quantity) + 1;
    } else {
        cart.items.push(Object.assign({}, product, {quantity: 1}));
    }
    //console.log("TypeOf cart in addProductToCart before setting to localStorage:", typeof cart);
    localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart));
    updateCartItemCount();
}

function updateCartItemCount() {
    let cart = getCart();
    let count = 0;
    cart.items.forEach(item => {
        count = count + item.quantity;
    });
    $('#cart-item-count').text('(' + count + ')');
}

const deleteCart = function() {
    localStorage.removeItem(BOOKSTORE_STATE_KEY)
    updateCartItemCount();
}

function getCartTotal() {
    let cart = getCart();
    let totalAmount = 0;
    cart.items.forEach(item => {
        totalAmount = totalAmount + (item.price * item.quantity);
    });
    return totalAmount;
}