package com.cfpi.aplicacao.servicos;

import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.apresentacao.shell.Tela;
import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.banco.BancoStoreImpl;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.CDB;
import com.cfpi.dominio.entidades.investimento.CRA;
import com.cfpi.dominio.entidades.investimento.CRI;
import com.cfpi.dominio.entidades.investimento.Cripto;
import com.cfpi.dominio.entidades.investimento.DEB;
import com.cfpi.dominio.entidades.investimento.FII;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.investimento.LCA;
import com.cfpi.dominio.entidades.investimento.LCI;
import com.cfpi.dominio.entidades.investimento.PGBL;
import com.cfpi.dominio.entidades.investimento.TesouroDireto;
import com.cfpi.dominio.entidades.investimento.VGBL;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportExportServico {

    // -------------------------------------------------------------------------
    // Export
    // -------------------------------------------------------------------------

    public void exportar(AppSession sessao) {
        if (sessao.getUsuarioAtual() == null) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum usuário logado para exportar.",
                    "Exportar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));
        chooser.setSelectedFile(new File("cfpi_export.json"));

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File arquivo = chooser.getSelectedFile();
        if (!arquivo.getName().endsWith(".json")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".json");
        }

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8))) {
            pw.print(construirJson(sessao));
            JOptionPane.showMessageDialog(null,
                    "Dados exportados com sucesso!",
                    "Exportar", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao exportar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String construirJson(AppSession sessao) {
        Usuario usuario = sessao.getUsuarioAtual();
        BancoStoreImpl bancoStore = sessao.getBancoStore();

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // usuario
        sb.append("  \"usuario\": {\n");
        sb.append("    \"nome\": ").append(jsonString(usuario.getNome())).append(",\n");
        sb.append("    \"cpf\": ").append(jsonString(usuario.getCpf())).append(",\n");
        sb.append("    \"telefone\": ").append(jsonString(usuario.getTelefone())).append(",\n");
        sb.append("    \"dataNascimento\": ").append(jsonString(usuario.getDataNascimento())).append("\n");
        sb.append("  },\n");

        // bancos
        sb.append("  \"bancos\": [");
        if (bancoStore != null) {
            Banco[] bancos = bancoStore.getBancos();
            boolean primeiroBanco = true;
            for (Banco banco : bancos) {
                if (banco == null) break;
                if (!primeiroBanco) sb.append(",");
                sb.append("\n    {\n");
                sb.append("      \"id\": ").append(banco.getId()).append(",\n");
                sb.append("      \"nome\": ").append(jsonString(banco.getNome())).append(",\n");
                sb.append("      \"codigo\": ").append(banco.getCodigo()).append("\n");
                sb.append("    }");
                primeiroBanco = false;
            }
            if (!primeiroBanco) sb.append("\n  ");
        }
        sb.append("],\n");

        // contas
        sb.append("  \"contas\": [");
        Conta[] contas = usuario.getContas();
        boolean primeiraConta = true;
        for (Conta conta : contas) {
            if (conta == null) break;
            if (!primeiraConta) sb.append(",");
            sb.append("\n    {\n");
            sb.append("      \"id\": ").append(conta.getId()).append(",\n");
            sb.append("      \"tipo\": ").append(jsonString(conta.getTipo())).append(",\n");
            sb.append("      \"valorConta\": ").append(conta.getValorConta()).append(",\n");
            sb.append("      \"numeroConta\": ").append(jsonString(conta.getNumeroConta())).append(",\n");
            sb.append("      \"moeda\": ").append(jsonString(conta.getMoeda())).append(",\n");
            sb.append("      \"bancoId\": ").append(conta.getBanco() != null ? conta.getBanco().getId() : -1).append(",\n");
            sb.append("      \"limiteCredito\": ").append(conta.getLimiteCredito()).append(",\n");
            sb.append("      \"limiteCreditoUtilizado\": ").append(conta.getLimiteCreditoUtilizado()).append(",\n");

            // transacoes
            sb.append("      \"transacoes\": [");
            Transacao[] transacoes = conta.getTransacoes();
            boolean primeiraTransacao = true;
            for (Transacao t : transacoes) {
                if (t == null) break;
                if (!primeiraTransacao) sb.append(",");
                sb.append("\n        {\n");
                boolean ehDebito = t instanceof Debito;
                sb.append("          \"tipoClasse\": ").append(jsonString(ehDebito ? "Debito" : "Credito")).append(",\n");
                sb.append("          \"descricao\": ").append(jsonString(t.getDescricao())).append(",\n");
                sb.append("          \"data\": ").append(jsonString(t.getData())).append(",\n");
                sb.append("          \"valor\": ").append(t.getValor()).append(",\n");
                sb.append("          \"categoria\": ").append(jsonString(t.getCategoria()));
                if (ehDebito) {
                    sb.append(",\n          \"tipo\": ").append(jsonString(((Debito) t).getTipo()));
                }
                sb.append("\n        }");
                primeiraTransacao = false;
            }
            if (!primeiraTransacao) sb.append("\n      ");
            sb.append("],\n");

            // investimentos
            sb.append("      \"investimentos\": [");
            Investimento[] investimentos = conta.getInvestimentos();
            boolean primeiroInvestimento = true;
            for (Investimento inv : investimentos) {
                if (inv == null) break;
                if (!primeiroInvestimento) sb.append(",");
                sb.append("\n        {\n");
                sb.append("          \"tipoClasse\": ").append(jsonString(inv.getClass().getSimpleName())).append(",\n");
                sb.append("          \"nomeAtivo\": ").append(jsonString(inv.getNomeAtivo())).append(",\n");
                sb.append("          \"valor\": ").append(inv.getValor()).append(",\n");
                sb.append("          \"quantidade\": ").append(inv.getQuantidade()).append(",\n");
                sb.append("          \"imposto\": ").append(inv.getImposto()).append(",\n");
                sb.append("          \"data\": ").append(jsonString(inv.getData())).append(",\n");
                sb.append("          \"valorRealizado\": ").append(inv.getValorRealizado()).append(",\n");
                sb.append("          \"operacao\": ").append(jsonString(inv.getOperacao())).append("\n");
                sb.append("        }");
                primeiroInvestimento = false;
            }
            if (!primeiroInvestimento) sb.append("\n      ");
            sb.append("]\n    }");
            primeiraConta = false;
        }
        if (!primeiraConta) sb.append("\n  ");
        sb.append("],\n");

        // objetivos
        sb.append("  \"objetivos\": [");
        Objetivo[] objetivos = usuario.getObjetivos();
        boolean primeiroObjetivo = true;
        for (Objetivo obj : objetivos) {
            if (obj == null) break;
            if (!primeiroObjetivo) sb.append(",");
            sb.append("\n    {\n");
            sb.append("      \"nome\": ").append(jsonString(obj.getNome())).append(",\n");
            sb.append("      \"valor\": ").append(obj.getValor()).append("\n");
            sb.append("    }");
            primeiroObjetivo = false;
        }
        if (!primeiroObjetivo) sb.append("\n  ");
        sb.append("]\n}");

        return sb.toString();
    }

    private String jsonString(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:   sb.append(c);      break;
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Import
    // -------------------------------------------------------------------------

    public void importar(AppSession sessao, Consumer<Tela> navegador) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(chooser.getSelectedFile().toPath()), StandardCharsets.UTF_8);
            carregarDoJson(json, sessao);
            navegador.accept(Tela.DASHBOARD);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao ler o arquivo: " + e.getMessage(),
                    "Erro ao importar", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo inválido ou corrompido: " + e.getMessage(),
                    "Erro ao importar", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoJson(String json, AppSession sessao) {
        Map<String, Object> root = (Map<String, Object>) parseJson(json);

        // 1. Criar usuario
        Map<String, Object> usuarioMap = (Map<String, Object>) root.get("usuario");
        Usuario usuario = new Usuario(
                (String) usuarioMap.get("nome"),
                (String) usuarioMap.get("cpf"),
                (String) usuarioMap.get("telefone"),
                (String) usuarioMap.get("dataNascimento")
        );

        // 2. Criar bancos
        BancoStoreImpl bancoStore = new BancoStoreImpl(usuario);
        Map<Integer, Banco> bancosPorId = new HashMap<>();
        List<Object> bancosList = (List<Object>) root.get("bancos");
        for (Object bancoObj : bancosList) {
            Map<String, Object> bancoMap = (Map<String, Object>) bancoObj;
            int exportedId = ((Double) bancoMap.get("id")).intValue();
            String nome = (String) bancoMap.get("nome");
            int codigo = ((Double) bancoMap.get("codigo")).intValue();
            Banco banco = new Banco(nome, codigo);
            bancoStore.inserir(banco);
            bancosPorId.put(exportedId, banco);
        }

        // 3. Criar contas com transacoes e investimentos
        List<Object> contasList = (List<Object>) root.get("contas");
        for (Object contaObj : contasList) {
            Map<String, Object> contaMap = (Map<String, Object>) contaObj;

            String tipo = (String) contaMap.get("tipo");
            double valorConta = (Double) contaMap.get("valorConta");
            String numeroConta = (String) contaMap.get("numeroConta");
            String moeda = (String) contaMap.get("moeda");
            int bancoId = ((Double) contaMap.get("bancoId")).intValue();
            double limiteCredito = (Double) contaMap.get("limiteCredito");
            double limiteCreditoUtilizado = (Double) contaMap.get("limiteCreditoUtilizado");

            Banco banco = bancoId == -1 ? null : bancosPorId.get(bancoId);

            Conta conta = new Conta();
            conta.setTipo(tipo);
            conta.ajustarValorConta(valorConta);
            conta.setNumeroConta(numeroConta);
            conta.setMoeda(moeda);
            conta.setBanco(banco);
            conta.setUsuario(usuario);
            conta.setLimiteCredito(limiteCredito);
            conta.setLimiteCreditoUtilizado(limiteCreditoUtilizado);
            usuario.adicionarConta(conta);

            List<Object> transacoesList = (List<Object>) contaMap.get("transacoes");
            for (Object transacaoObj : transacoesList) {
                Map<String, Object> tMap = (Map<String, Object>) transacaoObj;
                String tipoClasse = (String) tMap.get("tipoClasse");
                String descricao = (String) tMap.get("descricao");
                String data = (String) tMap.get("data");
                double valor = (Double) tMap.get("valor");
                String categoria = (String) tMap.get("categoria");

                Transacao transacao;
                if ("Debito".equals(tipoClasse)) {
                    Debito debito = new Debito();
                    debito.setDescricao(descricao);
                    debito.setConta(conta);
                    debito.setData(data);
                    debito.setValor(valor);
                    debito.setCategoria(categoria);
                    debito.setTipo((String) tMap.get("tipo"));
                    transacao = debito;
                } else {
                    Credito credito = new Credito();
                    credito.setDescricao(descricao);
                    credito.setConta(conta);
                    credito.setData(data);
                    credito.setValor(valor);
                    credito.setCategoria(categoria);
                    transacao = credito;
                }
                conta.adicionarTransacao(transacao);
            }

            List<Object> investimentosList = (List<Object>) contaMap.get("investimentos");
            for (Object investimentoObj : investimentosList) {
                Map<String, Object> iMap = (Map<String, Object>) investimentoObj;
                String tipoClasse = (String) iMap.get("tipoClasse");
                String nomeAtivo = (String) iMap.get("nomeAtivo");
                double valor = (Double) iMap.get("valor");
                double quantidade = (Double) iMap.get("quantidade");
                double imposto = (Double) iMap.get("imposto");
                String data = (String) iMap.get("data");
                double valorRealizado = (Double) iMap.get("valorRealizado");
                String operacao = (String) iMap.get("operacao");

                Investimento investimento = criarInvestimento(tipoClasse);
                investimento.setNomeAtivo(nomeAtivo);
                investimento.setValor(valor);
                investimento.setConta(conta);
                investimento.setQuantidade(quantidade);
                investimento.setImposto(imposto);
                investimento.setData(data);
                investimento.setValorRealizado(valorRealizado);
                investimento.setOperacao(operacao);
                conta.adicionarInvestimento(investimento);
            }
        }

        // 4. Criar objetivos
        List<Object> objetivosList = (List<Object>) root.get("objetivos");
        for (Object objetivoObj : objetivosList) {
            Map<String, Object> oMap = (Map<String, Object>) objetivoObj;
            String nome = (String) oMap.get("nome");
            double valor = (Double) oMap.get("valor");

            Objetivo objetivo = new Objetivo();
            objetivo.setNome(nome);
            objetivo.setValor(valor);
            objetivo.setUsuario(usuario);
            usuario.adicionarObjetivo(objetivo);
        }

        sessao.setUsuarioAtual(usuario);
        sessao.setBancoStore(bancoStore);
    }

    private Investimento criarInvestimento(String tipoClasse) {
        switch (tipoClasse) {
            case "Acao":         return new Acao();
            case "CDB":          return new CDB();
            case "CRA":          return new CRA();
            case "CRI":          return new CRI();
            case "Cripto":       return new Cripto();
            case "DEB":          return new DEB();
            case "FII":          return new FII();
            case "LCA":          return new LCA();
            case "LCI":          return new LCI();
            case "PGBL":         return new PGBL();
            case "TesouroDireto": return new TesouroDireto();
            case "VGBL":         return new VGBL();
            default: throw new IllegalArgumentException("Tipo de investimento desconhecido: " + tipoClasse);
        }
    }

    // -------------------------------------------------------------------------
    // JSON parser simples (sem dependências externas)
    // -------------------------------------------------------------------------

    private static Object parseJson(String json) {
        return new JsonParser(json.trim()).parse();
    }

    private static class JsonParser {

        private final String s;
        private int pos = 0;

        JsonParser(String s) {
            this.s = s;
        }

        Object parse() {
            skipWhitespace();
            char c = s.charAt(pos);
            if (c == '{') return parseObject();
            if (c == '[') return parseArray();
            if (c == '"') return parseString();
            if (c == 't' || c == 'f') return parseBoolean();
            if (c == 'n') { pos += 4; return null; }
            return parseNumber();
        }

        Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            pos++; // '{'
            skipWhitespace();
            if (s.charAt(pos) == '}') { pos++; return map; }
            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                pos++; // ':'
                skipWhitespace();
                Object value = parse();
                map.put(key, value);
                skipWhitespace();
                char next = s.charAt(pos);
                if (next == '}') { pos++; return map; }
                pos++; // ','
            }
        }

        List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            pos++; // '['
            skipWhitespace();
            if (s.charAt(pos) == ']') { pos++; return list; }
            while (true) {
                skipWhitespace();
                list.add(parse());
                skipWhitespace();
                char next = s.charAt(pos);
                if (next == ']') { pos++; return list; }
                pos++; // ','
            }
        }

        String parseString() {
            pos++; // '"'
            StringBuilder sb = new StringBuilder();
            while (pos < s.length() && s.charAt(pos) != '"') {
                char c = s.charAt(pos);
                if (c == '\\') {
                    pos++;
                    char esc = s.charAt(pos);
                    switch (esc) {
                        case '"':  sb.append('"');  break;
                        case '\\': sb.append('\\'); break;
                        case '/':  sb.append('/');  break;
                        case 'n':  sb.append('\n'); break;
                        case 'r':  sb.append('\r'); break;
                        case 't':  sb.append('\t'); break;
                        default:   sb.append(esc);  break;
                    }
                } else {
                    sb.append(c);
                }
                pos++;
            }
            pos++; // '"'
            return sb.toString();
        }

        boolean parseBoolean() {
            if (s.startsWith("true", pos)) { pos += 4; return true; }
            pos += 5;
            return false;
        }

        double parseNumber() {
            int start = pos;
            while (pos < s.length()) {
                char c = s.charAt(pos);
                if (Character.isDigit(c) || c == '.' || c == '-' || c == 'E' || c == 'e' || c == '+') {
                    pos++;
                } else {
                    break;
                }
            }
            return Double.parseDouble(s.substring(start, pos));
        }

        void skipWhitespace() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
                pos++;
            }
        }
    }
}
