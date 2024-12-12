
function confirmDeletion(){
    return confirm("Are you sure you want to delete this member?");
}

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

function deleteTrainingBandMember(id){

    $.ajax({
        url: '/director/training-band/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/director/training-band";
        },
        error: function(xhr){
            alert("Error deleting member: " + xhr.responseText);
        }
    });
}

$(document).ready(function(){

    $('#deleteTrainingBandMemberButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmDeletion()){
            deleteTrainingBandMember(id);
        }
    });
})