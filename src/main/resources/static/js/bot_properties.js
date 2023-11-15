const successInfoDiv = document.getElementById("success-info");
const errorInfoDiv = document.getElementById("error-info");
const botPropsForm = document.getElementById("bot-props-form");
const invoiceProviderTokenInput = document.getElementById("invoice-provider-token");
const deactivationDateInput = document.getElementById("deactivation-date");
const saveButton = document.getElementById("save-btn");

function saveBotProps() {
    if (!botPropsForm.checkValidity()) {
        botPropsForm.reportValidity();
        return;
    }

    saveButton.disabled = true;
    const oldVal = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';
    successInfoDiv.classList.add('d-none');
    errorInfoDiv.classList.add('d-none');

    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/properties/update",
        headers: {'X-Session-Id': sessionId},
        contentType: "application/json",
        data: JSON.stringify({
            invoiceProviderToken: invoiceProviderTokenInput.value,
            deactivationDate: deactivationDateInput.value
        })
    })
        .done(function () {
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            successInfoDiv.classList.remove('d-none');
        })
        .fail(function (jqXHR) {
            if(jqXHR.status === 401) {
                window.location.reload();
            }
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            errorInfoDiv.classList.remove('d-none');
        });
}