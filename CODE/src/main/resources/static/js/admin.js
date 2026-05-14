async function loadAdminProducts() {
    const user = requireRole("ADMIN");
    if (!user) {
        return;
    }

    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/products");

        if (!response.ok) {
            throw new Error("Could not load products.");
        }

        const products = await response.json();

        const body = document.getElementById("productsBody");
        body.innerHTML = "";

        products.forEach(product => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${product.name}</td>
                <td>${product.category}</td>
                <td>${product.details}</td>
                <td>${product.price}</td>
                <td>${product.stock}</td>
                <td>
                    <button onclick='editProduct(${JSON.stringify(product)})'>Edit</button>
                    <button class="secondary" onclick="deleteProduct(${product.id})">Delete</button>
                </td>
            `;

            body.appendChild(row);
        });
    } catch (error) {
        if (message) {
            message.style.color = "#b00020";
            message.textContent = error.message;
        }
    }
}

function editProduct(product) {
    document.getElementById("formTitle").textContent = "Update Product";
    document.getElementById("productId").value = product.id;
    document.getElementById("name").value = product.name;
    document.getElementById("category").value = product.category;
    document.getElementById("details").value = product.details;
    document.getElementById("price").value = product.price;
    document.getElementById("stock").value = product.stock;
}

function clearForm() {
    document.getElementById("formTitle").textContent = "Add Product";
    document.getElementById("productId").value = "";
    document.getElementById("name").value = "";
    document.getElementById("category").value = "";
    document.getElementById("details").value = "";
    document.getElementById("price").value = "";
    document.getElementById("stock").value = "";
}

async function saveProduct() {
    const user = requireRole("ADMIN");
    if (!user) {
        return;
    }

    const productId = document.getElementById("productId").value;
    const message = document.getElementById("message");

    const product = {
        name: document.getElementById("name").value,
        category: document.getElementById("category").value,
        details: document.getElementById("details").value,
        price: Number(document.getElementById("price").value),
        stock: Number(document.getElementById("stock").value)
    };

    try {
        let url = API_URL + "/api/products";
        let method = "POST";

        if (productId) {
            url = API_URL + "/api/products/" + productId;
            method = "PUT";
        }

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(product)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Product saved successfully.";

        clearForm();
        await loadAdminProducts();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}

async function deleteProduct(productId) {
    const user = requireRole("ADMIN");
    if (!user) {
        return;
    }

    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/products/" + productId, {
            method: "DELETE"
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Product deleted successfully.";

        await loadAdminProducts();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}

async function loadAdminOrders() {
    const user = requireRole("ADMIN");
    if (!user) {
        return;
    }

    const message = document.getElementById("message");
    const ordersDiv = document.getElementById("orders");
    const notification = document.getElementById("notification");

    try {
        const response = await fetch(API_URL + "/api/orders/admin");

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error("Could not load orders. " + errorText);
        }

        const orders = await response.json();

        ordersDiv.innerHTML = "";

        const newOrders = orders.filter(order => order.status === "NEW");

        notification.textContent =
            "New order notifications: " + newOrders.length + " new order(s).";

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

            let confirmButton = "";

            if (order.status === "NEW") {
                confirmButton = `<button onclick="confirmOrder(${order.id})">Confirm Order</button>`;
            }

            div.innerHTML = `
                <h3>Order #${order.id}</h3>
                <p>Consumer: ${order.consumer.fullName} (${order.consumer.username})</p>
                <p>Status: ${order.status}</p>
                <p>Date: ${order.createdAt}</p>
                ${itemsHtml}
                ${confirmButton}
            `;

            ordersDiv.appendChild(div);
        });
    } catch (error) {
        if (message) {
            message.style.color = "#b00020";
            message.textContent = error.message;
        }
    }
}

async function confirmOrder(orderId) {
    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/orders/" + orderId + "/confirm", {
            method: "PUT"
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Order confirmed successfully.";

        await loadAdminOrders();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}