
function confirmDeletion(){
    return confirm("Are you sure you want to delete this music order?");
}


$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});


function deleteMusicOrder(id){

    $.ajax({
        url: '/music-order/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/music";
        },
        error: function(xhr){
            const errorText = xhr.responseText || ""; // Get error text from response
            alert("Error deleting music set: " + errorText);
        }
    });
}

function deleteChildMusicOrder(musicOrderId, childId){

    $.ajax({
        url: '/music-order/' + musicOrderId,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/child/" + childId + "/music"
        },
        error: function(xhr){
            const errorText = xhr.responseText || ""; // Get error text from response
            alert("Error deleting music set: " + errorText);
        }
    });
}



$(document).ready(function(){

    $('#deleteMusicOrderButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmDeletion()){
            deleteMusicOrder(id);
        }
    });
    $('#deleteChildMusicOrderButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        const [musicOrderId, childId] = id.split('_');
        if(confirmDeletion()){
            deleteChildMusicOrder(musicOrderId, childId);
        }
    });

})