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


#define SIZE_TYPE 4
int main(int argc, char const *argv[]) {
	if (argc != 2)
	{
		printf("Veuillez indiquer un fichier de configuration\n");
		return 0;
	}
	char * fichier = malloc(strlen(argv[1]));
	strcpy(fichier,argv[1]);
	fichier[strlen(argv[1])] = '\0';
	int fd = open(fichier,O_RDONLY);
	if(fd == -1)
	{
		perror("Fichier de configuration inexistant");
		return 0;
	}
	char contenu_fich[55];
	int a = read(fd,contenu_fich,55);
	contenu_fich[a] = '\0';
	int indexes[5] = { 0 };
	int i = 0;
	int j = 0;
	char id[9], addr_multi[16], port_multi[5],addr_cli[16],port_tcp[5];
	while (contenu_fich[i] != '\0')
	{
		while(contenu_fich[i] != '\n')
		{
			i++;
		}
		indexes[j] = i;
		i++;
		j++;
	}
	for (size_t i = 0; i < 5; i++) {
		int prec = 0;
		int act = indexes[i];
		if(i != 0)
		{
			prec = indexes[i-1] + 1;
		}
		switch (i) {
			case 0:
				strncpy(id,contenu_fich + prec,act - prec);
				id[act - prec] = '\0';
				break;
			case 1:
				strncpy(addr_multi,contenu_fich + prec,act - prec);
				addr_multi[act - prec] = '\0';
				break;
			case 2:
				strncpy(port_multi,contenu_fich + prec,act - prec);
				port_multi[act - prec] = '\0';
				break;
			case 3:
				strncpy(addr_cli,contenu_fich + prec,act - prec);
				addr_cli[act - prec] = '\0';
				break;
			case 4:
				strncpy(port_tcp,contenu_fich + prec,act - prec);
				port_tcp[act - prec] = '\0';
				break;
		}
	}
	int sock=socket(PF_INET,SOCK_DGRAM,0);
	int ok=1;
	int r=setsockopt(sock,SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok));
	struct sockaddr_in address_sock;
	address_sock.sin_family=AF_INET;
	address_sock.sin_port=htons(atoi(port_multi));
	address_sock.sin_addr.s_addr=htonl(INADDR_ANY);
	r=bind(sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
	struct ip_mreq mreq;
	mreq.imr_multiaddr.s_addr=inet_addr(addr_multi);
	mreq.imr_interface.s_addr=htonl(INADDR_ANY);
	r=setsockopt(sock,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq));
	char tampon[1000];
	while(1){
		int rec=recv(sock,tampon,1000,0);
		tampon[rec]='\0';
		printf("Message recu : %s\n",tampon);
	}
	return 0;
}
