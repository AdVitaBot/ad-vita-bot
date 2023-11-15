const loginInfoField = document.getElementById("login-info-field");
const passwordInfoField = document.getElementById("password-info-field");
const authErrorInfo = document.getElementById("auth-error-info");
const loginButton = document.getElementById("login-button");
const loginField = document.getElementById("login-form-login");
const passwordField = document.getElementById("login-form-password");

function doLogin() {
    const username = loginField.value;
    const password = passwordField.value;
    loginButton.disabled = true;
    const oldVal = loginButton.innerHTML;
    loginButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';
    loginInfoField.innerText = '';
    loginField.classList.remove('is-invalid');
    passwordInfoField.innerText = '';
    passwordField.classList.remove('is-invalid');

    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/auth/login",
        contentType: "application/json",
        data: JSON.stringify({login: username, password: password})
    })
    .done(function (msg, textStatus, jqXHR ) {
        authErrorInfo.innerText = "";
        document.cookie = "X-Session-Id=" + jqXHR.getResponseHeader('X-Session-Id') + "; max-age=86400";
        window.location.reload();
        loginButton.innerHTML = oldVal;
        loginButton.disabled = false;
    }).fail(function () {
        authErrorInfo.innerText = "Service error";
        loginButton.innerHTML = oldVal;
        loginButton.disabled = false;
    });
    return false;
}

function onKeyUp(e) {
    if (e.which === 10 || e.which === 13) {
        this.form.submit();
    }
}