package br.com.jzbreno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
		//settando minha senha diretamente
		generateHashedPassword();

	}
	private static void generateHashedPassword(){
		//(), COMPRIMENTO DA CHAVE, NUMERO DE VZS QUE VAI SER APLICADO, ALGORITMO DE HASH
		PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
				"", 8, 185000,
				Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		//
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", pbkdf2Encoder);
		// monta o id que vai buscar o encoder e insere a logica pbkdf montado anteriormente
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		//seta o encoder montado
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
		var pass1 = passwordEncoder.encode("admin123");
		var pass2 = passwordEncoder.encode("admin234");

		System.out.println("Senha Gerada: " + pass1);
		System.out.println("Senha Gerada: " + pass2);

	};


}
