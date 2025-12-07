document.addEventListener("DOMContentLoaded", function () {
    // Only customers allowed
    ensureCustomer();

    const customerNameEl = document.getElementById("customerName");
    const roleLabelEl = document.getElementById("roleLabel");
    const logoutBtn = document.getElementById("logoutBtn");

    const productsBody = document.getElementById("productsBody");
    const placeOrderBtn = document.getElementById("placeOrderBtn");
    const msg = document.getElementById("message");

    const searchInput = document.getElementById("searchInput");
    const searchBtn = document.getElementById("searchBtn");
    const clearSearchBtn = document.getElementById("clearSearchBtn");

    const loadOrdersBtn = document.getElementById("loadOrdersBtn");
    const ordersList = document.getElementById("ordersList");

    customerNameEl.textContent = getUsername() || "(unknown)";
    roleLabelEl.textContent = getRole();

    logoutBtn.addEventListener("click", function () {
        localStorage.clear();
        window.location.href = "/login.html";
    });

    // Load all products initially
    loadProducts();

    // Load orders initially
    loadOrders();

    searchBtn.addEventListener("click", function () {
        const keyword = searchInput.value.trim();
        if (!keyword) {
            msg.textContent = "Enter something to search.";
            return;
        }
        loadProducts(keyword);
    });

    clearSearchBtn.addEventListener("click", function () {
        searchInput.value = "";
        msg.textContent = "";
        loadProducts();
    });

    placeOrderBtn.addEventListener("click", async function () {
        msg.style.color = "red";
        msg.textContent = "";

        const items = collectOrderItems();
        if (items.length === 0) {
            msg.textContent = "Please enter quantity for at least one product.";
            return;
        }

        try {
            const response = await authFetch("/api/orders", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ items })
            });

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                msg.textContent = data.message || "Failed to place order.";
                return;
            }

            msg.style.color = "green";
            msg.textContent = "Order placed successfully! Order ID: " + data.id;

            // Clear quantity inputs
            document.querySelectorAll(".qty-input").forEach(input => input.value = "");

            // Reload products (to refresh stock)
            loadProducts();

            // Reload orders
            loadOrders();

        } catch (err) {
            console.error(err);
            msg.textContent = "Something went wrong. Try again.";
        }
    });

    loadOrdersBtn.addEventListener("click", function () {
        loadOrders();
    });

    async function loadProducts(keyword) {
        productsBody.innerHTML = "<tr><td colspan='7'>Loading...</td></tr>";

        try {
            let url = "/api/products";
            if (keyword) {
                url = "/api/products/search?keyword=" + encodeURIComponent(keyword);
            }

            const response = await authFetch(url, { method: "GET" });

            if (response.status === 401 || response.status === 403) {
                alert("Session expired or unauthorized. Please login again.");
                localStorage.clear();
                window.location.href = "/login.html";
                return;
            }

            const data = await response.json().catch(() => []);

            if (!response.ok) {
                productsBody.innerHTML = "<tr><td colspan='7'>Failed to load products.</td></tr>";
                return;
            }

            if (!Array.isArray(data) || data.length === 0) {
                productsBody.innerHTML = "<tr><td colspan='7'>No products found.</td></tr>";
                return;
            }

            productsBody.innerHTML = "";

            data.forEach(product => {
                const tr = document.createElement("tr");

                const stock = product.stockQuantity ?? 0;

                tr.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.sellerUsername || ""}</td>
                    <td>${product.name || ""}</td>
                    <td>${product.description || ""}</td>
                    <td>${product.price}</td>
                    <td>${stock}</td>
                    <td>
                        <input type="number" min="0" max="${stock}" class="qty-input"
                               data-product-id="${product.id}" style="width: 80px;">
                    </td>
                `;

                productsBody.appendChild(tr);
            });

        } catch (err) {
            console.error(err);
            productsBody.innerHTML = "<tr><td colspan='7'>Error loading products.</td></tr>";
        }
    }

    function collectOrderItems() {
        const items = [];
        document.querySelectorAll(".qty-input").forEach(input => {
            const value = input.value.trim();
            if (!value) return;
            const qty = parseInt(value, 10);
            if (isNaN(qty) || qty <= 0) return;

            const productId = parseInt(input.getAttribute("data-product-id"), 10);
            if (!isNaN(productId)) {
                items.push({
                    productId: productId,
                    quantity: qty
                });
            }
        });
        return items;
    }

    async function loadOrders() {
        ordersList.innerHTML = "<li>Loading orders...</li>";

        try {
            const response = await authFetch("/api/orders/my", { method: "GET" });

            if (response.status === 401 || response.status === 403) {
                ordersList.innerHTML = "<li>Unauthorized. Please login again.</li>";
                return;
            }

            const data = await response.json().catch(() => []);

            if (!response.ok) {
                ordersList.innerHTML = "<li>Failed to load orders.</li>";
                return;
            }

            if (!Array.isArray(data) || data.length === 0) {
                ordersList.innerHTML = "<li>No orders yet.</li>";
                return;
            }

            ordersList.innerHTML = "";

            data.forEach(order => {
                const li = document.createElement("li");

                const createdAt = order.createdAt || "";
                const itemsText = (order.items || [])
                    .map(i => `${i.productName} x ${i.quantity}`)
                    .join(", ");

                li.textContent = `Order #${order.id} | Total: ${order.totalAmount} | Status: ${order.status} | ${createdAt} | Items: ${itemsText}`;

                ordersList.appendChild(li);
            });

        } catch (err) {
            console.error(err);
            ordersList.innerHTML = "<li>Error loading orders.</li>";
        }
    }
});
