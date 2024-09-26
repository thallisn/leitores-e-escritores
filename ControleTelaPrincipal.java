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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControleTelaPrincipal implements Initializable {
  @FXML
  public ImageView imgvLeitor_00, imgvLeitor_01, imgvLeitor_02, imgvLeitor_03, imgvLeitor_04;

  @FXML
  public ImageView imgvEscritor_00, imgvEscritor_01, imgvEscritor_02, imgvEscritor_03, imgvEscritor_04;

  @FXML
  public Label lblComentario_00, lblComentario_01, lblComentario_02, lblComentario_03, lblComentario_04;

  @FXML
  public Slider sldLendo_00, sldLendo_01, sldLendo_02, sldLendo_03, sldLendo_04;

  @FXML
  public Slider sldUtilizando_00, sldUtilizando_01, sldUtilizando_02, sldUtilizando_03, sldUtilizando_04;

  @FXML
  public Slider sldEscrevendo_00, sldEscrevendo_01, sldEscrevendo_02, sldEscrevendo_03, sldEscrevendo_04;

  @FXML
  public Slider sldObtendo_00, sldObtendo_01, sldObtendo_02, sldObtendo_03, sldObtendo_04;

  @FXML
  public Button btnPararRetomar_L_00, btnPararRetomar_L_01, btnPararRetomar_L_02, btnPararRetomar_L_03,
      btnPararRetomar_L_04;

  @FXML
  public Button btnPararRetomar_E_00, btnPararRetomar_E_01, btnPararRetomar_E_02, btnPararRetomar_E_03,
      btnPararRetomar_E_04;

  @FXML
  public Label lblBaseDeDados;

  @FXML
  public Button btn_Iniciar;

  @FXML
  public Button btn_Reiniciar;

  public final int NUM_LEITORES = 5;// numero de Threads Leitor
  public final int NUM_ESCRITORES = 5;

  public Leitor[] arrayLeitores;
  public Escritor[] arrayEscritores;
  public BaseDeDados baseDeDados;

  public ImageView[] arrayImgv_Leitores;
  public ImageView[] arrayImgv_Escritores;

  public Label[] arrayLabel_Comentarios;

  public Slider[] arraySlider_TempoLendo;
  public Slider[] arraySlider_TempoUtilizandoDado;

  public Slider[] arraySlider_TempoEscrevendo;
  public Slider[] arraySlider_TempoObtendoDado;

  public Button[] arrayBtn_Parar_Retomar_Leitores;
  public Button[] arrayBtn_Parar_Retomar_Escritores;

  // CONTROLE DE ESTADOS DAS THREADS
  public void criarThreads() {
    // ao criar uma base de dados os semaforos sao iniciados com seus valores padrao
    this.baseDeDados = new BaseDeDados(lblBaseDeDados);// LABEL DA BASE

    this.arrayLeitores = new Leitor[NUM_LEITORES];
    this.arrayEscritores = new Escritor[NUM_ESCRITORES];

    for (int i = 0; i < this.arrayLeitores.length; i++) {
      this.arrayLeitores[i] = new Leitor(this, this.baseDeDados, i);
    } // fim do for

    for (int i = 0; i < this.arrayEscritores.length; i++) {
      this.arrayEscritores[i] = new Escritor(this, this.baseDeDados, i);
    } // fim do for
  }// fim do metodo criarThreadsLeitor

  public void iniciarThreads() {
    for (int i = 0; i < this.arrayLeitores.length; i++) {
      this.arrayLeitores[i].start();
    } // fim do for

    for (int i = 0; i < this.arrayEscritores.length; i++) {
      this.arrayEscritores[i].start();
    } // fim do for
  }// fim do metodo iniciarThreadsLeitor

  public void interromperThreads() {
    for (int i = 0; i < this.arrayLeitores.length; i++) {
      if (this.arrayLeitores[i].isParado()) {
        this.arrayLeitores[i].retomar();// retoma a execucao da thread para encerrar corretamente
      }
      this.arrayLeitores[i].interrupt();
    } // fim do for

    for (int i = 0; i < this.arrayEscritores.length; i++) {
      if (this.arrayEscritores[i].isParado()) {
        this.arrayEscritores[i].retomar();// retoma a execucao da thread para encerrar corretamente
      }
      this.arrayEscritores[i].interrupt();
    } // fim do for
  }// fim do metodo interromperThreadsLeitor

  public void iniciarArraysDeInterface() {
    this.arrayImgv_Leitores = new ImageView[] { imgvLeitor_00, imgvLeitor_01, imgvLeitor_02, imgvLeitor_03,
        imgvLeitor_04 };
    this.arrayImgv_Escritores = new ImageView[] { imgvEscritor_00, imgvEscritor_01, imgvEscritor_02, imgvEscritor_03,
        imgvEscritor_04 };

    this.arrayLabel_Comentarios = new Label[] { lblComentario_00, lblComentario_01, lblComentario_02, lblComentario_03,
        lblComentario_04 };

    this.arraySlider_TempoLendo = new Slider[] { sldLendo_00, sldLendo_01, sldLendo_02, sldLendo_03, sldLendo_04 };
    this.arraySlider_TempoUtilizandoDado = new Slider[] { sldUtilizando_00, sldUtilizando_01, sldUtilizando_02,
        sldUtilizando_03, sldUtilizando_04 };

    this.arraySlider_TempoEscrevendo = new Slider[] { sldEscrevendo_00, sldEscrevendo_01, sldEscrevendo_02,
        sldEscrevendo_03, sldEscrevendo_04 };
    this.arraySlider_TempoObtendoDado = new Slider[] { sldObtendo_00, sldObtendo_01, sldObtendo_02, sldObtendo_03,
        sldObtendo_04 };

    this.arrayBtn_Parar_Retomar_Leitores = new Button[] { btnPararRetomar_L_00, btnPararRetomar_L_01,
        btnPararRetomar_L_02, btnPararRetomar_L_03, btnPararRetomar_L_04 };
    this.arrayBtn_Parar_Retomar_Escritores = new Button[] { btnPararRetomar_E_00, btnPararRetomar_E_01,
        btnPararRetomar_E_02, btnPararRetomar_E_03, btnPararRetomar_E_04 };
  }

  /*
   * ***************************************************************
   * Metodo: initialize
   * Funcao: definir acoes tomadas ao iniciar aplicacao
   * Parametros: void
   * Retorno: void
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    iniciarArraysDeInterface();

    for (int i = 0; i < NUM_ESCRITORES; i++) {// configurar os sliders de cada thread
      final int indice = i;// resolve problema de escopo
      this.arraySlider_TempoLendo[i].valueProperty().addListener((observable, valorAntigo, novoValor) -> {
        arrayLeitores[indice].alterarTempoLendo(novoValor.intValue());
      });

      this.arraySlider_TempoUtilizandoDado[i].valueProperty().addListener((observable, valorAntigo, novoValor) -> {
        arrayLeitores[indice].alterarTempoUtilizandoDado(novoValor.intValue());
      });

      this.arraySlider_TempoEscrevendo[i].valueProperty().addListener((observable, valorAntigo, novoValor) -> {
        arrayEscritores[indice].alterarTempoEscrevendo(novoValor.intValue());
      });

      this.arraySlider_TempoObtendoDado[i].valueProperty().addListener((observable, valorAntigo, novoValor) -> {
        arrayEscritores[indice].alterarTempoObtendoDado(novoValor.intValue());
      });
    }

    Platform.runLater(() -> {
      desabilitarBtn_Reiniciar();
      desabilitarControles_Leitores();
      desabilitarControles_Escritores();
    });

  }// fim do metodo initialize

  // METODOS "OUVINTES" PARA ELEMENTOS DA INTERFACE GRAFICA

  public void configurarOuvintesSliders() {

  }

  public void acaoBtn_Iniciar() {
    Platform.runLater(() -> {
      habilitarBtn_Reiniciar();
      desabilitarBtnIniciar();
      habilitarControles_Leitores();
      habilitarControles_Escritores();
    });

    criarThreads();
    iniciarThreads();
  }

  public void acaoBtn_Reiniciar() {
    interromperThreads();

    Platform.runLater(() -> {
      this.baseDeDados.setDado("Em breve: Manchetes 'quase' engracadas");

      desabilitarBtn_Reiniciar();
      desabilitarControles_Leitores();
      desabilitarControles_Escritores();
    });
    habilitarBtn_Iniciar();
  }

  public void acaoBtn_Parar_Retomar_Leitor00() {
    if (arrayLeitores[0].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoLendo[0].setDisable(false);
        arraySlider_TempoUtilizandoDado[0].setDisable(false);
        arrayBtn_Parar_Retomar_Leitores[0].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[0].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoLendo[0].setDisable(true);
        arraySlider_TempoUtilizandoDado[0].setDisable(true);
        arrayBtn_Parar_Retomar_Leitores[0].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[0].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Leitor01() {
    if (arrayLeitores[1].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoLendo[1].setDisable(false);
        arraySlider_TempoUtilizandoDado[1].setDisable(false);
        arrayBtn_Parar_Retomar_Leitores[1].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[1].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoLendo[1].setDisable(true);
        arraySlider_TempoUtilizandoDado[1].setDisable(true);
        arrayBtn_Parar_Retomar_Leitores[1].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[1].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Leitor02() {
    if (arrayLeitores[2].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoLendo[2].setDisable(false);
        arraySlider_TempoUtilizandoDado[2].setDisable(false);
        arrayBtn_Parar_Retomar_Leitores[2].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[2].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoLendo[2].setDisable(true);
        arraySlider_TempoUtilizandoDado[2].setDisable(true);
        arrayBtn_Parar_Retomar_Leitores[2].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[2].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Leitor03() {
    if (arrayLeitores[3].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoLendo[3].setDisable(false);
        arraySlider_TempoUtilizandoDado[3].setDisable(false);
        arrayBtn_Parar_Retomar_Leitores[3].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[3].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoLendo[3].setDisable(true);
        arraySlider_TempoUtilizandoDado[3].setDisable(true);
        arrayBtn_Parar_Retomar_Leitores[3].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[3].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Leitor04() {
    if (arrayLeitores[4].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoLendo[4].setDisable(false);
        arraySlider_TempoUtilizandoDado[4].setDisable(false);
        arrayBtn_Parar_Retomar_Leitores[4].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[4].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoLendo[4].setDisable(true);
        arraySlider_TempoUtilizandoDado[4].setDisable(true);
        arrayBtn_Parar_Retomar_Leitores[4].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayLeitores[4].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Escritor00() {
    if (arrayEscritores[0].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoEscrevendo[0].setDisable(false);
        arraySlider_TempoObtendoDado[0].setDisable(false);
        arrayBtn_Parar_Retomar_Escritores[0].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[0].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoEscrevendo[0].setDisable(true);
        arraySlider_TempoObtendoDado[0].setDisable(true);
        arrayBtn_Parar_Retomar_Escritores[0].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[0].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Escritor01() {
    if (arrayEscritores[1].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoEscrevendo[1].setDisable(false);
        arraySlider_TempoObtendoDado[1].setDisable(false);
        arrayBtn_Parar_Retomar_Escritores[1].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[1].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoEscrevendo[1].setDisable(true);
        arraySlider_TempoObtendoDado[1].setDisable(true);
        arrayBtn_Parar_Retomar_Escritores[1].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[1].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Escritor02() {
    if (arrayEscritores[2].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoEscrevendo[2].setDisable(false);
        arraySlider_TempoObtendoDado[2].setDisable(false);
        arrayBtn_Parar_Retomar_Escritores[2].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[2].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoEscrevendo[2].setDisable(true);
        arraySlider_TempoObtendoDado[2].setDisable(true);
        arrayBtn_Parar_Retomar_Escritores[2].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[2].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Escritor03() {
    if (arrayEscritores[3].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoEscrevendo[3].setDisable(false);
        arraySlider_TempoObtendoDado[3].setDisable(false);
        arrayBtn_Parar_Retomar_Escritores[3].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[3].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoEscrevendo[3].setDisable(true);
        arraySlider_TempoObtendoDado[3].setDisable(true);
        arrayBtn_Parar_Retomar_Escritores[3].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[3].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  public void acaoBtn_Parar_Retomar_Escritor04() {
    if (arrayEscritores[4].isParado()) {
      Platform.runLater(() -> {
        arraySlider_TempoEscrevendo[4].setDisable(false);
        arraySlider_TempoObtendoDado[4].setDisable(false);
        arrayBtn_Parar_Retomar_Escritores[4].setText("Parar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[4].retomar();// retoma a execucao da thread
    } else {
      Platform.runLater(() -> {// atualizacoes na interface grafica
        arraySlider_TempoEscrevendo[4].setDisable(true);
        arraySlider_TempoObtendoDado[4].setDisable(true);
        arrayBtn_Parar_Retomar_Escritores[4].setText("Retomar");
      });// fim das atualizacoes na interface grafica
      arrayEscritores[4].parar();// para a execucao da thread
    } // fim do bloco if-else
  }

  // CONTROLES DE ESTADOS PARA ELEMENTOS DA INTERFACE GRAFICA
  public void habilitarBtn_Iniciar() {
    btn_Iniciar.setDisable(false);// habilita o botao
    btn_Iniciar.setOpacity(1);// exibe o botao
  }// fim do metodo habilitarBtnIniciar

  public void habilitarBtn_Reiniciar() {
    btn_Reiniciar.setDisable(false);// habilita o botao
    btn_Reiniciar.setOpacity(1);// exibe o botao
  }// fim do metodo habilitarBtnReiniciar

  public void desabilitarBtnIniciar() {
    btn_Iniciar.setDisable(true);// desabilita o botao
    btn_Iniciar.setOpacity(0);// exibe o botao
  }// fim do metodo desabilitarBtnIniciar

  public void desabilitarBtn_Reiniciar() {
    btn_Reiniciar.setDisable(true);// desabilita o botao
    btn_Reiniciar.setOpacity(0);// oculta o botao
  }// fim do metodo desabilitarBtnReiniciar

  public void habilitarControles_Leitores() {
    for (int i = 0; i < NUM_LEITORES; i++) {
      arraySlider_TempoLendo[i].setDisable(false);
      arraySlider_TempoUtilizandoDado[i].setDisable(false);

      arrayBtn_Parar_Retomar_Leitores[i].setDisable(false);
    }
  }

  public void habilitarControles_Escritores() {
    for (int i = 0; i < NUM_ESCRITORES; i++) {
      arraySlider_TempoEscrevendo[i].setDisable(false);
      arraySlider_TempoObtendoDado[i].setDisable(false);

      arrayBtn_Parar_Retomar_Escritores[i].setDisable(false);
    }
  }

  public void desabilitarControles_Leitores() {
    for (int i = 0; i < NUM_LEITORES; i++) {
      arraySlider_TempoLendo[i].setDisable(true);
      arraySlider_TempoUtilizandoDado[i].setDisable(true);

      arraySlider_TempoLendo[i].setValue(2);
      arraySlider_TempoUtilizandoDado[i].setValue(2);

      arrayBtn_Parar_Retomar_Leitores[i].setText("Parar");
      arrayBtn_Parar_Retomar_Leitores[i].setDisable(true);
      arrayLabel_Comentarios[i].setVisible(false);

    }
  }

  public void desabilitarControles_Escritores() {
    for (int i = 0; i < NUM_LEITORES; i++) {
      arraySlider_TempoEscrevendo[i].setDisable(true);
      arraySlider_TempoObtendoDado[i].setDisable(true);

      arraySlider_TempoEscrevendo[i].setValue(2);
      arraySlider_TempoObtendoDado[i].setValue(2);

      arrayBtn_Parar_Retomar_Escritores[i].setText("Parar");
      arrayBtn_Parar_Retomar_Escritores[i].setDisable(true);
    }
  }

  // GETTERS UTILIZADOS PARA INICIAR AS THREADS
  public ImageView getImageView_Leitor(int id) {
    return arrayImgv_Leitores[id];
  }

  public ImageView getImageView_Escritor(int id) {
    return arrayImgv_Escritores[id];
  }

  public Image getImagem_Lendo() {
    Image imagemLendo = new Image("img/leitor_lendo.png");

    return imagemLendo;
  }

  public Image getImagem_UtilizandoDado() {
    Image imagemUtilizandoDado = new Image("img/leitor_utilizando_dado.png");

    return imagemUtilizandoDado;
  }

  public Image getImagem_Aguardando_Leitor() {
    Image imagemAguardando = new Image("img/leitor_aguardando.png");

    return imagemAguardando;
  }

  public Image getImagem_Escrevendo() {
    Image imagemEscrevendo = new Image("img/escritor_escrevendo.png");

    return imagemEscrevendo;
  }

  public Image getImagem_ObtendoDado() {
    Image imagemObtendoDado = new Image("img/escritor_obtendo_dado.png");

    return imagemObtendoDado;
  }

  public Image getImagem_Aguardando_Escritor() {
    Image imagemAguardando = new Image("img/escritor_aguardando.png");

    return imagemAguardando;
  }

  public Label getLabel_Comentario(int id) {
    return arrayLabel_Comentarios[id];
  }

}// fim da classe ControleTelaPrincipal