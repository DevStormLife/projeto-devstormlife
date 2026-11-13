window.STORMLIFE_API_ORIGIN = location.hostname === "stormlife.lab"
    ? "http://api.stormlife.lab"
    : "http://localhost:8080";
window.stormFetch = (url, options = {}) => fetch(url, { credentials: "include", ...options });
