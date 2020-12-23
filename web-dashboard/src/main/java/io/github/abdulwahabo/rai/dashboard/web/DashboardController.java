package io.github.abdulwahabo.rai.dashboard.web;

import io.github.abdulwahabo.rai.dashboard.model.DashboardDataDto;
import io.github.abdulwahabo.rai.dashboard.service.DashboardService;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<DashboardDataDto> data(@RequestParam("from_date") String from, @RequestParam("to_date") String to) {
        DashboardDataDto dashboardDataDto = dashboardService.getData(from, to);
        return ResponseEntity.ok(dashboardDataDto);
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelMap modelMap) {
        String start = LocalDate.now().minusDays(10).toString();
        String end = LocalDate.now().toString();
        DashboardDataDto dashboardDataDto = dashboardService.getData(start, end);
        return new ModelAndView("dashboard", modelMap);
    }
}
