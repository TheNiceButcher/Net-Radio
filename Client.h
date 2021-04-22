#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#define SIZE_TYPE 4
#define SIZE_ID 8
#define SIZE_IP 15
#define SIZE_MSG 140
#define SIZE_PORT 4
#define SIZE_NMSG_DIFF 4
#define SIZE_NMSG_LAST 3
#define SIZE_MESS (SIZE_TYPE + SIZE_ID + SIZE_MSG + 2)
#define SIZE_DIFF (SIZE_TYPE + SIZE_NMSG_DIFF + SIZE_ID + SIZE_MSG + 3)
typedef struct Client_t{
	char id[SIZE_ID + 1];
	char addr_multi[SIZE_IP + 1];
	char port_multi[SIZE_PORT + 1];
	char addr_diff[SIZE_IP + 1];
	char port_tcp[SIZE_PORT + 1];
} Client;

Client * create_client(char * filename)
{
	int fd = open(filename,O_RDONLY);
	if(fd == -1)
	{
		return NULL;
	}
	char contenu_fich[55];
	int a = read(fd,contenu_fich,55);
	contenu_fich[a] = '\0';
	int indexes[5] = { 0 };
	int i = 0;
	int j = 0;
	char id[SIZE_ID + 1], addr_multi[SIZE_IP + 1], port_multi[SIZE_PORT + 1],addr_diff[SIZE_IP + 1],port_tcp[SIZE_PORT + 1];
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
		int taille = act - prec;
		switch (i) {
			case 0:
				if (taille > SIZE_ID)
				{
					return NULL;
				}
				strncpy(id,contenu_fich + prec,taille);
				id[taille] = '\0';
				break;
			case 1:
				if (taille > SIZE_IP)
				{
					return NULL;
				}
				strncpy(addr_multi,contenu_fich + prec,taille);
				addr_multi[taille] = '\0';
				break;
			case 2:
				if (taille > SIZE_PORT)
				{
					return NULL;
				}
				strncpy(port_multi,contenu_fich + prec,taille);
				port_multi[taille] = '\0';
				break;
			case 3:
				if (taille > SIZE_IP)
				{
					return NULL;
				}
				strncpy(addr_diff,contenu_fich + prec,taille);
				addr_diff[taille] = '\0';
				break;
			case 4:
				if (taille > SIZE_PORT)
				{
					return NULL;
				}
				strncpy(port_tcp,contenu_fich + prec,taille);
				port_tcp[taille] = '\0';
				break;
		}
	}
	Client * client = malloc(sizeof(Client));
	sprintf(client->id,"%s",id);
	sprintf(client->addr_multi,"%s",addr_multi);
	sprintf(client->addr_diff,"%s",addr_diff);
	sprintf(client->port_multi,"%s",port_multi);
	sprintf(client->port_tcp,"%s",port_tcp);
	return client;
}
int verif_num(char * nb, int lg)
{
	if(strlen(nb) != lg)
	{
		return 0;
	}
	for(int i = 0; i < lg; i++)
	{
		if(nb[i] < '0' && nb[i] > '9')
		{
			return 0;
		}
	}
	return 1;
}
void verif_diff(int fd, char message[SIZE_DIFF + 2])
{
	char type_mess[5];
	memcpy(type_mess,message,SIZE_TYPE);
	type_mess[4] = '\0';
	if(strcmp(type_mess,"DIFF"))
	{
		write(fd,"Mauvais message DIFF\n",22);
		return;
	}
	if(message[SIZE_DIFF + 1] != '\n' || message[SIZE_DIFF] != '\r')
	{
		write(fd,"Ne finis pas bien\n",19);
		return;
	}
	char id[SIZE_ID+1],num_msg[SIZE_NMSG_DIFF+1],msg[SIZE_MSG+1];
	memcpy(num_msg,message + 5,SIZE_NMSG_DIFF);
	memcpy(id,message + 10,SIZE_ID);
	memcpy(msg,message + 20,SIZE_MSG);
	id[SIZE_ID] = '\0';
	num_msg[SIZE_NMSG_DIFF] = '\0';
	msg[SIZE_MSG] = '\0';
	if(message[4] != ' ' || message[9] != ' ' || message[18] != ' ')
	{
		write(fd,"Mauvais Format\n",16);
		return;
	}
	if(!verif_num(num_msg,4))
	{
		write(fd,"Mauvais numero message\n",25);
		return;
	}
}
