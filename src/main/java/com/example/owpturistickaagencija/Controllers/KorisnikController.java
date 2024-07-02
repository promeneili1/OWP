package com.example.owpturistickaagencija.Controllers;

import com.example.owpturistickaagencija.Exceptions.UserNotFoundException;
import com.example.owpturistickaagencija.Models.Korisnik;
import com.example.owpturistickaagencija.Models.Kupac;
import com.example.owpturistickaagencija.Models.Rezervacija;
import com.example.owpturistickaagencija.Models.Uloga;
import com.example.owpturistickaagencija.Services.KorisnikService;
import com.example.owpturistickaagencija.Services.KupacService;
import com.example.owpturistickaagencija.Services.RezervacijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class KorisnikController {

    @Autowired
    private KorisnikService korisnikService;
    @Autowired
    private KupacService kupacService;
    @Autowired
    private RezervacijaService rezervacijaService;


    @GetMapping("/registracija")
    public String showRegistracija(Model model){
        model.addAttribute("naslov", "Registruj se");
        model.addAttribute("korisnik", new Korisnik());
        model.addAttribute("method", "/korisnici/save");

        return "Registracija";
    }

    @GetMapping("/korisnici") // link na koji idu euprava/korisnici
    public String showKorisniciList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<Korisnik> listKorisnici = korisnikService.findAll();
        model.addAttribute("test", listKorisnici); // prosledjivanje svih korisnika

        Cookie[] cookies = request.getCookies();
        if(korisnikService.checkCookies(cookies, Uloga.ADMINISTRATOR)){
            return "korisnici";
        }

        return "odbijen_pristup"; // stranica koju vraca
    }
    @GetMapping("/korisnici/{src}")
    public String showNewForm(@PathVariable("src") String src, Model model){
        if(src.equals("new")){
            model.addAttribute("korisnik", new Korisnik());
            model.addAttribute("redirect","/turistickaAgencija/korisnici");
        } else if (src.equals("registracija")){
            model.addAttribute("naslov", "Registruj se");
            model.addAttribute("redirect", "/turistickaAgencija/");
        }

        model.addAttribute("korisnik", new Korisnik());
        model.addAttribute("method", "/korisnici/save");

        return "dodaj_korisnika";
    }

    @PostMapping("/korisnici/save")
    public String saveUser(Korisnik korisnik, RedirectAttributes ra) throws UserNotFoundException {
        korisnikService.save(korisnik);
        Korisnik novKorisnik = korisnikService.findKorisnikByEmail(korisnik.getEmailAdresa());

        if(korisnik.getUloga() == Uloga.KUPAC){
            Kupac kupac = new Kupac(novKorisnik);
            kupacService.save(kupac);

        }
        ra.addFlashAttribute("message", "Korisnik je sacuvan");
        return "redirect:/index";
    }

    @PostMapping("/korisnici/update")
    public String updateUser(Korisnik user, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        Korisnik stariKorisnik = korisnikService.get(user.getId());

        if (newPassword != null && !newPassword.isEmpty()) {
            if (newPassword.equals(confirmPassword)) {
                user.setLozinka(newPassword);
            } else {
                ra.addFlashAttribute("message", "Lozinke se ne poklapaju");
                return "redirect:/korisnici/edit/" + user.getId();
            }
        } else {
            user.setLozinka(stariKorisnik.getLozinka());
        }

        user.setUloga(stariKorisnik.getUloga());
        korisnikService.update(user);
        ra.addFlashAttribute("message", "Korisnik je izmenjen");
        return "redirect:/profil";
    }

    @GetMapping("/korisnici/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try{
            Korisnik korisnik = korisnikService.findOne(id);
            model.addAttribute("korisnik", korisnik);
            model.addAttribute("method", "/korisnici/update");
            model.addAttribute("naslov", "Izmeni Korisnika (Email:" + korisnik.getEmailAdresa() + ")");
            return "dodaj_korisnika";
        } catch (UserNotFoundException exception){
            ra.addFlashAttribute("message", "Korisnik nije izmenjen");
            return "redirect:/profile";
        }
    }

    @GetMapping("/korisnici/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra){
        korisnikService.delete(id);
        kupacService.delete(id);
        ra.addFlashAttribute("message", "Korisnik je obrisan!");
        return "redirect:/korisnici";
    }


    @GetMapping("/profil")
    public String showProfile(Model model, HttpServletRequest httpServletRequest) throws UserNotFoundException {
        Cookie[] cookies = httpServletRequest.getCookies();
        Korisnik korisnik = korisnikService.checkCookieUser(cookies);
        model.addAttribute("korisnik", korisnik);

        if(korisnik.getUloga().equals(Uloga.ADMINISTRATOR)){
            model.addAttribute("uloga", "ADMINISTRATOR");
        }
        else if(korisnik.getUloga().equals(Uloga.MENADZER)){
            model.addAttribute("uloga", "MENADZER");
        }
        else if(korisnik.getUloga().equals(Uloga.KUPAC)){
                model.addAttribute("uloga", "KUPAC");
                // Fetch only active reservations for the logged-in KUPAC
                List<Rezervacija> listAktivneRezervacije = rezervacijaService.getAktivneRezervacijeByKupacId(korisnik.getId());
                model.addAttribute("listAktivneRezervacije", listAktivneRezervacije);

                List<Rezervacija> listapotvrdjene = rezervacijaService.getAktivneRezervacijeByKupacId(korisnik.getId());
                model.addAttribute("listPotvrdjeneRezervacije", listapotvrdjene);
        }


        return "profil";
    }
}

