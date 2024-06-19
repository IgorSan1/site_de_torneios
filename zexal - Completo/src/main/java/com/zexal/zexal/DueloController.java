package com.zexal.zexal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zexal.repository.DueloRepository;
import com.zexal.repository.ParticipanteRepository;
import com.zexal.zexal.models.Duelo;
import com.zexal.zexal.models.Participante;


@Controller
public class DueloController {

    @Autowired
    private DueloRepository dueloRepository;

    @Autowired
    private ParticipanteRepository participanteRepository;

    @RequestMapping(value = "/cadastrarDuelo", method = RequestMethod.GET)
    public String form() {
        return "duelo/formDuelo";
    }

    @RequestMapping(value = "/cadastrarDuelo", method = RequestMethod.POST)
    public String form(@Valid Duelo duelo, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/cadastrarDuelo";
        }
        dueloRepository.save(duelo);
        attributes.addFlashAttribute("mensagem", "Duelo cadastrado com sucesso!");
        return "redirect:/cadastrarDuelo";
    }

    @RequestMapping("/duelos")
    public ModelAndView listarDuelo() {
        ModelAndView mv = new ModelAndView("/index");
        Iterable<Duelo> duelos= dueloRepository.findAll();
        mv.addObject("duelos", duelos);
        return mv;
    }

    @RequestMapping(value = "/detalhesDuelo/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesDuelo(@PathVariable("codigo") long codigo) {
        Duelo duelo = dueloRepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("duelo/detalhesDuelo");
        mv.addObject("duelo", duelo);

        Iterable<Participante> participantes = participanteRepository.findByDuelo(duelo);
        mv.addObject("participantes", participantes);

        return mv;
    }

    @RequestMapping(value = "/cadastrarParticipante/{codigo}", method = RequestMethod.POST)
    public String detalhesDueloPost(@PathVariable("codigo") long codigo, @Valid Participante participante, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/" + codigo;
        }
        Duelo duelo = dueloRepository.findByCodigo(codigo);
        participante.setDuelo(duelo);
        participanteRepository.save(participante);
        attributes.addFlashAttribute("mensagem", "Participante adicionado com sucesso!");
        return "redirect:/detalhesDuelo/" + codigo;
    }

    @GetMapping("/deletarDuelo")
    public String deletarDuelo(@RequestParam long codigo) {
        Duelo duelo = dueloRepository.findByCodigo(codigo);
        Iterable<Participante> participantes = participanteRepository.findByDuelo(duelo);
        participanteRepository.deleteAll(participantes);
        dueloRepository.delete(duelo);
        return "redirect:/duelos";
    }

    @GetMapping(value = "/deletarParticipante/{CPF}/{codigoDuelo}")
    public String deletarParticipante(@PathVariable("CPF") String CPF, @PathVariable("codigoDuelo") long codigoDuelo) {
        Participante participante = participanteRepository.findByCPF(CPF);
        participanteRepository.delete(participante);
        return "redirect:/detalhesDuelo/" + codigoDuelo;
    }

    @RequestMapping(value = "/editarDuelo/{codigo}", method = RequestMethod.GET)
    public ModelAndView editarDueloForm (@PathVariable("codigo") long codigo){
        Duelo duelo = dueloRepository.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("duelo/editarDuelo");
        mv.addObject("duelo", duelo);
        return mv;
    }

    @RequestMapping(value = "/editarDuelo/{codigo}", method = RequestMethod.POST)
    public String editarDuelo(@PathVariable("codigo") long codigo, @Valid Duelo duelo, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/editarDuelo" + codigo;
        }
        duelo.setCodigo(codigo);
        dueloRepository.save(duelo);
        attributes.addFlashAttribute("mensagem", "Duelo editado com sucesso!");
        return "redirect:/duelos";
    }
    
    @RequestMapping(value = "/editarParticipante/{CPF}", method = RequestMethod.GET)
    public ModelAndView editarParticipanteForm(@PathVariable("CPF") String CPF) {
        Participante participante = participanteRepository.findByCPF(CPF);
        ModelAndView mv = new ModelAndView("duelo/editarParticipante");
        mv.addObject("participante", participante);
        return mv;
    }

    @RequestMapping(value = "/editarParticipante/{CPF}", method = RequestMethod.POST)
    public String editarParticipante(@PathVariable("CPF") String CPF, @Valid Participante participante, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/editarParticipante/" + CPF;
        }
        
        Participante participanteExistente = participanteRepository.findByCPF(CPF);
        Duelo duelo = participanteExistente.getDuelo();
        
        participanteRepository.delete(participanteExistente);
        
        Participante novoParticipante = new Participante();
        novoParticipante.setCPF(participante.getCPF());
        novoParticipante.setNomeParticipante(participante.getNomeParticipante());
        novoParticipante.setDuelo(duelo);
        
        participanteRepository.save(novoParticipante);

        attributes.addFlashAttribute("mensagem", "Participante editado com sucesso!");
        return "redirect:/detalhesDuelo/" + duelo.getCodigo();
    }

}
