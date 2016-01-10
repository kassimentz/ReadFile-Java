package com.ilegra.teste.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ilegra.teste.file.FileIlegra;

public class FileService {

	static String caminhoIn = "data/in";
	static String nomeArquivo = "docTeste1.dat";
	

	private BufferedReader dados;

	FileIlegra file;

	public FileService() {

		file = new FileIlegra();
		
	}

	public void readFile(String arquivo){
		this.dados = file.Read(caminhoIn, arquivo);
	}
	
	public BufferedReader getDados() {
		return dados;
	}

	public void setDados(BufferedReader dados) {
		this.dados = dados;
	}

	public boolean createFile(String caminhoIn, String nomeArquivo, String texto) {
		System.out.println(texto);
		
		File file = new File(caminhoIn+"/"+nomeArquivo);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(texto);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;

	}

}
