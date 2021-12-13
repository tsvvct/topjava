const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

//overriding func from topjava.common.js
function formSerializer() {
    return form.serializeArray().reduce(reducer, {});
}

function reducer(output, value) {
    if (value.name === "calories" && value.value === "") {
        return output;
    }
    output[value.name] = value.value;
    return output;
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 16).replace("T", " ")
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );

    let inputs = ["startDate", "endDate", "startTime", "endTime", "dateTime"];
    let forControl;
    for (const inputName of inputs) {
        forControl = document.querySelector('input[name="' + inputName + '"]');
        forControl.addEventListener('click', function(e) {e.preventDefault();});
    }

    let locale = (navigator.language === 'ru-RU') ? 'ru' : 'en';
    $.datetimepicker.setLocale(locale);

    $('#startDate').datetimepicker({
        format:'Y-m-d',
        timepicker:false
    });

    $('#endDate').datetimepicker({
        format:'Y-m-d',
        timepicker:false
    });

    $('#startTime').datetimepicker({
        datepicker:false,
        step:10,
        format:'H:i'
    });

    $('#endTime').datetimepicker({
        datepicker:false,
        step:10,
        format:'H:i'
    });

    $('#dateTime').datetimepicker({
        validateOnBlur:false,
        step:10,
        format:'Y-m-d\\TH:i'
    });
});