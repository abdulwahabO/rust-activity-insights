var chart_options = {
    scales: {
        yAxes: [{
            ticks: {
                beginAtZero: true
            }
        }]
    }
};

var bar_chart_ctx = document.getElementById("bar_chart").getContext("2d");
var barChart = new Chart(bar_chart_ctx, {
    type: 'bar',
    data: {
        datasets: [{
            backgroundColor: ["#0a8691", "#ba1436", "#3d408f", "#6a7a47", "#6188b0"],
            borderWidth: 0,
            data: []
        }],
        labels: []
    },
    options: chart_options
});

var doughnut_chart_ctx = document.getElementById("doughnut_chart").getContext("2d");
var doughnutChart = new Chart(doughnut_chart_ctx, {
    type: 'doughnut',
    data: {
        datasets: [{
            backgroundColor: ["#0a8691", "#ba1436", "#3d408f", "#6a7a47", "#6188b0"],
            data: []
        }],
        labels: []
    },
    options: {
        cutoutPercentage: 56
    }
});

var api = document.getElementById("api-url").value;

function onpick(start, end) {
    $('#date-range span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
    fetch(api + new URLSearchParams({
        from_date: start.format('YYYY-MM-DD'),
        to_date: end.format('YYYY-MM-DD'),
    })).then(response => {
        if (response.status == 200) {
            response.json().then(data => {
                const issuesData = data.repo_issues_activity;
                var bar_labels = [];
                var bar_data = [];
                for (var x in issuesData) {
                    bar_data.push(issuesData[x]);
                    bar_labels.push(x);
                }
                barChart.data.datasets[0].data = bar_data;
                barChart.data.labels = bar_labels;

                const percentData = data.activities_percentage;
                var dough_labels = [];
                var dough_data = [];
                for(var y in percentData) {
                    dough_labels.push(y);
                    dough_data.push(percentData[y]);
                }
                doughnutChart.data.labels = dough_labels;
                doughnutChart.data.datasets[0].data = dough_data;

                barChart.update();
                doughnutChart.update();
            });
            
        } else {
            console.warn("STATUS CODE: " + response.status);
        }
    }).catch(console.error);
}

var init_range_picker = function() {
    var start = moment().subtract(7, 'days');
    var end = moment();

    $('#date-range').daterangepicker({
        startDate: start,
        endDate: end,
        ranges: {
           'Last 7 Days': [moment().subtract(6, 'days'), moment()],
           'Last 30 Days': [moment().subtract(29, 'days'), moment()],
           'This Month': [moment().startOf('month'), moment().endOf('month')],
           'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    }, onpick);

    onpick(start, end);
};

init_range_picker();
