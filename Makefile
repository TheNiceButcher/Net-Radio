Diffuseur: Diffuseur.java Un_Diffuseur.class
	javac Diffuseur.java
Un_Diffuseur.class: Un_Diffuseur.java Diffuseur_Multi.class Diffuseur_TCP.class
	javac Un_Diffuseur.java
Diffuseur_Multi.class: Diffuseur_Multi.java
	javac Diffuseur_Multi.java
Diffuseur_TCP.class: Diffuseur_Client.class Diffuseur_Gestionnaire.class Diffuseur_TCP.java
	javac Diffuseur_TCP.java
Diffuseur_Client.class: Diffuseur_Client.java
	javac Diffuseur_Client.java
Diffuseur_Gestionnaire.class: Diffuseur_Gestionnaire.java
	javac Diffuseur_Gestionnaire.java
Client: Client.c Client.h
	gcc -Wall Client.c -o client -pthread
Gestionnaire: Gestionnaire.java Gestionnaire_Client.class
	javac Gestionnaire.java
Gestionnaire_Client.class: Gestionnaire_Client.java Gestionnaire_Diffuseur.class
	javac Gestionnaire_Client.java
Gestionnaire_Diffuseur.class: Gestionnaire_Diffuseur.java
	javac Gestionnaire_Diffuseur.java
clean:
	rm *.class client
complet: Diffuseur Client Gestionnaire
