package com.example.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

public class MsgController {

    MsgService msmessage;

    public MsgController(MsgService msgService){
        msmessage = msgService;
    }

    static HashMap<Integer, TempMessages> messages = new HashMap<>();

    @PostMapping("/mensageiro/publica/{CHAVE}")
    public String tempMessage(@PathVariable("CHAVE") Integer chave, @RequestParam String message, @RequestParam(defaultValue = "5") Integer vida, @RequestParam(defaultValue = "5") Integer acessos){

        TempMessages vars = new TempMessages(message, vida, acessos);
        messages.put(chave, vars);

        return messages.toString();
    }


    @GetMapping("/mensageiro/acessa/{CHAVE}")
    public String tempMessageAcess(@PathVariable("CHAVE") Integer chave) {
        TempMessages temps;
        try {
            temps = messages.get(chave);
        } catch (Error e) {
            throw new Error("Error com temps");
        }

        if(temps.getAcessos() == 0){
            return "Mensagem expirada";
        }

        temps.decrementarAcessos();
        return temps.getMessage();
    }
}
