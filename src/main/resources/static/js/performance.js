function confirmDeletion() {
    return confirm("Are you sure you want to delete this performance?");
}

function confirmUpdate() {
    return confirm("Are you sure you want to update this performance?")
}

function confirmUpdateAvailability() {
    return confirm("Are you sure you want to update the availability?")
}

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

function updatePerformance(id){
    const date = $('#date').val();
    const time = $('#time').val();
    const location = $('#location').val()
    const musicSets = $('#musicSets').val()
    const seniorBand = $('#seniorBand').is(':checked');
    const trainingBand = $('#trainingBand').is(':checked');

    const musicSetsParam = musicSets.map(id => `musicSets=${id}`).join('&');
    if(location === "") {
        alert("Location cannot be empty!");
        return;
    }
    $.ajax({
        url: '/committee-member/performance/' + id +
            '?seniorBand=' + seniorBand +
            '&trainingBand=' + trainingBand +
            '&' + musicSetsParam,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            location: location,
            date: date,
            time: time,
        }),
        success: function(response){
            alert(response);
            window.location.href = "/committee-member/performance";
        },
        error: function(xhr){
            alert("Error updating performance: " + xhr.responseText);
        }
    });
}

function deletePerformance(id){

    $.ajax({
        url: '/committee-member/performance/' + id,
        type: 'DELETE',
        success: function(response){
            alert(response);
            window.location.href = "/committee-member/performance";
        },
        error: function(xhr){
            alert("Error deleting performance: " + xhr.responseText);
        }
    });
}

function updateChildAvailability(userId, bandId, performanceId) {
    const availability = $('#availability').is(':checked');
    $.ajax({
        url: '/performance/' + userId + '/' + bandId + '/' + performanceId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            availability: availability
        }),
        success: function (response){
            alert(response);
            window.location.href = "/child/" + userId + "/performance"
        },
        error: function (xhr){
            alert("Error updating availability: " + xhr.responseText);
        }
    });
}

function updateAvailability(userId, bandId, performanceId) {
    const availability = $('#availability').is(':checked');
    $.ajax({
        url: '/performance/' + userId + '/' + bandId + '/' + performanceId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            availability: availability
        }),
        success: function (response){
            alert(response);
            window.location.href = "/performance"
        },
        error: function (xhr){
            alert("Error updating availability: " + xhr.responseText);
        }
    });
}

$(document).ready(function(){
    $('#updatePerformanceButton').click(function(event){

        event.preventDefault();
        const id = $(this).data('id');
        if(confirmUpdate()){
            updatePerformance(id);
        }
    });
    $('#deletePerformanceButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        if(confirmDeletion()){
            deletePerformance(id);
        }
    });
    $('#updateChildPerformanceMemberButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        const [userId, bandId, performanceId] = id.split('_');
        if(confirmUpdateAvailability()){
            updateChildAvailability(userId, bandId, performanceId);
        }
    });
    $('#updatePerformanceMemberButton').click(function(event){
        event.preventDefault();
        const id = $(this).data('id');
        const [userId, bandId, performanceId] = id.split('_');
        if(confirmUpdateAvailability()){
            updateAvailability(userId, bandId, performanceId);
        }
    });
})