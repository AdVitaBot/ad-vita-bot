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
        })
        .fail(function (jqXHR) {
            if(jqXHR.status === 401) {
                window.location.reload();
            }
            updateButton.innerHTML = oldVal;
            updateButton.disabled = false;
            window.alert("Ошибка сохранения");
        });
}