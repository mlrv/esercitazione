package esercitazioneJava2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import javax.swing.border.*;
import java.io.*;

/** Classe che realizza un rudimentale programma di disegno di figure
 * geometriche (rettangoli, triangoli, ellissi). */
public class DisegnaFigure extends JPanel 
implements ActionListener, MouseListener, MouseMotionListener
{

// ================ Parte per il dimensionamento del pannello:

  // La parte del piano "inquadrata" dal pannello di disegno e'
  // quella nel rettangolo [-massimoX,massimoX] x [-massimoY,massimoY]
  
  /** Un valore positivo XX tale che tutte le coordinate x delle figure
   * presenti nel sistema siano comprese tra -XX e XX. */
  private int massimoX = SistemaI.DIMENSIONE/2;

  /** Un valore positivo YY tale che le coordinate y delle figure
   * presenti nel sistema siano  comprese tra -YY e YY. */
  private int massimoY = SistemaI.DIMENSIONE/2;

  /** Allarga le dimensioni della parte di piano inquadrata nella finestra
   * in seguito alla traslazione o all'aggiunta della figura f, in modo tale che
   * la parte di piano mostrata contenga f interamente.
   * Per le modalita' di inserimento, in inserimento e' necessario
   * solo quando la nuova figura f e' un ellisse. */
  private void aggiornaDimensioni(FiguraI f)
  {
    boolean allarga = false;
    if (f.minX()<-massimoX) 
    {  allarga = true;  massimoX = -(int)f.minX();  }
    if (f.minY()<-massimoY)
    {  allarga = true;  massimoY = -(int)f.minY();  }
    if (f.minX()+f.estensioneX()>massimoX) 
    {  allarga = true;  massimoX = (int)(f.minX()+f.estensioneX());  }
    if (f.minY()+f.estensioneY()>massimoY)
    {  allarga = true;  massimoY = (int)(f.minY()+f.estensioneY());  }
    if (allarga)
    {
      this.setPreferredSize(new Dimension(2*massimoX,2*massimoY));
      this.revalidate();
    }
  }
// ====================== Colori per disegnare le varie cose:

  /** Colore per la figura selezionata. */ 
  public Color coloreSelezione = Color.yellow;
  /** Colore per la figura in via di inserimento. */ 
  public Color coloreInserimento = Color.green;
  /** Colore per le situazioni di errore o che chiedono attenzione. */ 
  public Color coloreErrore = Color.red;
  /** Colore per la figura che l'utente sta traslando. */ 
  public Color coloreTraslazione = Color.cyan;
  /** Colore per gli assi cartesiani. */ 
  public Color coloreAssi = Color.gray;
  /** Colore dello sfondo. */
  public Color coloreNeutro;
    
// ================ Parte per il disegno delle figure:

  // Le coordinate in pixel sono [0,2*massimoX] x [0,2*massimoY],
  // le coordinate astratte invece [-massimoX,massimoX] x [-massimoY,massimoY]
  // quindi bisogna operare una traslazione.
  // Inoltre l'asse y delle coordinate in pixel del disegno e'
  // orientato verso il basso, mentre quello reale "astratto" e' verso l'alto.
  
  /** Le coordinate sono di disegno. Il valore di altezza
   * e' negativo perche' il triangolo deve estendersi verso l'alto,
   * cioe' nella direzione negativa dell'asse y di disegno.
   * Ritorna la poligonale chiusa da disegnare con drawPath. */
  private Path2D.Double creaTriangolo(double x1, double y1, 
            double base, double altezza)
  {
    Path2D.Double tri  = new Path2D.Double();
    tri.moveTo(x1,y1);
    tri.lineTo(x1+base,y1);
    tri.lineTo(x1+0.5*base, y1+altezza);
    tri.closePath();
    return tri;
  }

  // Le seguenti funzioni realizzano la trasformazione di coordinate 
  // tra coordinare reali "astratte" e coordinate intere "di disegno".
  // L'origine "astratta" si trova al centro del pannello.
  // L'asse y "astratto" e' orientato verso l'alto, mentre nelle 
  // coordinate di disegno e' orientato verso il basso.
  
  /** Calcola la coordinata x in pixel dove va disegnata la x reale data. */
  protected int disegnoX(double x)
  {  return (int)(x)+this.getWidth()/2;  }

  /** Calcola la coordinata x reale corrispondente alla posizione x in pixel. */
  protected double astrattaX(int x)
  {  return x-this.getWidth()/2;  }

  /** Calcola la coordinata y in pixel dove va disegnata la y reale data. */
  protected int disegnoY(double y)
  {  return this.getHeight()/2-(int)(y);  }

  /** Calcola la coordinata y reale corrispondente alla posizione y in pixel. */
  protected double astrattaY(int y)
  {  return this.getHeight()/2-y;  }

  /** Funzione ausiliaria chiamata da paintComponent per disegnare le
   * figure presenti nel sistema. */
  protected void disegnaLeFigure(Graphics2D g)
  {
    Ellipse2D.Double ellisse;
    Rectangle2D.Double rettangolo;
    Path2D.Double triangolo;  
    int i;
    FiguraI[] figg = sistema.figure();
    for (i=0; i<figg.length; i++)
    {
      g.setColor(figg[i].colore());
      switch (figg[i].numeroVertici())
      {
        case 0: // ellisse
          ellisse = new Ellipse2D.Double(disegnoX(figg[i].minX()),
                                         disegnoY(figg[i].minY()+figg[i].estensioneY()),
                                         figg[i].estensioneX(),
                                         figg[i].estensioneY());
          g.setColor(figg[i].colore());
          g.fill(ellisse);
          g.setColor(Color.black);
          g.draw(ellisse);
          break;
        case 3: // triangolo
          triangolo = creaTriangolo(disegnoX(figg[i].minX()),
                                    disegnoY(figg[i].minY()),
                                    figg[i].estensioneX(),
                                    -figg[i].estensioneY());
          g.setColor(figg[i].colore());
          g.fill(triangolo);
          g.setColor(Color.black);
          g.draw(triangolo);
          break;
        case 4: // rettangolo
          rettangolo = new Rectangle2D.Double(disegnoX(figg[i].minX()),
                                              disegnoY(figg[i].minY()+figg[i].estensioneY()),
                                              figg[i].estensioneX(),
                                              figg[i].estensioneY());
          g.setColor(figg[i].colore());
          g.fill(rettangolo);
          g.setColor(Color.black);
          g.draw(rettangolo);
          break;
      }
    }
  }

 /** Funzione ausiliaria chiamata da paintComponent per disegnare la
   * figura selezionata. */
  protected void disegnaSelezionata(Graphics2D g)
  {
    Ellipse2D.Double ellisse;
    Rectangle2D.Double rettangolo;
    Path2D.Double triangolo;  
    Stroke tratto = g.getStroke(); // salva
    g.setStroke(new BasicStroke(2)); // metti spessore linea 2
    g.setColor(coloreSelezione);
    switch (selezionata.numeroVertici())
    {
        case 0: // ellisse
          ellisse = new Ellipse2D.Double(disegnoX(selezionata.minX()),
                                         disegnoY(selezionata.minY()+selezionata.estensioneY()),
                                         selezionata.estensioneX(),
                                         selezionata.estensioneY());
          g.draw(ellisse);
          break;
        case 3: // triangolo
          triangolo = creaTriangolo(disegnoX(selezionata.minX()),
                                    disegnoY(selezionata.minY()),
                                    selezionata.estensioneX(),
                                    -selezionata.estensioneY());


          g.draw(triangolo);
          break;
        case 4: // rettangolo
          rettangolo = new Rectangle2D.Double(disegnoX(selezionata.minX()),
                                              disegnoY(selezionata.minY()+selezionata.estensioneY()),
                                              selezionata.estensioneX(),
                                              selezionata.estensioneY());
          g.draw(rettangolo);
          break;
    }
    g.setStroke(tratto); // ripristina
    if (traslando)
    {
      g.setColor(coloreTraslazione);
      g.fillOval(disegnoX(selezionata.centroX())-2, disegnoY(selezionata.centroY())-2, 5,5); // baricentro
      g.fillOval(x1-2,y1-2,5,5); // nuova posizione per il baricentro
      g.drawLine(disegnoX(selezionata.centroX()), disegnoY(selezionata.centroY()), x1,y1);
    }
  }

  /** Funzione ausiliaria chiamata da paintComponent per
   * disegnare la figura in via di inserimento. */
  public void disegnaFiguraIns(Graphics2D g)
  {
    if (fase==1)
    {
      switch (tipoFigura)
      {
        case ELLISSE:
          if (figuraOK())
          { 
            g.setColor(coloreInserimento);
            int dx = (int)calcolaRaggioX(); 
            int dy = (int)calcolaRaggioY();
            g.drawOval(x1-dx,y1-dy, 2*dx,2*dy);
          }
          else 
          { 
            g.setColor(coloreErrore); 
            if (x1!=x2) g.drawLine(2*x1-x2,y1, x2,y1);
            if (y1!=y2) g.drawLine(x1,2*y1-y2, x1,y2);
          }
          break;          
        case RETTANG:
          if (figuraOK())
          { 
            g.setColor(coloreInserimento);
            g.drawLine(x1,y1, x1,y2);
            g.drawLine(x1,y2, x2,y2);
            g.drawLine(x2,y2, x2,y1);
            g.drawLine(x2,y1, x1,y1);
          } 
          else 
          { 
            g.setColor(Color.red); 
            if ((x1!=x2)||(y1!=y2)) g.drawLine(x1,y1, x2,y2);
          }          
          break;
        case TRIANG:
          if (figuraOK())
          {
            g.setColor(coloreInserimento);
            if (y1>y2)
            {
              g.drawLine(x1,y1, x2,y1);
              g.drawLine(x1,y1, (x1+x2)/2,y2);
              g.drawLine(x2,y1, (x1+x2)/2,y2);
            }
            else
            {
              g.drawLine(x1,y2, x2,y2);
              g.drawLine(x1,y2, (x1+x2)/2,y1);
              g.drawLine(x2,y2, (x1+x2)/2,y1);
            }
          }
          else
          {
            g.setColor(coloreErrore); 
            if ((x1!=x2)||(y1!=y2)) g.drawLine(x1,y1, x2,y2);
          }
          break;
      }
      // disegna punto (x1,y1)
      g.setColor(coloreInserimento);
      g.fillOval(x1-2,y1-2,5,5);
    }
  }

 /** Soprascrive la funzione di disegno ereditata dalla superclasse
  * JPanel per disegnare tutte le figure presenti nel sistema, evidenziando
  * l'eventuale figura selezionata e facendo l'anteprima dell'eventuale
  * figura in via di inserimento. */
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    // disegno gli assi cartesiani
    g.setColor(coloreAssi);
    g.drawLine(disegnoX(0),0, disegnoX(0), getHeight());
    g.drawLine(0,disegnoY(0), getWidth(),disegnoY(0));
    
    // disegno di tutte le figure presenti
    disegnaLeFigure(g2);
     
    // eventuale disegno della figura selezionata
    if (selezionata!=null)
       disegnaSelezionata(g2);

    // eventuale disegno della figura in fase di inserimento
    if (tipoFigura!= NESSUNA)
       disegnaFiguraIns(g2);     
  }

