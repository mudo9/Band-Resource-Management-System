
function confirmDeletion(){
    return confirm("Are you sure you want to delete this music set?");
}

function confirmUpdate(){
    return confirm("Are you sure you want to update this music set?");
}

function confirmStorage(){
    return confirm("Are you sure you want to put the music set into storage," +
        "this apply to all the bands")
}

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
function updateMusicSet(id){

    const title = $('#title').val();
    const composer = $('#composer').val();
    const arranger = $('#arranger').val()
    const suitableForTraining = $('#suitableForTraining').is(':checked');
    if (title === "") {
        alert("Title cannot be empty!");
        return; // Stop further execution if title is empty
    }
    if(composer === ""){
        alert("Composer cannot be empty!");
        return;
    }
    $.ajax({
        url: '/committee-member/music-set/' + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            title: title,
            composer: composer,
            arranger: arranger,
            suitableForTraining: suitableForTraining
        }),

        success: function(response){
            alert(response);
            window.location.href = "/committee-member/music";
        },
        error: function(xhr){
            alert("Error updating music set: " + xhr.responseText);
        }
    });
}

function deleteMusicSet(id){

    $.ajax({
        url: '/committee-member/music-set/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/committee-member/music";
        },
        error: function(xhr){
            const errorText = xhr.responseText || ""; // Get error text from response
            if (errorText.includes("a foreign key constraint fails")) {
                alert("Cannot delete this music set due to foreign key constraint. " +
                    "Please remove all music parts and try again.");
            } else {
                alert("Error deleting music set: " + errorText);
            }
        }
    });
}

function storageMusicSet(id){
    $.ajax({
        url: '/committee-member/music-set/' + id + '/practice',
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/committee-member/music-set/" + id;
        },
        error: function(xhr){
            alert("Error putting music set into storage: " + xhr.responseText);
        }
    });
}


$(document).ready(function(){
    $('#updateMusicSetButton').click(function(event){

        event.preventDefault();
        const id = $(this).data('id');
        if(confirmUpdate()){
            updateMusicSet(id);
        }
    });
    $('#deleteMusicSetButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmDeletion()){
            deleteMusicSet(id);
        }
    });
    $('#deletePracticeButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmStorage()){
            storageMusicSet(id);
        }
    });
})