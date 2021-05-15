# Net-Radio
## Compilation
Pour compiler l'intégralité du projet, il suffit de lancer la commande `make net-radio`.
Si on veut juste compiler une entité du projet (diffuseur,client ou gestionnaire),
il faut taper la commande `make entite` où `entite` peut être `Diffuseur`, `Client`
ou `Gestionnaire`.
La commande `make clean` supprimera tous les fichiers produits lors de la compilation.
## Exécution
* Une fois la compilation effectuée, on peut lancer les différentes entités comme suit:
	* `java Diffuseur diff-config` pour le diffuseur, avec `diff-config` le nom du fichier de
configuration de la forme explicitée dans la partie dédiée.
	* `./client client-config (path)` pour le client, avec `client-config` le nom
du fichier de configuration voulue (dont la forme sera expliquée dans la partie dédiée),
et `path` le chemin du fichier où l'on veut rediriger les messages reçus en multi-diffusion.
`path`. `path` est optionnel; s'il n'est pas renseigné, les messages seront affichés
dans la sortie standard.
	* `java Gestionnaire port nb_max_diff` où `port` est le numéro du port où il écoute et
`nb_max_diff` est le nombre maximal de diffuseur qu'il peut stocker.
## Utilisation
### Client
* Quand on lance le client, on se retrouve face un menu nous proposant trois choix:
	* Envoi Message (touche 1): Le client envoie un message au diffuseur pour le diffuser
sur le canal de multi-diffusion.
	* Dernier Message (touche 2): On demande au diffuseur les derniers messages qu'il
a envoyés. Pour ce faire, après le choix 2, on demande le nombre voulu (entre 0 et ).
Les messages seront affichées dans la sortie standard.
	* Liste des diffuseurs (touche 3): Une fois ce choix réalisé, on demande l'adresse
et le port du gestionnaire avec lequel on souhaite interagir. On affiche alors les
diffuseurs enregistrés (leur nom,leur adresse et port de multi-diffusion,leur adresse et
port pour recevoir les messages des clients) dans ce gestionnaire.

Pour quitter le client, il faut .
### Gestionnaire
Le gestionnaire ne demande pas de manipulations pendant son exécution. Pour le quitter,

### Diffuseur
Une fois lancé, le diffuseur demande à l'utilisateur de taper sur la touche `1` pour
s'enregistrer auprès d'un gestionnaire. Il demande alors l'adresse et le port sur lesquels on peut contacter le gestionnaire. Pour le quitter, .

## Architecture     
