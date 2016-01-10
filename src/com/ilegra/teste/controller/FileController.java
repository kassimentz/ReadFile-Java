package com.ilegra.teste.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.ilegra.teste.modelo.Customer;
import com.ilegra.teste.modelo.Item;
import com.ilegra.teste.modelo.Sales;
import com.ilegra.teste.modelo.Salesman;
import com.ilegra.teste.service.FileService;

public class FileController {

	FileService fs;
	@SuppressWarnings("rawtypes")
	private List<Map> dados;

	public FileController() {
		fs = new FileService();

	}

	public void readFile(String file) {
		
		String extensao = file.split("\\.")[1];

		if (extensao.equalsIgnoreCase("dat")) {

			fs.readFile(file);
			String nome = file.split("\\.")[0];
			try {
				this.dados = this.readData();
				
			} catch (Exception e) {
				System.err.println("Problemas ao obter dados");
			}
			

			if (this.dados != null) {
				createFile("data/out", nome + ".done.dat", getAmountOfClients() + "\n" + getAmountOfSalesman() + "\n"
						+ getMostExpansiveSale() + "\n" + getWorstSalesmanEver());
			}

		} else {
			System.err.println("Extensão não suportada!");
		}

	}

	public void createFile(String caminho, String nome, String valor) {
		fs.createFile(caminho, nome, valor);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> readData() {

		String linha, cpf = null, vendedor = null, salario = null, cnpj, cliente, area, saleId, vendas, nomeVendedor;

		List<Map> maps = new ArrayList<>();

		try {
			while ((linha = fs.getDados().readLine()) != null) {

				Map<String, String> vendedoresMap = new HashMap<String, String>();
				Map<String, String> clientesMap = new HashMap<String, String>();
				Map<String, String> vendasMap = new HashMap<String, String>();

				StringTokenizer st = new StringTokenizer(linha, "ç");

				while (st.hasMoreElements()) {

					String op = st.nextElement().toString();

					if (op.equals("001")) {
						cpf = st.nextElement().toString();
						vendedor = st.nextElement().toString();
						salario = st.nextElement().toString();

						vendedoresMap.put("op", op);
						vendedoresMap.put("cpf", cpf);
						vendedoresMap.put("name", vendedor);
						vendedoresMap.put("salary", salario);

						maps.add(vendedoresMap);

					} else if (op.equals("002")) {
						cnpj = st.nextElement().toString();
						cliente = st.nextElement().toString();
						area = st.nextElement().toString();

						clientesMap.put("op", op);
						clientesMap.put("cnpj", cnpj);
						clientesMap.put("nome", cliente);
						clientesMap.put("businessArea", area);

						maps.add(clientesMap);

					} else if (op.equals("003")) {
						saleId = st.nextElement().toString();
						vendas = st.nextElement().toString();
						nomeVendedor = st.nextElement().toString();

						String itens = vendas.substring(1, vendas.length() - 1);
						// cada posicao do array gerado pelo split é um item que
						// deve ser adicionado ao arraylis de sales
						// System.out.println(itens.split(",")[0]);

						vendasMap.put("op", op);
						vendasMap.put("id", saleId);
						vendasMap.put("salesmanName", nomeVendedor);
						vendasMap.put("itens", itens);

						maps.add(vendasMap);
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return maps;
	}

	@SuppressWarnings("rawtypes")
	public List<Salesman> getSalesmans() {

		List<Salesman> salesmans = new ArrayList<Salesman>();
		List<Map> dados = this.dados;

		for (Map map : dados) {

			if (map.containsValue("001")) {

				Salesman s = new Salesman();
				s.setCpf(Long.valueOf(map.get("cpf").toString()));
				s.setName(map.get("name").toString());
				s.setSalary(Double.valueOf(map.get("salary").toString()));
				salesmans.add(s);

			}

		}

		return salesmans;

	}

	@SuppressWarnings("rawtypes")
	public List<Customer> getCustomers() {

		List<Customer> custumers = new ArrayList<Customer>();
		List<Map> dados = this.dados;

		for (Map map : dados) {

			if (map.containsValue("002")) {

				Customer c = new Customer();
				c.setCnpj(Long.valueOf(map.get("cnpj").toString()));
				c.setNome((map.get("nome").toString()));
				c.setBusinessArea(map.get("businessArea").toString());
				custumers.add(c);

			}
		}

		return custumers;

	}

	@SuppressWarnings("rawtypes")
	public List<Sales> getSales() {

		List<Sales> sales = new ArrayList<Sales>();
		List<Map> dados = this.dados;

		for (Map map : dados) {

			if (map.containsValue("003")) {

				List<Item> itens = this.getItens(map.get("itens").toString().split(","));
				Sales s = new Sales();
				s.setId(Integer.valueOf(map.get("id").toString()));
				s.setSalesman(findSalesmanByName(map.get("salesmanName").toString()));
				s.setItens(itens);
				sales.add(s);

			}
		}

		return sales;

	}

	public List<Item> getItens(String[] itens) {

		List<Item> items = new ArrayList<Item>();

		for (int i = 0; i < itens.length; i++) {

			String[] split = itens[i].split("-");
			Item it = new Item();
			it.setId(Integer.valueOf(split[0]));
			it.setQuantity(Integer.valueOf(split[1]));
			it.setPrice(Double.valueOf(split[2]));
			items.add(it);
		}

		return items;

	}

	// quantidade de clientes no arquivo
	public String getAmountOfClients() {
		return "Amout of Clients: " + this.getCustomers().size();

	}

	// quantidade de vendedores
	public String getAmountOfSalesman() {
		return "Amount of Salesman: " + this.getSalesmans().size();

	}

	// ID da venda mais cara - reunindo todos os arquivos?
	public String getMostExpansiveSale() {
		int id = 0;
		double vendaAnterior = 0;

		List<Sales> sales = this.getSales();
		for (Sales sales2 : sales) {

			List<Item> items = sales2.getItens();
			for (Item item : items) {

				if (item.getPrice() > vendaAnterior) {
					vendaAnterior = item.getPrice();
					id = sales2.getId();
				}
			}

		}

		return "Most Expansive Sale: " + id;

	}

	// pior vendedor de todos os tempos - reunindo todos os arquivos?
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getWorstSalesmanEver() {

		String vendedor = null;
		double valorVenda = 0;
		List<Map> vendas = new ArrayList<Map>();
		double totalVendas = 0;
		Map<String, String> venda = null;
		List<Sales> sales = this.getSales();
		for (Sales sales2 : sales) {

			venda = new HashMap<String, String>();
			List<Item> items = sales2.getItens();
			for (Item item : items) {
				// aqui é a soma de todas as vendas daquele vendedor

				valorVenda += item.getPrice() * item.getQuantity();

			}
			venda.put("nome", sales2.getSalesman().getName());
			venda.put("valor", String.valueOf(valorVenda));

			valorVenda = 0;
			vendas.add(venda);
		}

		String nome = "";
		for (Map sl : vendas) {
			// System.out.println(sl.toString());
			if (nome.equals(sl.get("nome"))) {
				totalVendas = totalVendas + Double.valueOf(sl.get("valor").toString());

			} else {
				totalVendas = Double.valueOf(sl.get("valor").toString());
				nome = sl.get("nome").toString();
			}
			sl.put("soma", totalVendas);

		}

		double soma2 = 0;
		for (Map sa : vendas) {
			if (!nome.equals(sa.get("nome")) && (soma2 < (double) sa.get("soma"))) {
				vendedor = sa.get("nome").toString();
			} else {
				soma2 = (double) sa.get("soma");
				nome = sa.get("nome").toString();

			}

		}

		return "Worst Salesman Ever: " + vendedor;

	}

	public Salesman findSalesmanByName(String nome) {
		Salesman s = null;
		List<Salesman> salesmans = getSalesmans();
		for (Salesman salesman : salesmans) {
			if (salesman.getName().equalsIgnoreCase(nome))
				s = salesman;

		}

		return s;

	}

}
