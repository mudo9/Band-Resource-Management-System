function confirmRemoval(){
    return confirm("Are you sure you want to delete this member?");
}

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

function demoteCommitteeMember(id){

    $.ajax({
        url: '/director/committee/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/director/committee";
        },
        error: function(xhr){
            alert("Error removing committee member: " + xhr.responseText);
        }
    });
}

$(document).ready(function(){

    $('#removeCommitteeMemberButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmRemoval()){
            demoteCommitteeMember(id);
        }
    });
})