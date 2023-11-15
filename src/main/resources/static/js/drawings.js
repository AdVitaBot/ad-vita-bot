function removeDrawing(updateButton) {
    const drawingId = updateButton.dataset.drawingId;

    updateButton.disabled = true;
    const oldVal = updateButton.innerHTML;
    updateButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';

    const cardDiv = document.getElementById("drawing-card-" + drawingId)

    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/drawing/delete",
        headers: {'X-Session-Id': sessionId},
        contentType: "application/json",
        data: JSON.stringify({
            id: drawingId
        })
    })
        .done(function () {
            cardDiv.remove();
        })
        .fail(function (jqXHR) {
            if(jqXHR.status === 401) {
                window.location.reload();
            }
            updateButton.innerHTML = oldVal;
            updateButton.disabled = false;
            window.alert("Ошибка удаления");
        });
}