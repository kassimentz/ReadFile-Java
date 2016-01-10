package com.ilegra.teste.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIlegra {

	public FileIlegra() {

	}

	public BufferedReader Read(String caminhoIn, String nomeArquivo) {

		BufferedReader br = null;
		try {
			Path file = Paths.get(caminhoIn, nomeArquivo);
			InputStream is = Files.newInputStream(file);
			br = new BufferedReader(new InputStreamReader(is));

		} catch (Exception e) {
			System.out.println("Arquivo não pode ser lido");
		}

		return br;
	}

}
