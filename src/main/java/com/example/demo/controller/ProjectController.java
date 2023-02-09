package com.example.demo.controller;

import com.example.demo.logic.ProjectService;
import com.example.demo.model.Project;
import com.example.demo.model.ProjectStep;
import com.example.demo.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model) {
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        model.addAttribute("project", projectWriteModel);
        return "projects";
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
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model ){
        projectService.save(current);
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        projectWriteModel.setDescription("test from controller");
        model.addAttribute("project", projectWriteModel);
        model.addAttribute("message", "Projekt został dodany");
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
       return projectService.findAll();
    }
}
