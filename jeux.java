import extensions.CSVFile ;
import extensions.File ;
class jeux extends Program{ 
    final String RED = "\u001B[31m";
    final String BLUE = "\u001B[34m";
    final String WHITE = "\u001B[0m";
    final String CYAN = "\u001B[36m";

void afficherAcceuil(){
    clear();
    print("\u001B[33m"); 
    afficherFichier("./txt/accueil.txt");
    print("\u001B[0m");        
}

void saveGame(Joueur joueur){
    CSVFile fichier = loadCSV("./csv/score.csv");
    
    int nbLignes = rowCount(fichier); 
    String[][] sauvegarde = new String[nbLignes + 1][2];
    for (int i = 0; i < nbLignes; i++) {
        sauvegarde[i][0] = getCell(fichier, i, 0);
        sauvegarde[i][1] = getCell(fichier, i, 1);
    }
    sauvegarde[nbLignes][0] = joueur.nom_joueur;
    sauvegarde[nbLignes][1] = "" + (joueur.score);
    saveCSV(sauvegarde, "./csv/score.csv");
}

//création d'un nouveau joueur
Joueur newJoueur(String nom){
    Joueur j = new Joueur();
    j.nom_joueur = nom;
    j.nb_bonne_rep = 0;
    j.nb_mauvaise_rep = 0;
    j.score = 0;
    j.suite_correcte = 0;
    return j;
}

void test_est_Un_Nombre(){
    assertTrue(est_Un_Nombre("2"));
    assertFalse(est_Un_Nombre("fromage"));
    assertFalse(est_Un_Nombre(""));
}
// on vérifie si la saisie du joueur est un nombre entier positif
boolean est_Un_Nombre(String s) {
    if (length(s) != 1){
        return false;
    }
    char c = charAt(s, 0);
    if (c < '0' || c > '9') {
        return false;
        }
    return true;
}
    
int Choix_du_joueur( int min, int max){
     int valeur = 0;
     boolean Valide = false;
     while (!Valide) {
         String saisie = readString(); 
         if (est_Un_Nombre(saisie)) {
             valeur = stringToInt(saisie); 
             if (valeur >= min && valeur <= max) {
                Valide = true;
             } else {
                 println("Le nombre doit etre entre " + min + " et " + max + ".");
             }
         } else {
             println("Veuillez entrer un chiffre valide.");
         }
     }
     return valeur;
}

void test_Charger_theme(){
    assertEquals(charger_Theme(2) , "./csv/svt.csv");
}

String charger_Theme(int num_theme){
         if(num_theme == 1){
             return "./csv/histoire.csv";
         }else if (num_theme == 2 ){
             return "./csv/svt.csv";
         } return "./csv/physique.csv";
     }

void test_verifierReponse(){
    assertFalse(verifierReponse(loadCSV("./csv/histoire.csv"), 0, "LOUIS 14"));
    assertTrue(verifierReponse(loadCSV("./csv/histoire.csv"), 0, "LoUIs Xiv"));
    assertFalse(verifierReponse(loadCSV("./csv/histoire.csv"), 0, "Sabri"));
}

boolean verifierReponse(CSVFile fichier,int ligne,String reponseDuJoueur) {      
    String reponseAttendue =getCell(fichier, ligne, 1);
    if (equals(toUpperCase(reponseDuJoueur) , toUpperCase(reponseAttendue))) {
        return true;
    } else {
        return false;
    }
}

void annoncerResultat(boolean est_correct, Joueur joueur) {
    if (est_correct==true) {
        joueur.nb_bonne_rep ++;
        println(recupererTexte("./txt/phrases.txt", 8));
        }
    else {
        joueur.nb_mauvaise_rep ++;
        println(recupererTexte("./txt/phrases.txt", 9));
    }
}

void joueUnTour(CSVFile theme,int num_question, Joueur joueur){
    println(getCell(theme, num_question, 0));
    String partie1 = recupererTexte("./txt/phrases.txt", 4); 
    String partie2 = recupererTexte("./txt/phrases.txt", 6);
    print(BLUE+partie1 + joueur.nom_joueur + partie2 + WHITE);
    String rep_user = toUpperCase(readString());
    if(verifierReponse(theme , num_question,rep_user)){
        joueur.score = joueur.score + getPointsParQuestion(num_question);
    }
    annoncerResultat(verifierReponse(theme , num_question,rep_user),joueur);      
}

//Important d'en avoir 2 version car le gagnant n'est pas déterminer de la même manière selon le mode !
void gagnant(Joueur j){   //renvoie true si gagner , false si perdu 
    if(j.nb_mauvaise_rep != 3){         
        afficherFichier("./txt/victoire.txt");
    }else{
        afficherFichier("./txt/defaite.txt");
    }
}

void gagnant(Joueur j1 , Joueur j2){  
    String phraseGagne = recupererTexte("./txt/phrases.txt", 11);
    String phrasePerd  = recupererTexte("./txt/phrases.txt", 12);
    if (j1.score > j2.score){
        println(CYAN + j1.nom_joueur + WHITE + phraseGagne + " (" + j1.score + " pts)");
        println(RED + j2.nom_joueur + WHITE + phrasePerd + " (" + j2.score + " pts)");
    }
    else if(j2.score > j1.score){
        println(CYAN + j2.nom_joueur + WHITE + phraseGagne + " (" + j2.score + " pts)");
        println(RED + j1.nom_joueur + WHITE + phrasePerd + " (" + j1.score + " pts)");
    }
    else{ 
        println(CYAN + recupererTexte("./txt/phrases.txt", 13));
    }
}
// Fonction pour lire et afficher les fichier .txt
void afficherFichier(String chemin) {
    File f = new File(chemin); 
    while (ready(f)) {         
        String ligne = readLine(f);
        println(ligne);        
    }
}

String recupererTexte(String chemin, int ligneVoulue){
    File f = new File(chemin);
    String contenu = "";
    int i = 1;
    boolean trouve = false;
    
    while(ready(f) && !trouve){
        String l = readLine(f);
        if(i == ligneVoulue){
            contenu = l;
            trouve = true;
        }
        i = i + 1;
    }
    return ( contenu);
}

void trierParScores(String[][] scores){   //modifie le tabscore pour classer par score
//a utiliser après avoir mis a jour les score et rappeler un loadScore en parametreS 
for (int i = 0; i < length(scores) - 1; i++) {
    int idxMax = i;
    for (int j = i + 1; j < length(scores); j++) {
        if (stringToInt(scores[j][1]) > stringToInt(scores[idxMax][1])) {
            idxMax = j;
        }
    }
    // échange les lignes i et idxMax
    String tempNom = scores[i][0];
    String tempScore = scores[i][1];
    scores[i][0] = scores[idxMax][0];
    scores[i][1] = scores[idxMax][1];
    scores[idxMax][0] = tempNom;
    scores[idxMax][1] = tempScore;
    }
}

String[][] loadScore(){
    CSVFile scoreCSV = loadCSV("./csv/score.csv");
    int nbLignes = rowCount(scoreCSV) - 1;         
    String[][] tab_score = new String[nbLignes][2];
    int idxTab = 0;
    for (int idxLig = 1; idxLig < rowCount(scoreCSV); idxLig++) { 
        tab_score[idxTab][0] = getCell(scoreCSV, idxLig, 0); // nom
        tab_score[idxTab][1] = getCell(scoreCSV, idxLig, 1); // score
        idxTab = idxTab + 1;
    }
    return tab_score;
}

void afficherScores(String[][] scores){
    String cheminScoreAffichage = "./txt/score_affichage.txt";
    for (int i = 1; i <= 5; i++) {
        println(recupererTexte(cheminScoreAffichage, i));
    }
    
    int maxAfficher = 5;
    if (length(scores) < maxAfficher) {
        maxAfficher = length(scores);
    }
    String deb_fin =recupererTexte(cheminScoreAffichage, 6);      
    for (int i = 0; i < maxAfficher; i++) {
        String nom = scores[i][0]; 
        while(length(nom)<16){
            nom +=" ";                
        }
        String score = scores[i][1]; 
        while(length(score)<2){
            score +=" ";                
        }
        println(deb_fin+"\t"+(i + 1) + ".\t" + nom + "\t"+ score+"    "+deb_fin);
    }
    println(recupererTexte(cheminScoreAffichage, 8));
}

// implémentation de la fonction pour nettoyer le terminal
void clear(){
    print("\033[H\033[2J\033[3J"); // séquence ANSI pour reset le terminal
}

void test_verifierPseudo(){
    assertFalse(VerifierPseudo("ijava c est cool"));
    assertTrue(VerifierPseudo("SABRItheGOAT"));
    assertFalse(VerifierPseudo("Le yiyanic"));
}
boolean VerifierPseudo(String name_j){
    for(int i =0; i< length(name_j);i++)
        if(equals(substring(name_j,i,i+1)," ")){
            return false;
        }
    return true;                
}

void verifSuiteCorrecte(Joueur j,int scoreAvant){
    // Vérifie si le joueur a bien répondu au tour précédent
    if (j.nb_bonne_rep > scoreAvant) {
        j.suite_correcte = j.suite_correcte + 1;
    } else {
        j.suite_correcte = 0; // Erreur = on remet le compteur à 0
    }
}

// Cette fonction calcule uniquement l'indice de la prochaine question
int gererProgression(int questionActuelle, Joueur j, int scoreAvant) {
    verifSuiteCorrecte(j,scoreAvant);
    // Vérifier si on doit sauter un niveau (5 bonnes réponses d'affilée)
    if (j.suite_correcte == 5) {
        j.suite_correcte = 0; 
        //lignes du CSV
        if (questionActuelle < 20) {
            println('\n'+CYAN + '\t'+'\t'+">>> NIVEAU MOYEN  ! <<<" + WHITE);
            return 20;
        } 
        else if (questionActuelle < 40) {
            println('\n'+CYAN +'\t'+'\t'+ ">>> NIVEAU DIFFICILE ! COURAGE ! <<<" + WHITE);
            return 40;
        }
    }
    return questionActuelle + 1;
}

// Cette fonction c est pour calculer les points selon la difficulté de la question 
int getPointsParQuestion(int num_question) {
    if (num_question < 20) {
        return 1; // Facile = 1 point
    } else if (num_question < 40) {
        return 2; // Moyen = 2 points
    } else {
        return 3; // Difficile = 3 points
    }
}

void DebPartie(){
    clear();
    print("\u001B[33m"); 
    afficherFichier("./txt/accueil.txt");
    String[][] classement = loadScore();
    trierParScores(classement);
    afficherScores(classement);
    print(recupererTexte("./txt/phrases.txt", 19));
    readString();
    print("\u001B[0m");
    
    afficherAcceuil();
    afficherFichier("./txt/mode.txt");
}

void j1(CSVFile theme){
    afficherAcceuil();
    print(recupererTexte("./txt/phrases.txt", 1));
    String name_j = readString();
    while(name_j=="" || VerifierPseudo(name_j)==false){
        afficherAcceuil();
        print(recupererTexte("./txt/phrases.txt", 18));
        name_j = readString();
    }
    Joueur j = newJoueur(name_j);
    afficherAcceuil();
    afficherFichier("./txt/regles_solo.txt");
    int num_question =0;
    println('\n'+CYAN + '\t'+'\t'+">>> NIVEAU FACILE  ! <<<" + WHITE);
    while(j.nb_mauvaise_rep != 3 && j.nb_bonne_rep != 15){                       
        print(RED+recupererTexte("./txt/phrases.txt", 17)+WHITE);
        int scoreAvant = j.nb_bonne_rep;
        joueUnTour(theme, num_question, j);
        num_question = gererProgression(num_question, j, scoreAvant);
    }
    afficherAcceuil();             
    println(recupererTexte("./txt/phrases.txt", 7) + j.score);
    gagnant(j);
    saveGame(j);
}

void j2(CSVFile theme){
    afficherAcceuil();
    print(recupererTexte("./txt/phrases.txt", 2));
    String name_j1 = readString();
    Joueur j1 = newJoueur(name_j1);
    print(recupererTexte("./txt/phrases.txt", 3));
    String name_j2 = readString();
    Joueur j2 = newJoueur(name_j2);
    println("");
    afficherAcceuil();
    afficherFichier("./txt/regles_multi.txt");
    int num_question_j1 = 0;
    int num_question_j2 = 0;      
    println('\n'+CYAN + '\t'+'\t'+">>> NIVEAU FACILE  ! <<<" + WHITE);
    while(j1.nb_mauvaise_rep != 3 && j1.nb_bonne_rep != 15){
        print(RED+recupererTexte("./txt/phrases.txt", 17)+WHITE);
        joueUnTour(theme,num_question_j1,j1);
        num_question_j1 = num_question_j1 + 1 ;
    }
    
    println(RED+recupererTexte("./txt/phrases.txt",16)+WHITE);
    readString();
    afficherAcceuil();
    String partie1 = recupererTexte("./txt/phrases.txt", 14);
    String partie2 = recupererTexte("./txt/phrases.txt", 15); 
    
    println(RED+partie1 +BLUE+ j2.nom_joueur +RED+ partie2 +BLUE+ j1.nom_joueur+WHITE);
    
    println('\n'+CYAN + '\t'+'\t'+">>> NIVEAU FACILE  ! <<<" + WHITE);
    
    while(j2.nb_mauvaise_rep != 3 && j2.nb_bonne_rep<=j1.nb_bonne_rep){
        print(RED+recupererTexte("./txt/phrases.txt", 17)+WHITE);
        joueUnTour(theme,num_question_j2,j2);
        num_question_j2 = num_question_j2 + 1 ;
    } 
    afficherAcceuil();
    gagnant(j1,j2);
    saveGame(j1);
    saveGame(j2);
}

void algorithm(){
    boolean continuer = true;

    while(continuer){
        DebPartie();
        int nb_joueur = Choix_du_joueur(1,2);

        afficherAcceuil();
        afficherFichier("./txt/menu_themes.txt");
        // CSVFile theme =loadCSV(charger_Theme(Choix_du_joueur(1,3)));
        int choix_j = Choix_du_joueur(1,3);
        if(nb_joueur ==1){
            CSVFile theme = loadCSV(charger_Theme(choix_j));
            j1(theme);
        }
        else{
            CSVFile theme = loadCSV(charger_Theme(choix_j));
            j2(theme);
            
        }
        println("");
            // --- Menu de fin ---
            print(CYAN+recupererTexte("./txt/phrases.txt", 10)+WHITE); 
            String saisie = toUpperCase(readString()); 

            if (equals(saisie, "STOP")) {
                continuer = false;
                print("\u001B[33m");
                println("Le Sphinx retourne dans son sommeil...");
                print("\u001B[0m");
            }
        }
    }
}
