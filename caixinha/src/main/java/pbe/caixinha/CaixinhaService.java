package pbe.caixinha;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaixinhaService {

    ArrayList<BancoBens> bancoBensList = new ArrayList<>();


    public String bensPerdidosPost(String descricao, String data, String localPerdido, String contato){
        BancoBens addAoBanco = new BancoBens(descricao, data, localPerdido, contato);
        bancoBensList.add(addAoBanco);
        return "Descrição do Bem: " + descricao + " / data perdido: " + data + " / Local que foi perdido: " + localPerdido + " / Caso encontre, contatar: " + contato;
    }

    public String bensEncontradosPost(String descricao, String data, String localAchado, String contato){
        BancoBens addAoBanco = new BancoBens(descricao, data, localAchado, contato);
        bancoBensList.add(addAoBanco);
        return "Bem Encontrado com a seguinte descrição: " + descricao + " / Local achado: " + localAchado + " / Data achada: " + data + " / Se bate com algum item perdido, favor contatar: " + contato;
    }

    public String bensAchadosGet(){

        String list = "";
        for (int i = 0; i < bancoBensList.size(); i++) {
            list += bancoBensList.toString();
        }
        return list;
    }

}
