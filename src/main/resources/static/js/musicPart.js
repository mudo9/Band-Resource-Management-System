function confirmUpdate(){
    return confirm("Are you sure you want to update this music part?");
}
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
function updateMusicPart(musicPartId, musicSetId){
    const partName = $('#partName').val();

    if (partName === "") {
        alert("Part name cannot be empty!");
        return; // Stop further execution if part name is empty
    }
    $.ajax({
        url: '/committee-member/music-set/' + musicSetId + '/music-part/' + musicPartId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: musicPartId,
            partName: partName,
        }),

        success: function(response){
            alert(response);
            window.location.href = "/committee-member/music";
        },
        error: function(xhr){
            alert("Error updating music part: " + xhr.responseText);
        }
    });

}

$(document).ready(function(){
    $('#updateMusicPartButton').click(function(event){
        event.preventDefault();
        const musicPartId = $(this).data('musicpartid');
        const musicSetId = $(this).data('musicsetid');
        if(confirmUpdate()){
            updateMusicPart(musicPartId, musicSetId);
        }
    });

})

