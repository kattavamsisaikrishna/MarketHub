document.addEventListener("DOMContentLoaded", function () {
    const registerBtn = document.getElementById("registerBtn");
    const msg = document.getElementById("message");

    registerBtn.addEventListener("click", async function () {
        msg.textContent = "";

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const roleInput = document.querySelector("input[name='role']:checked");
        const role = roleInput ? roleInput.value : null;

        if (!username || !password || !role) {
            msg.textContent = "Please fill all fields and select a user type.";
            return;
        }

        try {
            const response = await fetch("/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ username, password, role })
            });

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                msg.textContent = data.message || "Registration failed";
                return;
            }

            // Success
            msg.style.color = "green";
            msg.textContent = "Registration successful! Redirecting to login...";

            setTimeout(() => {
                window.location.href = "/login.html";
            }, 1500);

        } catch (err) {
            console.error(err);
            msg.textContent = "Something went wrong. Try again.";
        }
    });
});
