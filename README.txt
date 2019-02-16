Zigmann Bastien <bastien.zigmann@etu.univ-grenoble-alpes.fr>
Sorin Gaëtan    <gaetan.sorin@etu.univ-grenoble-alpes.fr>

IDS TP2 : Chat


Compilation :
$ make

Lancement d'un serveur :
Depuis out/server
- Lancer rmiregistry si besoin.
$ rmiregistry
- Puis lancer le serveur
$ java ChatServer

Lancement d'un client :
Depuis out/client
$ java ChatClient <rmi host>


Nous avons implémenter les fonctionnalités suivantes :
- Choix du nom d'utilisateur à la connection.
- Salons séparés.
- Création de salon par l'utilisateur.
- Déplacement d'un salon à l'autre.
- Fichier historique commun à tous les salons
- Filtrage des messages de l'historique à la demande.
- Envoie de message à tous les clients concernés (même salon).
- Distinction de l'expediteur pour ne pas lui envoyer son propre message.
- Commande de chat pour le client
