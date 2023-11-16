const errorInfoDiv = document.getElementById("error-info");
const themeId = document.getElementById("theme-id").value;
const drawingForm = document.getElementById("drawing-form");
const drawingCaptionInput = document.getElementById("drawing-caption");
const drawingImageInput = document.getElementById("drawing-image");
const backButton = document.getElementById("back-btn");
const saveButton = document.getElementById("save-btn");

async function saveDrawing() {
    if (!drawingForm.checkValidity()) {
        drawingForm.reportValidity();
        return;
    }
    const files = drawingImageInput.files;
    if (!files || files.length === 0) {
        return;
    }
    const file = files[0];
    const content = await toBase64(file);

    saveButton.disabled = true;
    const oldVal = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';


    errorInfoDiv.classList.add('d-none');

    $.ajax({
        method: "POST",
        url: "/ad_vita_bot/api/drawing/upload",
        headers: {'X-Session-Id': sessionId},
        contentType: "application/json",
        data: JSON.stringify({
            themeId: themeId,
            caption: drawingCaptionInput.value,
            content: content
        })
    })
        .done(function () {
            backButton.click();
        })
        .fail(function (jqXHR) {
            errorInfoDiv.innerText = "Ошибка сохранения";
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