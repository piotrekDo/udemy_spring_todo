package com.example.demo.controller;

import com.example.demo.logic.ProjectService;
import com.example.demo.model.Project;
import com.example.demo.model.ProjectStep;
import com.example.demo.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"))) {
            ProjectWriteModel projectWriteModel = new ProjectWriteModel();
            model.addAttribute("project", projectWriteModel);
            return "projects";
        }
        return "index";
    }

    @PostMapping(params = "addStep")
    String addStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping(params = "removeStep")
    String removeStep(@ModelAttribute("project") ProjectWriteModel current, @RequestParam(value = "removeStep") int index) {
        if (index > 0) current.getSteps().remove(index);
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            projectService.save(current);
            ProjectWriteModel projectWriteModel = new ProjectWriteModel();
            projectWriteModel.setDescription("test from controller");
            model.addAttribute("project", projectWriteModel);
            model.addAttribute("projects", getProjects());
            model.addAttribute("message", "Projekt został dodany");
        }
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        try {
            projectService.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grupę");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.findAll();
    }
}
