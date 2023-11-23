const successInfoDiv = document.getElementById("success-info");
const errorInfoDiv = document.getElementById("error-info");
const accountForm = document.getElementById("account-form");
const oldPasswordInput = document.getElementById("old-password");
const newPasswordInput = document.getElementById("new-password");
const repeatNewPasswordInput = document.getElementById("repeat-new-password");
const validationNewPasswordDiv = document.getElementById("validation-new-password");
const validationRepeatNewPasswordDiv = document.getElementById("validation-repeat-new-password");
const saveButton = document.getElementById("save-btn");

function updatePassword() {
    if (!accountForm.checkValidity()) {
        accountForm.reportValidity();
        return;
    }

    const newPassword = newPasswordInput.value;
    if(newPassword.length < 8) {
        validationNewPasswordDiv.innerText = 'Пароль слишком короткий, минимум 8 символов';
        validationNewPasswordDiv.classList.remove('d-none');
        newPasswordInput.classList.add('is-invalid');
        return;
    } else {
        validationNewPasswordDiv.classList.add('d-none');
        newPasswordInput.classList.remove('is-invalid');
    }
    const repeatNewPassword = repeatNewPasswordInput.value;
    if(newPassword !== repeatNewPassword) {
        validationRepeatNewPasswordDiv.innerText = 'Пароли не совпадают';
        validationRepeatNewPasswordDiv.classList.remove('d-none');
        newPasswordInput.classList.add('is-invalid');
        repeatNewPasswordInput.classList.add('is-invalid');
        return;
    } else {
        validationRepeatNewPasswordDiv.classList.add('d-none');
        repeatNewPasswordInput.classList.remove('is-invalid');
    }
    saveButton.disabled = true;
    const oldVal = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';
    successInfoDiv.classList.add('d-none');
    errorInfoDiv.classList.add('d-none');

    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/account/changePassword",
        headers: {'X-Session-Id': sessionId},
        contentType: "application/json",
        data: JSON.stringify({
            oldPassword: oldPasswordInput.value,
            newPassword: newPassword
        })
    })
        .done(function () {
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            successInfoDiv.classList.remove('d-none');
        })
        .fail(function (jqXHR) {
            if (jqXHR.status === 401) {
                window.location.reload();
            } else if (jqXHR.status === 417) {
                if (jqXHR.responseText) {
                    const rsJson = JSON.parse(jqXHR.responseText);
                    if (rsJson && rsJson.error && rsJson.error.message) {
                        errorInfoDiv.innerText = rsJson.error.message;
                    }
                }
            }
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            errorInfoDiv.classList.remove('d-none');
        });
}