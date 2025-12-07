document.addEventListener("DOMContentLoaded", function () {
    // Make sure only SELLER can access
    ensureSeller();

    const sellerNameEl = document.getElementById("sellerName");
    const roleLabelEl = document.getElementById("roleLabel");
    const logoutBtn = document.getElementById("logoutBtn");
    const productsBody = document.getElementById("productsBody");

    const addProductBtn = document.getElementById("addProductBtn");
    const cancelEditBtn = document.getElementById("cancelEditBtn");
    const msg = document.getElementById("message");
    const formTitle = document.getElementById("formTitle");

    // Form fields
    const nameInput = document.getElementById("name");
    const descInput = document.getElementById("description");
    const priceInput = document.getElementById("price");
    const stockInput = document.getElementById("stock");
    const categoryInput = document.getElementById("category");

    let editingProductId = null; // null = add mode, number = edit mode

    sellerNameEl.textContent = getUsername() || "(unknown)";
    roleLabelEl.textContent = getRole();

    logoutBtn.addEventListener("click", function () {
        localStorage.clear();
        window.location.href = "/login.html";
    });

    // Load products on page load
    loadProducts();

    addProductBtn.addEventListener("click", async function () {
        msg.textContent = "";
        msg.className = "msg-error";

        const name = nameInput.value.trim();
        const description = descInput.value.trim();
        const priceStr = priceInput.value.trim();
        const stockStr = stockInput.value.trim();
        const category = categoryInput.value.trim();

        if (!name || !priceStr || !stockStr) {
            msg.textContent = "Name, price, and stock are required.";
            return;
        }

        const price = parseFloat(priceStr);
        const stockQuantity = parseInt(stockStr, 10);

        if (isNaN(price) || isNaN(stockQuantity)) {
            msg.textContent = "Price and stock must be valid numbers.";
            return;
        }

        // Build request body (same for add and update)
        const body = {
            name,
            description,
            price,
            stockQuantity,
            category
        };

        try {
            let response;
            if (editingProductId === null) {
                // ADD
                response = await authFetch("/api/seller/products", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(body)
                });
            } else {
                // UPDATE
                response = await authFetch("/api/seller/products/" + editingProductId, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(body)
                });
            }

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                msg.textContent = data.message || (editingProductId ? "Failed to update product" : "Failed to add product");
                return;
            }

            msg.className = "msg-success";
            msg.textContent = editingProductId
                ? "Product updated successfully!"
                : "Product added successfully!";

            // Clear form & reset mode
            resetForm();

            // Reload product list
            loadProducts();

        } catch (err) {
            console.error(err);
            msg.textContent = "Something went wrong. Try again.";
        }
    });

    cancelEditBtn.addEventListener("click", function () {
        resetForm();
        msg.textContent = "";
    });

    async function loadProducts() {
        productsBody.innerHTML = "<tr><td colspan='7'>Loading...</td></tr>";

        try {
            const response = await authFetch("/api/seller/products", {
                method: "GET"
            });

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

                tr.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.name || ""}</td>
                    <td>${product.description || ""}</td>
                    <td>${product.price}</td>
                    <td>${product.stockQuantity}</td>
                    <td>${product.category || ""}</td>
                    <td>
                        <button data-id="${product.id}" class="edit-btn btn-small">Edit</button>
                        <button data-id="${product.id}" class="delete-btn btn-small secondary">Delete</button>
                    </td>
                `;

                productsBody.appendChild(tr);
            });

            // Attach edit listeners
            document.querySelectorAll(".edit-btn").forEach(btn => {
                btn.addEventListener("click", () => {
                    const productId = btn.getAttribute("data-id");
                    const row = btn.closest("tr");
                    startEditProduct(productId, row);
                });
            });

            // Attach delete listeners
            document.querySelectorAll(".delete-btn").forEach(btn => {
                btn.addEventListener("click", async function () {
                    const productId = this.getAttribute("data-id");
                    if (!confirm("Are you sure you want to delete product #" + productId + "?")) {
                        return;
                    }

                    try {
                        const resp = await authFetch("/api/seller/products/" + productId, {
                            method: "DELETE"
                        });

                        const respData = await resp.json().catch(() => ({}));

                        if (!resp.ok) {
                            alert(respData.message || "Failed to delete product");
                            return;
                        }

                        alert("Product deleted successfully.");
                        loadProducts();
                    } catch (err) {
                        console.error(err);
                        alert("Something went wrong while deleting.");
                    }
                });
            });

        } catch (err) {
            console.error(err);
            productsBody.innerHTML = "<tr><td colspan='7'>Error loading products.</td></tr>";
        }
    }

    function startEditProduct(productId, row) {
        editingProductId = productId;

        const cells = row.querySelectorAll("td");
        const idCell = cells[0];
        const nameCell = cells[1];
        const descCell = cells[2];
        const priceCell = cells[3];
        const stockCell = cells[4];
        const categoryCell = cells[5];

        nameInput.value = nameCell.textContent || "";
        descInput.value = descCell.textContent || "";
        priceInput.value = priceCell.textContent || "";
        stockInput.value = stockCell.textContent || "";
        categoryInput.value = categoryCell.textContent || "";

        formTitle.textContent = "Edit Product #" + idCell.textContent;
        addProductBtn.textContent = "Save Changes";
        cancelEditBtn.style.display = "inline-block";

        msg.className = "msg-error";
        msg.textContent = "You are editing product #" + idCell.textContent;
    }

    function resetForm() {
        editingProductId = null;

        nameInput.value = "";
        descInput.value = "";
        priceInput.value = "";
        stockInput.value = "";
        categoryInput.value = "";

        formTitle.textContent = "Add New Product";
        addProductBtn.textContent = "Add Product";
        cancelEditBtn.style.display = "none";
    }
});
