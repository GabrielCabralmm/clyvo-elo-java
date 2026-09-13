package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.model.Pet;
import com.clyvoelo.painel.repository.PetRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pets", petRepository.findAll());
        return "pets/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("pet", new Pet());
        return "pets/form";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petRepository.findById(id).orElseThrow());
        return "pets/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Pet pet, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "pets/form";
        }
        petRepository.save(pet);
        return "redirect:/pets";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        petRepository.deleteById(id);
        return "redirect:/pets";
    }
}
