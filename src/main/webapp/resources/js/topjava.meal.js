const mealAjaxUrl = "ui/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

function editRow(id) {
    $.ajax({
        url: mealAjaxUrl + id,
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

function clearFilter() {
    $('#filter-form').find(":input").val("");
    updateTable();
}

function updateMealTable() {
    let url = mealAjaxUrl;
    let filterIsEmpty = $("#filter-form input").filter(function () {return $.trim($(this).val()).length !== 0;}).length === 0;
    let filter = null;
    if (!filterIsEmpty) {
        url += "filter";
        filter = $('#filter-form').serialize();
    }
    $.get(url, filter, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
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
        }),
        updateMealTable
    );
});