async function loadCart() {
    const user = requireRole("CONSUMER");
    if (!user) {
        return;
    }

    await loadCartItems(user.id);
    await loadMyOrders(user.id);
}

async function loadCartItems(userId) {
    const message = document.getElementById("message");

    const oldQuantities = {};
    document.querySelectorAll("input[id^='qty-cart-']").forEach(input => {
        oldQuantities[input.id] = input.value;
    });

    try {
        const response = await fetch(API_URL + "/api/cart/" + userId);

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        const cartItems = await response.json();

        const body = document.getElementById("cartBody");
        body.innerHTML = "";

        let total = 0;

        cartItems.forEach(item => {
            const inputId = "qty-cart-" + item.id;
            const quantityValue = oldQuantities[inputId] || item.quantity;

            const itemTotal = item.product.price * quantityValue;
            total += itemTotal;

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${item.product.name}</td>
                <td>${item.product.category}</td>
                <td>${item.product.price}</td>
                <td><input id="${inputId}" type="number" min="1" value="${quantityValue}"></td>
                <td>${itemTotal.toFixed(2)}</td>
                <td>
                    <button onclick="updateCartItem(${item.id})">Update</button>
                    <button class="secondary" onclick="removeCartItem(${item.id})">Remove</button>
                </td>
            `;

            body.appendChild(row);
        });

        document.getElementById("cartTotal").textContent = "Total: " + total.toFixed(2);
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}

async function updateCartItem(cartItemId) {
    const quantity = Number(document.getElementById("qty-cart-" + cartItemId).value);
    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/cart/" + cartItemId, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                quantity: quantity
            })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Cart updated successfully.";

        await loadCart();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}

async function removeCartItem(cartItemId) {
    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/cart/" + cartItemId, {
            method: "DELETE"
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Product removed from cart.";

        await loadCart();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}

async function placeOrder() {
    const user = requireRole("CONSUMER");
    if (!user) {
        return;
    }

    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/orders/place", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: user.id
            })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Order placed successfully.";

        await loadCart();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}

async function loadMyOrders(userId) {
    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/orders/consumer/" + userId);

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error("Could not load orders. " + errorText);
        }

        const orders = await response.json();

        const ordersDiv = document.getElementById("orders");
        ordersDiv.innerHTML = "";

        if (orders.length === 0) {
            ordersDiv.innerHTML = "<p>No orders placed yet.</p>";
            return;
        }

        orders.forEach(order => {
            const div = document.createElement("div");
            div.className = "order-card";

            let itemsHtml = "<ul>";

            order.items.forEach(item => {
                itemsHtml += `
                    <li>
                        ${item.product.name} -
                        quantity: ${item.quantity} -
                        price: ${item.priceAtOrder}
                    </li>
                `;
            });

            itemsHtml += "</ul>";

            div.innerHTML = `
                <h3>Order #${order.id}</h3>
                <p>Status: ${order.status}</p>
                <p>Date: ${order.createdAt}</p>
                ${itemsHtml}
            `;

            ordersDiv.appendChild(div);
        });
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}