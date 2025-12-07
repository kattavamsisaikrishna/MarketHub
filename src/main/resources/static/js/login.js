document.addEventListener("DOMContentLoaded", function () {
    const loginBtn = document.getElementById("loginBtn");
    const msg = document.getElementById("message");

    loginBtn.addEventListener("click", async function () {
        msg.textContent = "";

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        if (!username || !password) {
            msg.textContent = "Please enter username and password";
            return;
        }

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                msg.textContent = errorData.message || "Login failed";
                return;
            }

            const data = await response.json();

            // Save token and role in localStorage
            localStorage.setItem("token", data.token);
            localStorage.setItem("role", data.role);
            localStorage.setItem("username", username);

            // Redirect based on role (we'll create these pages later)
            if (data.role === "SELLER") {
                window.location.href = "/seller-dashboard.html";
            } else if (data.role === "CUSTOMER") {
                window.location.href = "/customer-products.html";
            } else {
                msg.textContent = "Unknown role: " + data.role;
            }

        } catch (err) {
            console.error(err);
            msg.textContent = "Something went wrong. Try again.";
        }
    });
});
