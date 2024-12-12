
function confirmDeletion(){
    return confirm("Are you sure you want to delete this miscellaneous item?");
}

function confirmUpdate(){
    return confirm("Are you sure you want to update this miscellaneous item?");
}


$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
function updateMiscellaneous(id){

    const name = $('#name').val();
    const quantity = $('#quantity').val();
    const make = $('#make').val()
    const specificForInstrument = $('#specificForInstrument').val()
    if (name === "") {
        alert("Name of miscellaneous item cannot be empty!");
        return; // Stop further execution if name is empty
    }
    if(quantity === ""){
        alert("quantity cannot be empty!");
        return;
    }
    if(make === ""){
        alert("Make cannot be empty!");
        return;
    }
    $.ajax({
        url: '/committee-member/miscellaneous/' + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            name: name,
            quantity: quantity,
            make: make,
            specificForInstrument: specificForInstrument,
        }),

        success: function(response){
            alert(response);
            window.location.href = "/committee-member/items";
        },
        error: function(xhr){
            alert("Error updating miscellaneous item: " + xhr.responseText);
        }
    });
}

function deleteMiscellaneous(id){

    $.ajax({
        url: '/committee-member/miscellaneous/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/committee-member/items";
        },
        error: function(xhr){
            const errorText = xhr.responseText || ""; // Get error text from response
            alert("Error deleting miscellaneous: " + errorText);
        }
    });
}


$(document).ready(function(){
    $('#updateMiscellaneousButton').click(function(event){

        event.preventDefault();
        const id = $(this).data('id');
        if(confirmUpdate()){
            updateMiscellaneous(id);
        }
    });
    $('#deleteMiscellaneousButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmDeletion()){
            deleteMiscellaneous(id);
        }
    });
})