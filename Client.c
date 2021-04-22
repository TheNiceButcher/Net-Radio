#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <pthread.h>
#include "Client.h"
Client  * client;
void * multi_cast(void * args)
{
	int sock = socket(PF_INET,SOCK_DGRAM,0);
	int ok=1;
	int r=setsockopt(sock,SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok));
	if(r == -1)
	{
		perror("Erreur setsockopt");
		exit(EXIT_FAILURE);
	}
	struct sockaddr_in address_sock;
	address_sock.sin_family=AF_INET;
	address_sock.sin_port=htons(atoi(client->port_multi));
	address_sock.sin_addr.s_addr=htonl(INADDR_ANY);
	r=bind(sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
	struct ip_mreq mreq;
	mreq.imr_multiaddr.s_addr=inet_addr(client->addr_multi);
	mreq.imr_interface.s_addr=htonl(INADDR_ANY);
	r=setsockopt(sock,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq));
	int fd = 0;
	memcpy(&fd,args,sizeof(int));
	char tampon[1000];
	while(1){
		int rec=recv(sock,tampon,1000,0);
		if(rec < 0)
			break;
		tampon[rec]='\0';
		write(fd,tampon,strlen(tampon));
	}
	if(fd != 1)
	{
		close(fd);
	}
	return NULL;
}
void * tcp(void * args)
{
	struct sockaddr_in adress_sock;
	adress_sock.sin_family = AF_INET;
	adress_sock.sin_port = htons(atoi(client->port_tcp));
	inet_aton(client->addr_diff,&adress_sock.sin_addr);
	int sock = 0;
	while(1)
	{
		sleep(10);
		sock = socket(PF_INET,SOCK_STREAM,0);
		int r = connect(sock,(struct sockaddr *)&adress_sock,sizeof(struct sockaddr_in));
		if(r != -1)
		{
			char to_send[SIZE_MESS + 2];
			char message[SIZE_MSG+1];
			sprintf(message,"Coucou");
			for(int i = strlen(message); i < SIZE_MSG;i++)
			{
				message[i] = '#';
			}
			message[SIZE_MSG] = '\0';
			sprintf(to_send,"MESS %s %s\r\n",client->id,message);
			char mess[5];
			sleep(10);
			send(sock,to_send,SIZE_MESS + 2,0);
			int r = recv(sock,mess,SIZE_TYPE,0);
			mess[r] = '\0';
			printf("%s\n",mess);
			close(sock);
		}
		else
		{
			perror("Connect TCP");
			break;
		}
	}
	return NULL;
}
int main(int argc, char const *argv[]) {
	if (argc < 2)
	{
		printf("Veuillez indiquer un fichier de configuration\n");
		return 0;
	}
	char * fichier = malloc(strlen(argv[1]));
	strcpy(fichier,argv[1]);
	fichier[strlen(argv[1])] = '\0';
	client = create_client(fichier);
	if(client == NULL)
	{
		write(STDERR_FILENO,"Fichier de configuration incorrect",35);
		return EXIT_FAILURE;
	}
	int fd_multi = 1;
	if(argc == 3)
	{
		fd_multi = open(argv[2], O_WRONLY | O_CREAT | O_TRUNC, S_IRWXU);
		if(fd_multi == -1)
		{
			perror("Fichier pour afficher les messages de multi_cast invalides");
			return EXIT_FAILURE;
		}
	}
	pthread_t t1,t2;
	pthread_create(&t1,NULL,multi_cast,&fd_multi);
	pthread_create(&t2,NULL,tcp,NULL);
	pthread_join(t1,NULL);
	pthread_join(t2,NULL);
	free(client);
	return 0;
}
