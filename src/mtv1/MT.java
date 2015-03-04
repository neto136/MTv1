/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtv1;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author Neto
 */
public class MT {
    private Fita fita;
    private int passos;
    private boolean rejeitado;
    private String estadoAtual, estadoInicial, estadoAceitacao;
    private String parEstadoValor;
    private String entrada;
    private HashMap< String, List<String> > mapPrograma = new HashMap<>();  // <Estado+ValorLido, estadoSeguinte, valorNovo, movimentoCabeçote>

    public MT () {
        fita = new Fita();
        passos = 0;
        rejeitado = false;
        mapPrograma.clear();
    }
    
    public void setFita(String entrada) {
        fita.setEntrada(entrada);
        this.entrada = entrada;
    }
    
    public DoublyLinkedList<TipoIntString> getFita() {
        return fita.getFita();
    }
    
    public void moveFitaDireita() {
        fita.paraDireita();
    }
    
    public void moveFitaEsquerda() {
        fita.paraEsquerda();
    }
    
    public int parar() {
        if (mapPrograma.isEmpty())
            return 1;
        
        return 0;
    }
    
    public int reset() {
        if (mapPrograma.isEmpty())
            return 1;
        
        passos = 0;
        rejeitado = false;
        setFita(entrada);
        estadoAtual = estadoInicial;
        
        return 0;
    }
        
    public TipoResultado rodar() {
        TipoResultado resultado = passo();

        return resultado;
    }
    
    public TipoResultado passo() {
        if (mapPrograma.isEmpty() || rejeitado || estadoAtual.equals(estadoAceitacao))
            return null;
        
        int aceito = 0;
        TipoIntString campo = fita.getCampo();
        char dadoFita = campo.getDado();
        String concat = estadoAtual.trim() + dadoFita;
        
        
        List<String> values = new ArrayList<>();
        if (mapPrograma.containsKey(concat))        // Proteção contra chave inexistente
            values = mapPrograma.get(concat);
        
        if (values == null)
            rejeitado = true;
        else if (values.isEmpty())
            rejeitado = true;
        else {
            estadoAtual = values.get(0);
            fita.setCampo(values.get(1).charAt(0));
            switch (values.get(2)) {
                case ">":
                    fita.paraDireita();
                    break;
                case "<":
                    fita.paraEsquerda();
                    break;
            }
        }
        
        if (estadoAtual.equals(estadoAceitacao))
            aceito = 1;
        if (rejeitado)
            aceito = 2;
        
        passos++;
        return new TipoResultado(aceito, passos, estadoAtual);
    }
    
    public String compilar(String programa) {
        // Variáveis locais necessárias
        List<String> valores = new ArrayList<>();
        String estado;
        String valor;
        String estadoValor = new String();
        boolean leuCondicao = false;
        boolean leuTransicao = false;
        
        // Reinicia varáveis globais
        mapPrograma.clear();
        passos = 0;
        rejeitado = false;
        
        
        // Verifica se há dados no campo para validação
        if (programa.equals("")) {
            return "Necessário inserir um programa no campo";
        }
        
        // Divide as entradas por linhas
        String[] linhas = programa.split("\\r?\\n");
        
        
        // Varredura linha-a-linha para tratamento
        for (String linha : linhas) {
            // Ignora linhas vazias ou comentários
            if (linha.startsWith("//") || linha.equals(" ") || linha.equals(""))
                continue;
            
            // Procura configuração de estado inicial e estado de aceitação
            String dados[] = linha.trim().split(":");
            if (dados.length == 2) {
                switch (dados[0]) {
                    case "init":
                        estadoInicial = dados[1].trim();
                        estadoAtual = estadoInicial;
                        break;
                    case "accept":
                        estadoAceitacao = dados[1].trim();
                        break;
                }
                continue;
            }
            
            
            // Procura configurações de condição e transição
            String condicao[] = linha.trim().split(",");
            
            if (condicao.length < 2)
                continue;
            
            if (condicao.length == 2 && !leuCondicao) {
                estado = condicao[0];
                valor = condicao[1];
                //estadoValor = new String();
                estadoValor = estado + valor;
                leuCondicao = true;
            } else if (condicao.length == 3 && leuCondicao) {
                if (!condicao[2].equals("<") && !condicao[2].equals(">") && !condicao[2].equals("-"))
                    return "A movimentação do cabeçote deve ser uma das três:\n" +
                           "> mover para direita\n" +
                           "< mover para esquerda\n" +
                           "- não mover";
                valores.clear();
                valores.add(condicao[0]);
                valores.add(condicao[1]);
                valores.add(condicao[2]);
                leuTransicao = true;
            } else {
                return "Favor seguir a regra de escrita do programa. Ex:\n\n" +
                       "estadoCondicao , valorCondicao\n"+
                       "estadoSeguinte , valorNovo , movimentoCabeçote";
            }
            
            if (leuTransicao && leuCondicao) {
                leuCondicao = false;
                leuTransicao = false;
                mapPrograma.put( estadoValor, new ArrayList<>(valores));
            }
        }
        
        
        // Verifica se as configurações iniciais foram feitas
        if (estadoInicial == null || estadoAceitacao == null)
            return "Necessário definir estado inicial e de aceitação. Ex:\n\n'init : qInicial'\n'accept : qAceitação'";
        else if (estadoInicial.equals("") || estadoAceitacao.equals(""))
            return "Necessário definir estado inicial e de aceitação. Ex:\n\n'init : qInicial'\n'accept : qAceitação'";
        
        // Verifica se leu o par condição-transição
        if (leuCondicao && !leuTransicao)
            return "Favor seguir a regra de escrita do programa. Ex:\n\n" +
                   "estadoCondicao , valorCondicao\n"+
                   "estadoSeguinte , valorNovo , movimentoCabeçote";
        
        // Verifica se foi digitada alguma condifuração de transições
        if (mapPrograma.isEmpty())
            return "Não há transições definidas.";

        
        return ":" + estadoInicial;
    }
    
}
