bool wantp = false, wantq = false;
byte turn = 1;

active proctype p() {
	do
	:: wantp = true;
		do
		:: wantq ->
			if
			:: (turn == 2) ->
				wantp = false; turn == 1; wantp = true
			:: else
			fi
		:: else -> break
		od;
		printf("P se afla in sectiunea critica\n");
		turn = 2;
		wantp = false
	od
}
active proctype q() {
	do
	:: wantq = true;
		do
		:: wantp ->
			if
			:: (turn == 1) ->
				wantq = false; turn == 2; wantq = true
			:: else
			fi
		:: else -> break
		od;
		printf("Q se afla in sectiunea critica\n");
		turn = 1;
		wantq = false
	od
}