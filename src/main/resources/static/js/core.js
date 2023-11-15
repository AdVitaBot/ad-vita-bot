const sessionId = getSessionId();

function getSessionId() {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; X-Session-Id=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function logout() {
    $.ajax({
        method: "GET",
        url: "/ad_vita_bot/api/auth/logout",
        headers: {'X-Session-Id': getSessionId()},
        contentType: "application/json"
    })
    .done(function () {
        window.location.reload();
    });
}

Element.prototype.remove = function() {
    this.parentElement.removeChild(this);
}
NodeList.prototype.remove = HTMLCollection.prototype.remove = function() {
    for(let i = this.length - 1; i >= 0; i--) {
        if(this[i] && this[i].parentElement) {
            this[i].parentElement.removeChild(this[i]);
        }
    }
}

const toBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result.split(',')[1]);
    reader.onerror = reject;
});