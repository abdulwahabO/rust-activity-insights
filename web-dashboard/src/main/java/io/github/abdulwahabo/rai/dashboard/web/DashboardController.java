package io.github.abdulwahabo.rai.dashboard.web;

import io.github.abdulwahabo.rai.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    // TODO a REST Endpoint to which date range is sent and which replies with data for that date range

    // TODO: An MVC endpoint which returns the page.

    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/data")
    @ResponseBody
    public void data(@RequestParam("from_date") String from, @RequestParam("to_date") String to) {

    }


    // TODO: reuse error pages from Filebox.

    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelMap modelMap) {
        // todo: Use services to get data
        //          populate model and send it with view.

        return null;
    }
}
