const API_URL = "";

function getCurrentUser() {
    const user = sessionStorage.getItem("currentUser");

    if (!user) {
        return null;
    }

    return JSON.parse(user);
}

function requireLogin() {
    const user = getCurrentUser();

    if (!user) {
        window.location.href = "login.html";
        return null;
    }

    return user;
}

function requireRole(role) {
    const user = requireLogin();

    if (!user) {
        return null;
    }

    if (user.role !== role) {
        alert("Access denied");
        window.location.href = "login.html";
        return null;
    }

    return user;
}

async function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const message = document.getElementById("message");

    try {
        const response = await fetch(API_URL + "/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        const user = await response.json();

        sessionStorage.setItem("currentUser", JSON.stringify(user));

        if (user.role === "ADMIN") {
            window.location.href = "admin-products.html";
        } else {
            window.location.href = "products.html";
        }
    } catch (error) {
        message.textContent = error.message;
    }
}

async function logout() {
    await fetch(API_URL + "/api/auth/logout", {
        method: "POST"
    });

    sessionStorage.removeItem("currentUser");
    window.location.href = "login.html";
}