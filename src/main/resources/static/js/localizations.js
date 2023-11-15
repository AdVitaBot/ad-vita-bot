const successSaveToast = bootstrap.Toast.getOrCreateInstance(document.getElementById("success-save-toast"));
const errorSaveToast = bootstrap.Toast.getOrCreateInstance(document.getElementById("error-save-toast"));

function updateLocalization(updateButton) {
    const localizationCode = updateButton.dataset.localizationCode;
    const localizationMessage = document.getElementById('localization-' + localizationCode).value;

    updateButton.disabled = true;
    const oldVal = updateButton.innerHTML;
    updateButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';

    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/localization/update",
        headers: {'X-Session-Id': sessionId},
        contentType: "application/json",
        data: JSON.stringify({
            code: localizationCode,
            message: localizationMessage
        })
    })
        .done(function () {
            updateButton.innerHTML = oldVal;
            updateButton.disabled = false;
            successSaveToast.show();
        })
        .fail(function (jqXHR) {
            if(jqXHR.status === 401) {
                window.location.reload();
            }
            updateButton.innerHTML = oldVal;
            updateButton.disabled = false;
            errorSaveToast.show();
        });
}