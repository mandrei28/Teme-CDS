int n = 0;

active[2] proctype pq() {
	int i = 0, temp;
	do
	:: i < 10 -> 
			temp = n; 
			n = temp + 1; 
			i = i + 1;
	:: else -> break;
	od;
	printf("n = %d \n", n);
}