// ============ Parte per inserimento di una figura:

  // Per comodita' definisco queste costanti come uguali al 
  // numero di vertici delle rispettive figure, servono come
  // valori della variabile tipoFigura
  private static final int ELLISSE = 0;
  private static final int RETTANG = 4;
  private static final int TRIANG = 3;
  private static final int NESSUNA = 999;
  /** Tipo della figura che l'utente sta inserendo. */
  private int tipoFigura = NESSUNA;
  /** Coordinate in pixel del primo punto durante l'inserimento. */
  private int x1, y1;
  /** Coordinate in pixel del secondo punto durante l'inserimento. */
  private int  x2, y2;
  /** Fase di inserimento. */
  private int fase;

  // Se sto inserendo un ellisse:
  //  (x1,y1) e' il centro
  //  se fase>=1 (x2,y2) e' un altro punto sul contorno

  // Se sto inserendo un rettangolo:
  //  (x1,y1) e' un vertice del rettangolo
  //  se fase>=1 (x2,y2) e' il vertice opposto al primo

  // Se sto inserendo un triangolo con punta in su:
  //  (x1,y1) e' uno dei due vertici di base
  //  se fase>=1 (x2,y2) e' il vertice opposto alla base

  /** Assume fase==1 e tipoFigura!=NESSUNA,
   * ritorna vero sse x1,y1,x2,y2 correnti definiscono 
   * un ellisse/rettangolo/triangolo non degenere.
   * Teoricamente basta che siano diverse, ma perche' il disegno
   * venga distinguibile chiedo almeno 4 pixel di differenza. */
  private boolean figuraOK()
  {  return ( (x1-x2)>=4 || (x2-x1)>=4 ) && ( (y1-y2)>=4 || (y2-y1)>=4 );  }
  
  /** Nel caso di ellisse, assumendo fase>1 calcola il raggio orizzontale, in pixel. */
  private double calcolaRaggioX()
  {  return (x2>x1) ? Math.sqrt(2.0)*(x2-x1) : Math.sqrt(2.0)*(x1-x2);  }

  /** Nel caso di ellisse, assumendo fase>1 calcola il raggio verticale, in pixel. */
  private double calcolaRaggioY()
  {  return (y2>y1) ? Math.sqrt(2.0)*(y2-y1) : Math.sqrt(2.0)*(y1-y2);  }

  /** Assume tipoFigura==uno tra ELLISSE,RETTANG,TRIANG. 
   * Gestisce l'azione dell'utente col mouse durante l'inserimento.
   * (x,y) sono le coordinate alla posizione del cursore, fase e' la fase corrente,
   * click dice se il punto e' stato cliccato.
   * Ritorna la nuova fase. 
   * <BR>
   * L'aggiunta di un ellisse avviene cosi':
   * l'utente muove il cursore e viene mostrata la posizione corrente,
   * l'utente clicca su un punto e stabilisce il centro,
   * da quel momento viene mostrato l'ellisse che si inserirebbe cliccando
   * alla posizione attuale del cursore,
   * l'utente clicca in un altro punto e stabilisce l'ellisse.
   * <BR>
   * L'aggiunta di un rettangolo avviene cosi':
   * l'utente muove il cursore e viene mostrata la posizione corrente,
   * l'utente clicca su un punto e stabilisce un estremo della diagonale,
   * da quel momento viene mostrato il rettangolo che si inserirebbe cliccando
   * alla posizione attuale del cursore,
   * l'utente clicca in un altro punto e stabilisce il secondo estremo della diagonale.
   * <BR>
   * L'aggiunta di un triangolo avviene cosi':
   * l'utente muove il cursore e viene mostrata la posizione corrente,
   * l'utente clicca su un punto e stabilisce un estremo della base,
   * da quel momento viene mostrato il triangolo che si inserirebbe cliccando
   * alla posizione attuale del cursore,
   * l'utente clicca in un altro punto e stabilisce il vertice del triangolo.
   */
  private int processaInserimento(int x, int y, boolean click)
  {
    if (fase==0)
    {
      x1=x; y1=y; 
      if (click) fase++;
    }
    else if (fase==1)
    {
      x2=x; y2=y;
      if (click && figuraOK()) fase++;
    }
    return fase;
  }

