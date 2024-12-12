
function confirmDeletion(){
    return confirm("Are you sure you want to delete this instrument?");
}

function confirmUpdate(){
    return confirm("Are you sure you want to update this instrument?");
}


$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
function updateInstrument(id){

    const name = $('#name').val();
    const serialNumber = $('#serialNumber').val();
    const make = $('#make').val()
    if (name === "") {
        alert("Name of instrument cannot be empty!");
        return; // Stop further execution if name is empty
    }
    if(serialNumber === ""){
        alert("Serial number cannot be empty!");
        return;
    }
    if(make === ""){
        alert("Make cannot be empty!");
        return;
    }
    $.ajax({
        url: '/committee-member/instrument/' + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            name: name,
            serialNumber: serialNumber,
            make: make,
        }),

        success: function(response){
            alert(response);
            window.location.href = "/committee-member/items";
        },
        error: function(xhr){
            alert("Error updating instrument: " + xhr.responseText);
        }
    });
}

function deleteInstrument(id){

    $.ajax({
        url: '/committee-member/instrument/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/committee-member/items";
        },
        error: function(xhr){
            const errorText = xhr.responseText || ""; // Get error text from response
            alert("Error deleting instrument: " + errorText);
        }
    });
}


$(document).ready(function(){
    $('#updateInstrumentButton').click(function(event){

        event.preventDefault();
        const id = $(this).data('id');
        if(confirmUpdate()){
            updateInstrument(id);
        }
    });
    $('#deleteInstrumentButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmDeletion()){
            deleteInstrument(id);
        }
    });
})