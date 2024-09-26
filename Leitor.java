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

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Leitor extends Thread {// representa um leitor que tenta ler a info da base dados
  // ATRIBUTOS
  private int id;// numero correspondente ao leitor
  private final int LENDO = 0;// o leitor esta lendo na base de dados
  private final int UTILIZANDO_DADO = 1;// o leitor esta utilizando o dado lido
  private final int AGUARDANDO = 2;// o leitor esta aguardando a sua vez
  private int tempoLendo = 2000;// tempo que o leitor passa LENDO
  private int tempoUtilizandoDado = 2000;// tempom que o leitor passa UTILIZANDO_DADO
  private ImageView imgvLeitor;// camada de imagem que representa o leitor
  private Image imgLendo;// imagem que representa o leitor LENDO
  private Image imgUtilizandoDado;// imagem que representa o leitor UTILIZANDO_DADO
  private Image imgAguardando;// imagem que representa o leitor AGUARDANDO
  private ControleTelaPrincipal controle;// classe controladora da tela
  private BaseDeDados baseDeDados;// base de dados acessada pelo leitor
  private boolean isParado = false;// flag que sinaliza se a thread esta suspensa (TRUE) ou nao (FALSE)
  private boolean isInterrompido = false;// flag que sinaliza se a thread foi interrompida (TRUE) ou nao (FALSE)
  private String dadoLido;// manchete lida pelo leitor
  private Label comentario;// comentario feito pelo leitor apos ler a manchete

  // CONSTRUTOR
  public Leitor(ControleTelaPrincipal controle, BaseDeDados baseDeDados, int id) {
    super("Leitor " + id);// define o nome da Thread
    setDaemon(true);// a Thread finaliza ao fechar a janela

    this.id = id;// define o id do escritor
    this.controle = controle;// define a classe controladora
    this.baseDeDados = baseDeDados;// define base de dados

    this.imgvLeitor = this.controle.getImageView_Leitor(this.id);// define a camada de imagem

    // define as imagens que representam o leitor em cada estado
    this.imgLendo = this.controle.getImagem_Lendo();
    this.imgUtilizandoDado = this.controle.getImagem_UtilizandoDado();
    this.imgAguardando = this.controle.getImagem_Aguardando_Leitor();

    this.comentario = this.controle.getLabel_Comentario(id);// define a camada de texto que exibe o comentario do leitor
  }// fim do Construtor

  // METODOS
  @Override
  public void run() {// acoes tomadas ao iniciar a thread
    mudarImagem(AGUARDANDO);// inicialmente o leitor aguarda
    while (!isInterrompido) {// enquanto a thread nao for interrompida
      try {
        this.baseDeDados.mutex.acquire();// obtem o acesso a RC (DOWN)
        this.baseDeDados.rc += 1; // informa que tem mais um leitor
        if (this.baseDeDados.rc == 1) {// Se este for o primeiro leitor...
          this.baseDeDados.db.acquire();// obtem o acesso exclusivo a base de dados (DOWN)
        } // fim do if
        this.baseDeDados.mutex.release();// libera o acesso a RC (UP)
        lerBaseDeDados();// faz a leitura dos dados na base de dado
        this.baseDeDados.mutex.acquire();// obtem o acesso a RC (DOWN)
        this.baseDeDados.rc -= 1;// informa que tem um leitor a menos
        if (this.baseDeDados.rc == 0) {// Se este for o ultimo leitor...
          this.baseDeDados.db.release();// libera o acesso exclusivo a base de dados (DOWN)
        } // fim do if
        this.baseDeDados.mutex.release();// libera o acesso a RC (UP)
        utilizarDadoLido();// regiao nao critica
      } catch (InterruptedException exc) {// captura a excecao gerada ao interromper a Thread
        reiniciar();// redefine a imagem e valores de tempo do leitor
        isInterrompido = true;// sai do laco while do metodo run()(fim da Thread)
      } // fim do bloco try-catch
    } // fim do while
  }// fim do metodo run

  public void lerBaseDeDados() throws InterruptedException {
    mudarImagem(LENDO);// atualiza a imagem
    this.dadoLido = baseDeDados.getDado();// le a manchete (acessa a base de dados)
    try {
      sleep(this.tempoLendo);// a Thread espera de acordo com o tempo
    } catch (InterruptedException exc) {// captura a excecao ao interromper a Thread
      throw exc;// relacanca a execao para o metodo run()
    } // fim do bloco try-catch
  }// fim do metodo lerBaseDeDados

  public void utilizarDadoLido() throws InterruptedException {
    mudarImagem(UTILIZANDO_DADO);// muda a imagem
    comentarManchete(this.id);// faz um comentario de acordo com a manchete lida (utilizar dado)
    try {
      sleep(this.tempoUtilizandoDado);// a Thread espera de acordo com o tempo
    } catch (InterruptedException exc) {// captura a excecao ao interromper a Thread
      throw exc;// relacanca a execao para o metodo run()
    } // fim do bloco try-catch
    mudarImagem(AGUARDANDO);// terminou de utilizar o dado, volta a esperar a sua vez de ler novamente
  }// fim do metodo utilizarDadoLido

  public synchronized void parar() {
    this.suspend();// supende a execucao da Thread
    isParado = true;// atualiza a flag
  }// fim do metodo parar

  public void retomar() {
    this.resume();// retoma a execucao da Thread
    isParado = false;// atualiza a flag
  }// fim do metodo parar

  public void reiniciar() {
    mudarImagem(AGUARDANDO);// volta a imagem inicial
    // atualiza as flags
    isParado = false;
    isInterrompido = false;
    // redefine os tempo em cada estado
    this.tempoLendo = 2000;
    this.tempoUtilizandoDado = 2000;
    // oculta os comentarios
    Platform.runLater(() -> {
      this.comentario.setVisible(false);
    });
    this.dadoLido = null;// redefine a manchete lida
  }// fim do metodo reiniciar

  public void mudarImagem(int estado) {// muda a imagem do leitor de acordo com seu estado atual
    switch (estado) {
      case LENDO: {// lendo
        Platform.runLater(() -> {
          this.imgvLeitor.setImage(imgLendo);
        });
        break;
      }

      case UTILIZANDO_DADO: {// utilizando dado
        Platform.runLater(() -> {
          this.imgvLeitor.setImage(imgUtilizandoDado);
        });
        break;
      }

      case AGUARDANDO: {// aguardando
        Platform.runLater(() -> {
          this.imgvLeitor.setImage(imgAguardando);
          this.comentario.setVisible(false);
        });
        break;
      }
    }
  }// fim do metodo mudarImagem

  private void comentarManchete(int id) {// cada leitor faz seu comentario de acordo com a manchete
    switch (this.dadoLido) {
      case "Pesquisadores afirmam que esquecimento eh comum, mas nao lembram por que": {
        switch (id) {
          case 0: {
            Platform.runLater(() -> this.comentario.setText("Leitor 1: De fato uma noticia 'inesquecivel'"));
            break;
          }
          case 1: {
            Platform.runLater(() -> this.comentario
                .setText("Leitor 2: Quem publicou a pesquisa foi um arabe? Aquele tal de Al Zheimer?"));
            break;
          }

          case 2: {
            Platform.runLater(() -> this.comentario.setText("Leitor 3: Sera que eu ja li essa materia?"));
            break;
          }

          case 3: {
            Platform.runLater(() -> this.comentario.setText("Leitor 4: Ah nao."));
            break;
          }
          case 4: {
            Platform.runLater(() -> this.comentario.setText("Leitor 5: Hahaha como que eu rio de uma coisa assim?"));
            break;
          }
        }
        break;
      }

      case "Urgente! Morador de rua eh condenado a prisao domiciliar em SP": {
        switch (id) {
          case 0: {
            Platform.runLater(() -> this.comentario.setText("Leitor 1: Prisao domiciliar? Como assim?"));
            break;
          }
          case 1: {
            Platform.runLater(() -> this.comentario.setText("Leitor 2: Pelo menos ele vai ter um teto, nao eh?"));
            break;
          }

          case 2: {
            Platform.runLater(() -> this.comentario.setText("Leitor 3: Sao Paulo sempre me supreendendo..."));
            break;
          }

          case 3: {
            Platform.runLater(() -> this.comentario.setText("Leitor 4:Isso aconteceu mesmo?"));
            break;
          }
          case 4: {
            Platform.runLater(() -> this.comentario.setText("Leitor 5: Eh cada uma..."));
            break;
          }
        }
        break;
      }

      case "Vidente preve que prever o futuro sera a profissao do futuro": {
        switch (id) {
          case 0: {
            Platform.runLater(() -> this.comentario.setText("Leitor 1: Uma mulher a frente do seu tempo..."));
            break;
          }
          case 1: {
            Platform.runLater(() -> this.comentario.setText("Leitor 2: Piada previsivel..."));
            break;
          }

          case 2: {
            Platform.runLater(() -> this.comentario.setText("Leitor 3: Isso nao eh uma noticia, eh um paradoxo"));
            break;
          }

          case 3: {
            Platform.runLater(() -> this.comentario.setText("Leitor 4: Acho que eu nao entendi essa"));
            break;
          }
          case 4: {
            Platform.runLater(() -> this.comentario.setText("Leitor 5: Quase um trava-linguas"));
            break;
          }
        }
        break;
      }

      case "Conferencia sobre procrastinacao eh adiada indefinidamente": {
        switch (id) {
          case 0: {
            Platform.runLater(() -> this.comentario.setText("Leitor 1: Eles vao remarcar a conferencia, algum dia..."));
            break;
          }
          case 1: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 2: Eu estava planejando participar, mas depois eu vejo isso."));
            break;
          }

          case 2: {
            Platform.runLater(() -> this.comentario
                .setText("Leitor 3: O salao estava vazio, deixaram pra avisar os participantes na ultima hora"));
            break;
          }

          case 3: {
            Platform.runLater(() -> this.comentario.setText("Leitor 4: Era so o que me faltava..."));
            break;
          }
          case 4: {
            Platform.runLater(() -> this.comentario.setText("Leitor 5: Essa foi bem fraquinha"));
            break;
          }
        }
        break;
      }

      case "Astronauta fica perdido no espaco, e reclama da falta de sinalizacao": {
        switch (id) {
          case 0: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 1: Se tivesse pedido informacao pra o ET de Varginha..."));
            break;
          }
          case 1: {
            Platform.runLater(() -> this.comentario
                .setText("Leitor 2: Falta pavimentacao tambem, as ruas sao todas de 'Terra', sacou?"));
            break;
          }

          case 2: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 3: Nao quero nem saber o tipo de buraco que tem nessas ruas..."));
            break;
          }

          case 3: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 4: Quem escreveu isso tava com a cabeca em outro mundo..."));
            break;
          }

          case 4: {
            Platform.runLater(() -> this.comentario
                .setText("Leitor 5: Falta escreverem sobre o aumento da passagem do onibus espacial"));
            break;
          }
        }
        break;
      }
      case "Em breve: Manchetes 'quase' engracadas": {
        switch (id) {
          case 0: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 1: Cheguei muito cedo..."));
            break;
          }
          case 1: {
            Platform.runLater(() -> this.comentario
                .setText("Leitor 2: Nossa! Ainda nao escreveram as manchetes?"));
            break;
          }

          case 2: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 3: Quem diria? Cheguei primeiro que o escritor"));
            break;
          }

          case 3: {
            Platform.runLater(
                () -> this.comentario.setText("Leitor 4: Mal posso esperar"));
            break;
          }

          case 4: {
            Platform.runLater(() -> this.comentario
                .setText("Leitor 5: Estou ansioso!"));
            break;
          }
        }
        break;
      }
    }// fim do switch(dadoLido)
    Platform.runLater(() -> this.comentario.setVisible(true));// exibe o comentario
  }// fim do metodo comentarManchete

  public void alterarTempoLendo(int tempoLendo) {
    this.tempoLendo = (tempoLendo) * 1000;// altera o numero de segundo que o leitor passa no estado
  }// fim do metodo alterarTempoLendo

  public void alterarTempoUtilizandoDado(int tempoUtilizandoDado) {
    this.tempoUtilizandoDado = (tempoUtilizandoDado) * 1000;// altera o numero de segundo que o leitor passa no estado
  }// fim do metodo alterarTempoUtilizandoDado

  public boolean isParado() {// retorna a info se a thread esta parada ou nao
    return isParado;
  }// fim do metodo isParado
}// fim da classe Leitor