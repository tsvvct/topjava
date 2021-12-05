const mealAjaxUrl = "meals/ui/";
const mealRestAjaxUrl = "rest/profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

function editRow(id) {
    $.ajax({
        url: mealRestAjaxUrl + id,
        type: "GET"
    }).done(function (meal) {
        $("#modal-title-add").hide();
        $("#modal-title-edit").show();
        form.find("#id").val(meal.id);
        form.find("#date-time").val(meal.dateTime);
        form.find("#description").val(meal.description);
        form.find("#calories").val(meal.calories);
        $("#editRow").modal();
    });
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});