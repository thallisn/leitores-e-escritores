/* ***************************************************************
* Autor............: Thallis Luciano Curcino Nunes
* Matricula........: 202211065
* Inicio...........: 06/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Programacao Concorrente - Trabalho 04.
* Funcao...........: Implementar a solucao para o classico problema
*                    "Leitores e Escritores" utilizando semaforos e
*                    uma GUI para representar o problema.
*************************************************************** */

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class BaseDeDados {// base de dados disputada pelas threads com acesso controlado (Semaforos)
  // ATRIBUTOS
  public Semaphore mutex;// semaforo para a exclusao mutua p/ regiao critica
  public Semaphore db;// semaforo que controla o acesso a base de dados
  public int rc = 0;// num de processos lendo ou tentando ler
  private String dado;// representacao do dado que sera lido ou sobescrito
  private Label infoBD;// camada de texto que exibe a informacao gravada na BD (letreiro)

  // CONSTRUTOR
  public BaseDeDados(Label infoBD) {// infoBD eh camada de texro onde a manchete sera exibido
    // INICIA OS SEMAFOROS PARA O CONTROLE DE ACESSO A RC (Base de dados)
    this.mutex = new Semaphore(1);// inicialmente livre
    this.db = new Semaphore(1); // inicialmente a base de dados esta livre
    this.rc = 0;// inicialmente nenhum processo esta lendo ou tentando ler a base de dados

    this.infoBD = infoBD;// inicializa a camada de texto
    this.dado = "Em breve: Manchetes 'quase' engracadas";// dado padrao (nada foi gravado na base)
    Platform.runLater(() -> this.infoBD.setText(this.dado));// exibe a mensagem que representa a base "vazia"
  }// fim do Construtor

  // METODOS
  public String getDado() {// retorna o dado que foi gravado na base para os Leitores
    return dado;// retorna o dado
  }// fim do metodo getDado

  public void setDado(String dado) {// dado eh o novo dado que sera escrito na base pelos Escritores
    this.dado = dado;// atualiza o dado da base
    Platform.runLater(() -> this.infoBD.setText(this.dado));// exibe o novo dado (manchete no letreiro)
  }// fim do metodo setDado

}// fim da classe BaseDeDados