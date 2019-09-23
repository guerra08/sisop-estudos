#include <stdio.h> 
#include <sys/types.h> 
#include <unistd.h> 

int main(){

	int pidF = fork();
	if(pidF < 0){
		printf("Erro no fork()");
		return 1;
	}
	else if(pidF == 0){
		printf("Oi, eu sou o filho!\n");
	}
	else{
		printf("Oi, eu sou o pai!\n");
	}
	return 0;
}
