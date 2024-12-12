function confirmUpdate(){
    return confirm("Are you sure you want to update your account?");
}

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
function updateAccount(id, name, password){
    const email = $('#email').val();
    const phoneNumber = $('#phoneNumber').val();

    if (email === "") {
        alert("Email cannot be empty!");
        return; // Stop further execution if name is empty
    }
    if(phoneNumber === ""){
        alert("Phone number cannot be empty!");
        return;
    }

    $.ajax({
        url: '/my-account/' + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            email: email,
            password: password,
            phoneNumber: phoneNumber,
            fullName: name,
        }),

        success: function(response){
            alert(response);
            logout();
        },
        error: function(xhr){
            alert("Error updating account: " + xhr.responseText);
        }
    });
}

function logout() {
    $.ajax({
        url: '/logout',
        type: 'POST',
        success: function() {
            window.location.href = "/login?logout"; // Redirect after logout
        },
        error: function(xhr) {
            alert("Error logging out: " + xhr.responseText);
        }
    });
}


$(document).ready(function(){
    $('#updateAccountButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        const name = $(this).data('name')
        const password = $(this).data('password')
        if(confirmUpdate()){
            updateAccount(id, name, password);
        }
    });
})