// ==================== Parte per interazione con l'utente:

  /** La finestra all'interno della quale si trova questo
   * pannello di disegno. */
  protected JFrame finestra; 
  
  // Bottoni per il modo corrente, mutuamente esclusivi:
  /** Bottone per modo selezione. */
  protected JRadioButton bottoneSelezione;
  /** Bottone per modo inserimento di ellisse. */
  protected JRadioButton bottoneEllisse;
  /** Bottone per modo inserimento di rettangolo. */
  protected JRadioButton bottoneRettangolo;
  /** Bottone per modo inserimento di triangolo. */
  protected JRadioButton bottoneTriangolo;
  
  // Per agire sulla figura selezionata (abilitati solo se ne esiste una):
  /** Bottone per eliminare la figura selezionata. */
  protected JButton bottoneCancella;
  /** Bottone per portare su la figura selezionata. */
  protected JButton bottoneSu; 
  /** Bottone per portare giu' la figura selezionata. */
  protected JButton bottoneGiu;
  /** Bottone per traslare la figura selezionata. */
  protected JButton bottoneTrasla;
  /** Bottone per cambiare colore alla figura selezionata. */
  protected JButton bottoneColore;
  
  // Per mostrare informazioni sulla figura selezionata:
  /** Campo che mostra l'area della figura selezionata. */
  protected JTextField infoArea;
  /** Campo che mostra la lunghezza del contorno della figura selezionata. */
  protected JTextField infoContorno;
  /** Campo che mostra il tipo della figura selezionata. */
  protected JTextField infoTipo;
  
  // Per le operazioni globali (sempre abilitati):
  /** Bottone per caricare figure da file. */
  protected JButton bottoneCarica;
  /** Bottone per salvare le figure su file. */
  protected JButton bottoneSalva;
  /** Bottone per terminare. */
  protected JButton bottoneEsci;
  /** Bottone per eliminare tutte le figure. */
  protected JButton bottoneSvuota;
  
  /** Per mostrare le coordinate correnti del punto in cui
   * si trova il cursore nel pannello di disegno. */
  JLabel coordinate;
  /** Per mostrare messaggi (istruzioni, errori...). */
  JTextField messaggi;

  /** Per scegliere il nome del file. */
  private JFileChooser scegliFile;
  /** Per scegliere il colore. */
  private JColorChooser scegliColore;

  /** Il sistema sottostante, che gestisce le figure. */
  protected SistemaI sistema;  
  
  /** La figura selezionata, null se nessuna. */
  private FiguraI selezionata;
  /** Vero se, in modo selezione, stiamo traslando la figura selezionata. */
  private boolean traslando;
    
  /** Mostra il messaggio nel campo di testo,
   * colorando lo sfondo del colore indicato. */
  protected void mostraMessaggio(Color sfondo, String testo)
  {
    messaggi.setBackground(sfondo);
    messaggi.setText(testo);
  }
  
  /** Abilita o disabilita i bottoni a seconda dello stato corrente. */
  private void abilitazioni()
  {
    bottoneCancella.setEnabled(selezionata!=null);
    bottoneSu.setEnabled(selezionata!=null);
    bottoneGiu.setEnabled(selezionata!=null);
    bottoneTrasla.setEnabled(selezionata!=null);
    bottoneColore.setEnabled(selezionata!=null);
  }
  
  /** Funzione per entrare in modo inserimento del tipo di
   * figura passato in argomento (uscendo dal modo selezione). */
  private void entraModoInserimento(int tipo)
  {
    tipoFigura=tipo;
    switch (tipo)
    {
      case ELLISSE:
        mostraMessaggio(coloreInserimento,"Fare click sul centro e su un punto del contorno");
        break;
      case RETTANG:
        mostraMessaggio(coloreInserimento,"Fare click su due vertici opposti del rettangolo");
        break;
      case TRIANG:
        mostraMessaggio(coloreInserimento,"Fare click su un estremo della base e sulla punta del triangolo");          
        break;
    }
    fase=0;
    pulisciSelezione();
    abilitazioni();
    //addMouseMotionListener(this);
  }

  /** Funzione per entrare in modo selezione
   * (uscendo dal modo inserimento). */
  private void entraModoSelezione()
  {
    mostraMessaggio(coloreSelezione,"Per selezionare fare click su una figura"); 
    tipoFigura= NESSUNA;
    pulisciSelezione();
    abilitazioni();
    repaint();
  }

  /** Deseleziona l'eventuale figura selezionata e sbianca
   * i campi con le informazioni. */
  private void pulisciSelezione()
  {
    selezionata = null;
    infoTipo.setText("");
    infoArea.setText("");
    infoContorno.setText("");  
  }
  
  /** Date le coordinate in pixel del punto cliccato,
   * recupera la figura selezionata e aggiorna le informazioni. */
  private void prendiSelezione(int x, int y)
  {
     selezionata = sistema.figuraCheContiene(astrattaX(x),astrattaY(y));
     abilitazioni();
     if (selezionata==null)
     {
       pulisciSelezione();
     }
     else
     {
       switch(selezionata.numeroVertici())
       {
         case 0: infoTipo.setText("ellisse"); break;
         case 3: infoTipo.setText("triangolo"); break;
         case 4: infoTipo.setText("rettangolo"); break;
       }
       String aux = ""+selezionata.area();
       int k = aux.indexOf(".");
       if ((k>=0)&&(aux.length()>k+3)) aux = aux.substring(0,k+3);
       infoArea.setText(aux);
       aux =""+selezionata.contorno();
       k = aux.indexOf(".");
       if ((k>=0)&&(aux.length()>k+3)) aux = aux.substring(0,k+3);
       infoContorno.setText(aux);
     }
  }
  
  /** Crea la finestra grafica che contiene questo pannello
   * di disegno e tutti gli elementi per l'interazione con
   * l'utente. */
  protected void creaFinestra()
  {
    coloreNeutro = this.getBackground();
  
    // PANNELLO PER IL MODO
    JPanel panModo = new JPanel(new GridLayout(4,1));
    panModo.setBorder(new TitledBorder(new LineBorder(Color.blue,2), "Modo corrente:"));
    bottoneSelezione = new JRadioButton("Seleziona figura");
    bottoneSelezione.setSelected(true);
    bottoneEllisse = new JRadioButton("Inserisci ellisse");
    bottoneRettangolo = new JRadioButton("Inserisci rettangolo");
    bottoneTriangolo = new JRadioButton("Inserisci triangolo");
    ButtonGroup gruppo = new ButtonGroup();
    gruppo.add(bottoneSelezione);
    gruppo.add(bottoneEllisse);
    gruppo.add(bottoneRettangolo);
    gruppo.add(bottoneTriangolo);
    panModo.add(bottoneSelezione);
    panModo.add(bottoneEllisse);
    panModo.add(bottoneRettangolo);
    panModo.add(bottoneTriangolo);
    panModo.add(bottoneSelezione);
    
    // PANNELLO PER OPERAZ. GENERALI
    JPanel panGenerali = new JPanel(new GridLayout(5,1));
    panGenerali.setBorder(new TitledBorder(new LineBorder(Color.blue,2), "Operazioni generali:"));
    bottoneCarica = new JButton("Carica figure da file");
    bottoneSalva = new JButton("Salva figure su file");
    bottoneSvuota = new JButton("Elimina tutte le figure");
    bottoneEsci = new JButton("Termina il programma");
    bottoneEsci.setBackground(Color.red);
    panGenerali.add(bottoneCarica);
    panGenerali.add(bottoneSalva);
    panGenerali.add(bottoneSvuota);
    panGenerali.add(new JLabel()); // stacco
    panGenerali.add(bottoneEsci);
    
    // PANNELLO PER OPERAZIONI SULLA FIGURA SELEZIONATA
    JPanel panFigura = new JPanel(new GridLayout(5,1));
    panFigura.setBorder(new TitledBorder(new LineBorder(Color.blue,2), "Con la figura selezionata:"));
    bottoneTrasla = new JButton("Trasla");
    bottoneSu = new JButton("Porta su");
    bottoneGiu = new JButton("Porta giu");
    bottoneColore = new JButton("Cambia colore");
    bottoneCancella = new JButton("Elimina");
    panFigura.add(bottoneTrasla);
    panFigura.add(bottoneSu);
    panFigura.add(bottoneGiu);
    panFigura.add(bottoneColore);
    panFigura.add(bottoneCancella);

    // PANNELLO PER MOSTRARE INFORMAZIONI SULLA FIGURA SELEZIONATA
    JPanel panInfo = new JPanel(new GridLayout(3,2));
    panInfo.setBorder(new TitledBorder(new LineBorder(Color.blue,2), "Figura selezionata:"));
    panInfo.add(new JLabel("Tipo:"));
    panInfo.add(infoTipo = new JTextField(10));
    panInfo.add(new JLabel("Area:"));
    panInfo.add(infoArea = new JTextField(10));
    panInfo.add(new JLabel("Contorno:"));
    panInfo.add(infoContorno = new JTextField(10));
    infoTipo.setEditable(false);
    infoArea.setEditable(false);
    infoContorno.setEditable(false);
    
    // COSTRUZIONE DELLA FINESTRA 
    finestra = new JFrame();
    finestra.setTitle(sistema.titolo());
    finestra.getContentPane().setLayout(new BorderLayout());
    
    JPanel sinistro = new JPanel(new BorderLayout());
    sinistro.add(BorderLayout.NORTH, panModo);
    sinistro.add(BorderLayout.CENTER, new JLabel());
    sinistro.add(BorderLayout.SOUTH, panGenerali);
    finestra.getContentPane().add(BorderLayout.WEST, sinistro);
    
    JPanel destro = new JPanel(new BorderLayout());
    destro.add(BorderLayout.NORTH, panInfo);
    destro.add(BorderLayout.CENTER, new JLabel());
    destro.add(BorderLayout.SOUTH, panFigura);
    finestra.getContentPane().add(BorderLayout.EAST, destro);
    
    JPanel sotto = new JPanel(new GridLayout(2,1));
    JPanel aux = new JPanel(new FlowLayout());
    aux.add(new JLabel("Posizione del cursore: "));
    aux.add(coordinate = new JLabel("(0,0)"));
    sotto.add(aux);
    sotto.add(messaggi = new JTextField());
    messaggi.setEditable(false);
    mostraMessaggio(coloreNeutro, "Benvenuti nel programma");
    finestra.getContentPane().add(BorderLayout.SOUTH, sotto);
    
    JScrollPane scr = new JScrollPane(this);
    finestra.getContentPane().add(BorderLayout.CENTER, scr);
    scr.setPreferredSize(new Dimension(2*massimoX,2*massimoY));
    finestra.pack();
  }
  
  /** Collega gli event listener per reagire
   * alle sollecitazioni dell'utente. */
  protected void allacciaEventi()
  {
    scegliFile = new JFileChooser();
    scegliColore = new JColorChooser();
    scegliFile.setCurrentDirectory(new File("."));
    
    // Cambio di modo
    bottoneSelezione.addActionListener(this);
    bottoneEllisse.addActionListener(this);
    bottoneRettangolo.addActionListener(this);
    bottoneTriangolo.addActionListener(this);
    // Operazioni generali
    bottoneCarica.addActionListener(this);
    bottoneSalva.addActionListener(this);
    bottoneSvuota.addActionListener(this);
    bottoneEsci.addActionListener(this);
    // Operazioni sulla figura selezionata
    bottoneCancella.addActionListener(this);
    bottoneSu.addActionListener(this);
    bottoneGiu.addActionListener(this);
    bottoneTrasla.addActionListener(this);
    bottoneColore.addActionListener(this);
    // Interazione col mouse per disegno e selezione
    addMouseListener(this);
    addMouseMotionListener(this);
  }

