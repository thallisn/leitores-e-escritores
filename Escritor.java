/* ***************************************************************
* Autor............: Thallis Luciano Curcino Nunes
* Matricula........: 202211065
* Inicio...........: 06/11/2023
* Ultima alteracao.: 06/11/2023
* Nome.............: Programacao Concorrente - Trabalho 04.
* Funcao...........: 
*
*************************************************************** */

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Escritor extends Thread {// Representa um escritor que tentar escrever a base de dados
  // ATRIBUTOS
  private int id;// numero correspondente ao escritor
  private final int OBTENDO_DADO = 0;// o escritor esta obtendo um dado (pensando em uma manchete)
  private final int ESCREVENDO = 1;// o escritor esta escrevendo na base de dados(escrevendo manchete)
  private final int AGUARDANDO = 2;// o escritor esta aguardando sua vez de escrever na base de dados
  private int tempoEscrevendo = 2000;// tempo que o escritor passa ESCREVENDO, inicialmente 2 seg
  private int tempoObtendoDado = 2000;// tempo que o escritor passa OBTENDO_DADO, inicialmente 2 seg
  private ImageView imgvEscritor;// camada de imagem que representa o escritor na tela
  private Image imgObtendoDado;// imagem que representa o escritor OBTENDO_DADO
  private Image imgEscrevendo;// imagem que representa o escritor ESCREVENDO
  private Image imgAguardando;// imagem que representa o escritor AGUARDANDO
  private ControleTelaPrincipal controle;// classe controladora da tela
  private BaseDeDados baseDeDados;// base de dados acessada pelo escritor
  private boolean isParado = false;// flag que sinaliza se a thread esta suspensa (TRUE) ou nao (FALSE)
  private boolean isInterrompido = false;// flag que sinaliza se a thread foi interrompida (TRUE) ou nao (FALSE)
  private String dadoObtido;// dado obtido pelo escritor (manchete)

  // CONTRUTOR
  public Escritor(ControleTelaPrincipal controle, BaseDeDados baseDeDados, int id) {
    super("Escritor " + id);// define o nome da Thread
    setDaemon(true);// a Thread finaliza ao fechar a janela

    this.id = id;// define o id do escritor
    this.controle = controle;// define a classe controladora
    this.baseDeDados = baseDeDados;// define base de dados

    this.imgvEscritor = this.controle.getImageView_Escritor(this.id);// define a camada de imagem do escritor

    // define as imagens que representarao o escritor
    this.imgObtendoDado = this.controle.getImagem_ObtendoDado();
    this.imgEscrevendo = this.controle.getImagem_Escrevendo();
    this.imgAguardando = this.controle.getImagem_Aguardando_Escritor();
  }// fim do construtor

  // METODOS
  @Override
  public void run() {// acoes realizadas ao iniciar a Thread
    while (!isInterrompido) {// loop enquanto a Thread nao for interrompida
      try {
        obterDado();// regiao nao critica
        this.baseDeDados.db.acquire();// obtem o acesso exclusivo a base de dados (DOWN)
        escreverNaBaseDeDados();// atualiza a base de dados
        this.baseDeDados.db.release();// libera o acesso exclusivo a base de dados (UP)
      } catch (InterruptedException exc) {// captura a excecao gerada ao interromper a Thread
        reiniciar();// redefine a imagem e valores de tempo do escritor
        isInterrompido = true;// sai do laco while do metodo run()(fim da Thread)
      } // fim do bloco try-catch
    } // fim do while
  }// fim do metodo run

  public void obterDado() throws InterruptedException {// define as acoes enquanto o escritor esta OBTENDO_DADO
    mudarImagem(OBTENDO_DADO);// atualiza a imagem
    pensarEmUmaManchete(id);// pensa em uma manchete (obtem Dado)
    try {
      sleep(this.tempoObtendoDado);// a Thread espera de acordo com tempo
    } catch (InterruptedException exc) {// captura a excecao ao interromper a Thread
      throw exc;// relacanca a execao para o metodo run()
    } // fim do bloco try-catch
    mudarImagem(AGUARDANDO);// o escritor terminou de obter dado (espera para poder escrever na base)
  }// fim do metodo obterDado

  public void escreverNaBaseDeDados() throws InterruptedException {
    mudarImagem(ESCREVENDO);// atualiza a imagem
    baseDeDados.setDado(dadoObtido);// escreve o dado obtido na base
    try {
      Thread.sleep(this.tempoEscrevendo);// a Thread espera de acordo com tempo
    } catch (InterruptedException exc) {// captura a excecao ao interromper a Thread
      throw exc;// relacanca a execao para o metodo run()
    } // fim do bloco try-catch
    mudarImagem(OBTENDO_DADO);// o escritor terminou de escrever na base(volta a obter dado)
  }// fim do metodo escreverNaBaseDeDados

  public synchronized void parar() {
    this.suspend();// supende a execucao da Thread
    isParado = true;// atualiza a flag
  }// fim do metodo parar

  public void retomar() {
    this.resume();// retoma a execucao da Thread
    isParado = false;// atualiza a flag
  }// fim do metodo parar

  public void reiniciar() {
    mudarImagem(OBTENDO_DADO);// volta a imagem inicial
    // atualiza as flags
    isParado = false;
    isInterrompido = false;
    // redefine os tempo em cada estado
    this.tempoEscrevendo = 2000;
    this.tempoObtendoDado = 2000;
  }// fim do metodo reiniciar

  public void mudarImagem(int estado) {// muda a imagem do escritor de acordo com o seu estado
    switch (estado) {
      case OBTENDO_DADO: {// escritor OBTENDO_DADO
        Platform.runLater(() -> {
          this.imgvEscritor.setImage(imgObtendoDado);
        });// fim das atualizacoes na interface grafica
        break;
      } // fim do case OBTENDO_DADO

      case ESCREVENDO: {// escritor ESCREVENDO
        Platform.runLater(() -> {
          this.imgvEscritor.setImage(imgEscrevendo);
        });// fim das atualizacoes na interface grafica
        break;
      } // fim do case

      case AGUARDANDO: {// escritor AGUARDANDO
        Platform.runLater(() -> {
          this.imgvEscritor.setImage(imgAguardando);
        });// fim das atualizacoes na interface grafica
        break;
      } // fim do case AGUARDANDO
    }// fim do switch (estado)
  }// fim do metodo mudarImagem

  private void pensarEmUmaManchete(int id) {// retorna a manchete que sera escrita de acordo com o "id"
    switch (id) {
      case 0: {// escritor00
        this.dadoObtido = "Pesquisadores afirmam que esquecimento eh comum, mas nao lembram por que";
        break;
      } // fim do case escritor00

      case 1: {// escritor01
        this.dadoObtido = "Urgente! Morador de rua eh condenado a prisao domiciliar em SP";
        break;
      } // fim do case escritor01

      case 2: {// escritor02
        this.dadoObtido = "Vidente preve que prever o futuro sera a profissao do futuro";
        break;
      } // fim do case escritor02

      case 3: {// escritor03
        this.dadoObtido = "Conferencia sobre procrastinacao eh adiada indefinidamente";
        break;
      } // fim do case escritor03

      case 4: {// escritor04
        this.dadoObtido = "Astronauta fica perdido no espaco, e reclama da falta de sinalizacao";
        break;
      } // fim do case escritor04
    }// fim do switch(id)
  }// fim do metodo pensarEmUmaManchete

  public void alterarTempoEscrevendo(int tempoEscrevendo) {
    this.tempoEscrevendo = (tempoEscrevendo) * 1000;// altera o numero de segundos que o escritor escreve na base
  }// fim do metodo alterarTempoEscrevendo

  public void alterarTempoObtendoDado(int tempoObtendoDado) {
    this.tempoObtendoDado = (tempoObtendoDado) * 1000;// altera o numero de segundos que o escrito obtem um dado
  }// fim do metodo alterarTempoObtendoDado

  public boolean isParado() {// retorna a info se a thread esta parada ou nao
    return isParado;
  }// fim do metodo isParado
}// fim da classe Escritor