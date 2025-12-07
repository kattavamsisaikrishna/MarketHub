// Get token from localStorage
function getToken() {
    return localStorage.getItem("token");
}

// Get role (CUSTOMER or SELLER)
function getRole() {
    return localStorage.getItem("role");
}

// Get username
function getUsername() {
    return localStorage.getItem("username");
}

// Simple wrapper around fetch that adds Authorization header
async function authFetch(url, options = {}) {
    const token = getToken();
    const headers = options.headers || {};

    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    return fetch(url, {
        ...options,
        headers
    });
}

// Redirect to login if not authenticated
function ensureAuthenticated() {
    if (!getToken()) {
        window.location.href = "/login.html";
    }
}

// Redirect if not seller
function ensureSeller() {
    ensureAuthenticated();
    if (getRole() !== "SELLER") {
        alert("Only sellers can access this page.");
        window.location.href = "/customer-products.html";
    }
}

// Redirect if not customer
function ensureCustomer() {
    ensureAuthenticated();
    if (getRole() !== "CUSTOMER") {
        alert("Only customers can access this page.");
        window.location.href = "/seller-dashboard.html";
    }
}