// ===== Mouse e MouseMotionListener per il pannello di disegno (this)

  /** Necessario per implementare MouseListener, non fa nulla. */
  public void mouseEntered(MouseEvent ev) {}
  /** Necessario per implementare MouseListener, non fa nulla. */
  public void mouseExited(MouseEvent ev) {}
  /** Necessario per implementare MouseListener, non fa nulla. */
  public void mousePressed(MouseEvent ev) {}
  /** Necessario per implementare MouseListener, non fa nulla. */
  public void mouseReleased(MouseEvent ev) {}
  /** Necessario per implementare MouseListener, non fa nulla. */
  public void mouseDragged(MouseEvent ev) {} 

  /** Necessario per implementare MouseListener, definisce
   * che cosa succede quando l'utente fa click sul pannello di
   * disegno. L'effetto e' diverso siamo in modo inserimento,
   * se siamo in modo selezione, e se (in modo selezione) 
   * e' in corso una traslazione. */
  public void mouseClicked(MouseEvent ev)
  {
    if (traslando && (selezionata!=null))
    {
      double dx = astrattaX(x1)-selezionata.centroX();
      double dy = astrattaY(y1)-selezionata.centroY();
      selezionata.trasla(dx,dy);
      // traslando potrebbe essere uscita dai limiti
      aggiornaDimensioni(selezionata);
      traslando = false;
      bottoneTrasla.setBackground(coloreNeutro);
      mostraMessaggio(coloreNeutro, "Fine traslazione");
      repaint();
      return;
    }

    switch (tipoFigura)
    {
      case NESSUNA: // modo selezione
        prendiSelezione(ev.getX(), ev.getY());
        break;
      case ELLISSE:
        fase=processaInserimento(ev.getX(),ev.getY(), true);
        if (fase==2) // inserimento terminato
        {
          double rx = calcolaRaggioX();
          double ry = calcolaRaggioY();
          FiguraI nuovoEllisse =
               sistema.aggiungiEllisse(astrattaX(x1),astrattaY(y1), rx,ry);          
          aggiornaDimensioni(nuovoEllisse);
        }
        break;
      case RETTANG:
        fase=processaInserimento(ev.getX(),ev.getY(), true);
        if (fase==2) // inserimento terminato
        {
          sistema.aggiungiRettangolo(astrattaX(x1), astrattaY(y1),
                                     astrattaX(x2), astrattaY(y2));
        }
        break;
      case TRIANG:
        fase=processaInserimento(ev.getX(),ev.getY(), true);
        if (fase==2) // inserimento terminato
        {
          int scambio;
//****CREDO NON NECESS          if (x1>x2) {  scambio=x1; x1=x2; x2=scambio;  }
          if (y1<y2) {  scambio=y1; y1=y2; y2=scambio;  } 
          sistema.aggiungiTriangolo(astrattaX(x1), astrattaX(x2),
               astrattaY(y1), y1-y2);
        }
        break;
    } // end switch
    if (fase==2)  fase = 0; // ricomincia nuovo inserimento
    repaint();
  } 

  /** Necessaria per implementare MouseMotionListener, definisce
   * che cosa succede quando l'utente nuove il mouse sul pannello
   * di disegno. L'effetto e' diverso se siamo in modo inserimento,
   * se siamo in modo selezione, e se (in modo selezione) 
   * e' in corso una traslazione. */
  public void mouseMoved(MouseEvent ev)
  {
    coordinate.setText("("+(int)astrattaX(ev.getX())+","+(int)astrattaY(ev.getY())+")"); 
    // esiste figura selezionata e la stiamo traslando
    if (traslando && (selezionata!=null)) 
    {
      x1 = ev.getX();
      y1 = ev.getY();
      repaint();
      return;
    }
    // modo selezione (se non stiamo traslando)
    if (tipoFigura==NESSUNA) return;
    // modo inserimento, tipoFigura e' ELLISSE, RETTANG o TRIANG
    processaInserimento(ev.getX(),ev.getY(), false);
    repaint();
  } 
  
