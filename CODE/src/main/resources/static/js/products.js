async function loadProducts() {
    const user = requireRole("CONSUMER");
    if (!user) {
        return;
    }

    document.getElementById("welcome").textContent = "Welcome, " + user.fullName;

    const oldQuantities = {};
    document.querySelectorAll("input[id^='qty-']").forEach(input => {
        oldQuantities[input.id] = input.value;
    });

    const response = await fetch(API_URL + "/api/products");
    const products = await response.json();

    const body = document.getElementById("productsBody");
    body.innerHTML = "";

    products.forEach(product => {
        const inputId = "qty-" + product.id;
        const quantityValue = oldQuantities[inputId] || "1";

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${product.name}</td>
            <td>${product.category}</td>
            <td>${product.details}</td>
            <td>${product.price}</td>
            <td>${product.stock}</td>
            <td><input id="${inputId}" type="number" min="1" value="${quantityValue}"></td>
            <td><button onclick="addToCart(${product.id})">Add to Cart</button></td>
        `;

        body.appendChild(row);
    });
}

async function addToCart(productId) {
    const user = requireRole("CONSUMER");
    const quantity = Number(document.getElementById("qty-" + productId).value);
    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/cart/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: user.id,
                productId: productId,
                quantity: quantity
            })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        message.style.color = "green";
        message.textContent = "Product added to cart.";

        await loadProducts();
    } catch (error) {
        message.style.color = "#b00020";
        message.textContent = error.message;
    }
}