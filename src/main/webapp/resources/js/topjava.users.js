const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

function onCheckboxClick() {
    let userRow = $(this).closest('#user-row');
    let userId = $(this).closest('tr').attr("data-item-id");
    let enabled = this.checked ? " enabled" : " disabled";
    $.ajax({
        type: "PUT",
        url: "rest/admin/users/" + userId + "/enable?enable=" + this.checked
    }).done(() => {
        userRow.attr("class", this.checked ? "top-java-enabled" : "top-java-disabled");
        successNoty("" + userId + enabled);
    }).fail(
        () => {this.checked = !this.checked;}
    );
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    );

    $( "input[type=checkbox]" ).on( "click", onCheckboxClick);
});