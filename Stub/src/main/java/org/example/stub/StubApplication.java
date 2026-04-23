package org.example.stub;

import com.dataaccess.webservicesserver.NumberConversion;
import com.dataaccess.webservicesserver.NumberConversionSoapType;

import java.math.BigInteger;

public class StubApplication {

    public static void main(String[] args) {
        // instancia o acesso ao web service
        NumberConversion service = new NumberConversion();

        // instancia o serviço de conversão de números
        NumberConversionSoapType numberConversionSoapType = service.getNumberConversionSoap();

        // faz uma chamada remota ao procedimento de conversão de número
        String numberInWords = numberConversionSoapType.numberToWords(BigInteger.valueOf(1001));

        // mostra na tela o resultado
        System.out.println(numberInWords);
    }

}
