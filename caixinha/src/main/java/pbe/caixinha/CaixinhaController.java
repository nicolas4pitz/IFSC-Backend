package pbe.caixinha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;

@RestController
public class CaixinhaController {


    public record Bens(String descricao, String data, String localPerdido, String contato){}

    @Autowired
    CaixinhaService caixinhaService;

    @PostMapping("/perdidos")
    public String perdidos(@RequestBody Bens query){
        return caixinhaService.bensPerdidosPost(query.descricao(), query.data(), query.localPerdido(), query.contato());
    }

    @PostMapping("/encontrados")
    public String encontrados(@RequestBody Bens query){
        return caixinhaService.bensEncontradosPost(query.descricao(), query.data(), query.localPerdido(), query.contato());
    }

    //Achar os itens do banco
    @GetMapping("/itens")
    public String bensAchados(){

        return caixinhaService.bensAchadosGet();


    }

}
