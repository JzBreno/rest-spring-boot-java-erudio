package br.com.jzbreno.controllers;

import br.com.jzbreno.Exceptions.SumAritmeticException;
import br.com.jzbreno.model.MathRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {
//    PARAMNS PATH
//    http://localhost:8080/math/sum/3/5
//    http://localhost:8080/math/subtraction/3/5
//    http://localhost:8080/math/division/3/5

    @RequestMapping("/sum/{param1}/{param2}")
    public ResponseEntity<MathRecord> sum(@PathVariable(name = "param1") String param1, @PathVariable("param2") String param2){

        Double firstParam = Double.parseDouble(param1.replace(",",".")); //R$ 5,00 USD 5.0
        Double secondParam = Double.parseDouble(param2.replace(",","."));//R$ 5,00 USD 5.0
        MathRecord mathRecord = new MathRecord(firstParam + secondParam, "SUM");
        return ResponseEntity.ok().body(mathRecord);
    }

    @RequestMapping("/sub/{param1}/{param2}")
    public ResponseEntity<MathRecord> subtraction(@PathVariable(name = "param1") String param1, @PathVariable("param2") String param2){

        Double firstParam = Double.parseDouble(param1.replace(",",".")); //R$ 5,00 USD 5.0
        Double secondParam = Double.parseDouble(param2.replace(",","."));//R$ 5,00 USD 5.0
        MathRecord mathRecord = new MathRecord(firstParam - secondParam, "SUB");
        return ResponseEntity.ok().body(mathRecord);
    }

    @RequestMapping("/div/{param1}/{param2}")
    public ResponseEntity<MathRecord> division(@PathVariable(name = "param1") String param1, @PathVariable("param2") String param2){

        if(param2.matches("0")) throw new IllegalArgumentException("Impossivel dividir por zero");
        Double firstParam = Double.parseDouble(param1.replace(",",".")); //R$ 5,00 USD 5.0
        Double secondParam = Double.parseDouble(param2.replace(",","."));//R$ 5,00 USD 5.0
        MathRecord mathRecord = new MathRecord(firstParam / secondParam, "SUB");
        return ResponseEntity.ok().body(mathRecord);
    }



}
