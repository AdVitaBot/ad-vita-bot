const themeId = document.getElementById("theme-id").value;
const drawingCaptionInput = document.getElementById("drawing-caption");
const drawingImageInput = document.getElementById("drawing-image");
const backButton = document.getElementById("back-btn");
const saveButton = document.getElementById("save-btn");

async function saveDrawing() {
    saveButton.disabled = true;
    const oldVal = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-pulse"></i>';

    const file = drawingImageInput.files[0];
    const content = await toBase64(file);
    console.log(content)

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
            if(jqXHR.status === 401) {
                window.location.reload();
            }
            saveButton.innerHTML = oldVal;
            saveButton.disabled = false;
            window.alert("Ошибка сохранения");
        });
}