// ======================== ActionListener per i bottoni

  /** Per caricare figure da file. */
  private void caricamento()
  {
    int risposta = scegliFile.showOpenDialog(finestra);
    if (risposta==JFileChooser.APPROVE_OPTION)
    {
      try 
      {
         sistema.leggiDaFile(scegliFile.getSelectedFile().getPath());  
         repaint();
      }
      catch (Exception ex)
      {
         mostraMessaggio(coloreErrore, "Errore in lettura");
         JOptionPane.showMessageDialog(finestra, 
               "Errore "+ex.getClass().getName() + "\nleggendo "
               +scegliFile.getSelectedFile().getPath()+":\n"+ex.getMessage(),
               "Errore in lettura",  JOptionPane.ERROR_MESSAGE); 
         //ex.printStackTrace();
         mostraMessaggio(coloreNeutro,"");
      }
    }
  }
  
  /** Per salvare le figure su file. */
  private void salvataggio()
  {
    int risposta = scegliFile.showSaveDialog(finestra);
    if (risposta==JFileChooser.APPROVE_OPTION)
    {
      try
      {
         sistema.scriviSuFile(scegliFile.getSelectedFile().getPath());  
         mostraMessaggio(coloreNeutro, "Scritto su file " + scegliFile.getSelectedFile().getPath());
      }
      catch (Exception ex)
      {
         mostraMessaggio(coloreErrore, "Errore in scrittura");
         JOptionPane.showMessageDialog(finestra, 
               "Errore "+ex.getClass().getName() + "\nscrivendo "
               +scegliFile.getSelectedFile().getPath()+":\n"+ex.getMessage(),
               "Errore in scrittura",  JOptionPane.ERROR_MESSAGE); 
         //ex.printStackTrace();
         mostraMessaggio(coloreNeutro,"");
      }
    }
  }

  /** Necessario per implementare ActionListener, stabilisce
   * che cosa succede quando l'utente fa click sui vari bottoni. */
  public void actionPerformed(ActionEvent ev)
  {
    // Modo
    if (ev.getSource()==bottoneSelezione)
       entraModoSelezione();
    else if (ev.getSource()==bottoneEllisse)
       entraModoInserimento(ELLISSE);
    else if (ev.getSource()==bottoneRettangolo)
       entraModoInserimento(RETTANG);
    else if (ev.getSource()==bottoneTriangolo)
       entraModoInserimento(TRIANG);
    // Operazioni sulla figura selezionata
    else if (ev.getSource()==bottoneCancella)
    {
       sistema.elimina(selezionata);
       pulisciSelezione();
       repaint();
    }
    else if (ev.getSource()==bottoneSu)
    {
      sistema.spostaSopra(selezionata);
      repaint();
    }
    else if (ev.getSource()==bottoneGiu)
    {
      sistema.spostaSotto(selezionata);
      repaint();
    }
    else if (ev.getSource()==bottoneTrasla)
    {
      traslando = true;
      bottoneTrasla.setBackground(coloreTraslazione);
      mostraMessaggio(coloreTraslazione, "Per traslare la figura, fare click nel nuovo centro");
    }
    else if (ev.getSource()==bottoneColore)
    {
      Color nuovo = JColorChooser.showDialog(finestra, "Scegli il colore", selezionata.colore());
      if (nuovo!=null)
      {
        selezionata.cambiaColore(nuovo);
        repaint();
      }
    }
    // Operazioni generali
    else if (ev.getSource()==bottoneCarica)
      caricamento();
    else if (ev.getSource()==bottoneSalva)
      salvataggio();
    else if (ev.getSource()==bottoneSvuota)
    {
      sistema.svuota();
      if (selezionata!=null)  pulisciSelezione();
      repaint();
    }
    else if (ev.getSource()==bottoneEsci) System.exit(0);
  }

// ================ Costruttore
  
  /** Costruisce il pannello di disegno 
   * che non contiene nessuna figura da disegnare,
   * costruisce anche la finestra che lo contiene con tutti
   * gli elementi per gestire l'interazione con l'utente.<BR>
   * QUI DOVETE INTERVENIRE PER COLLEGARE LA VOSTRA CLASSE
   * CHE IMPLEMENTA SistemaI */
  public DisegnaFigure()
  {
    // ************* CAMBIARE LA RIGA QUI SOTTO
    sistema = new Sistema();
    // ************* CAMBIARE LA RIGA QUI SOPRA
    selezionata = null;
    creaFinestra();
    allacciaEventi();
    abilitazioni();
    finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    finestra.setVisible(true);
  }  

// =================== MAIN
  
  /** Fa partire il programma con inizialmente nessuna figura. */
  public static void main(String[] arg) throws Exception
  {
    if (arg.length>0)
        System.out.println("Gli argomenti forniti "
        + "sulla linea di comando vengono ignorati.");
    DisegnaFigure dis = new DisegnaFigure();
  }
    
}

