const invoiceProviderTokenInput = document.getElementById("invoice-provider-token");
const deactivationDateInput = document.getElementById("deactivation-date");
const saveButton = document.getElementById("save-btn");

function saveBotProps() {
    saveButton.disabled = true;
    const oldVal = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';

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
        })
        .fail(function (jqXHR) {
            if(jqXHR.status === 401) {
                window.location.reload();
            }
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            window.alert("Ошибка сохранения");
        });
}