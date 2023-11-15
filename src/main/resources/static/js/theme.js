const successInfoDiv = document.getElementById("success-info");
const errorInfoDiv = document.getElementById("error-info");
const themeId = document.getElementById("theme-id").value;
const themeForm = document.getElementById("theme-form");
const themeDescriptionInput = document.getElementById("theme-description");
const minDonationAmountInput = document.getElementById("min-donation-amount");
const maxDonationAmountInput = document.getElementById("max-donation-amount");
const saveButton = document.getElementById("save-btn");

function updateTheme() {
    if (!themeForm.checkValidity()) {
        themeForm.reportValidity();
        return;
    }
    saveButton.disabled = true;
    const oldVal = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';
    successInfoDiv.classList.add('d-none');
    errorInfoDiv.classList.add('d-none');


    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/theme/update",
        headers: {'X-Session-Id': sessionId},
        contentType: "application/json",
        data: JSON.stringify({
            id: themeId,
            minDonationAmount: minDonationAmountInput.value,
            maxDonationAmount: maxDonationAmountInput.value,
            description: themeDescriptionInput.value
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
            }
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            errorInfoDiv.classList.remove('d-none');
        });
}