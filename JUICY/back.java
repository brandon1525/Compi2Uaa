program{
	int x,y,z;
	float a, b, c;
	float x;
	int w;
	bool xx;
	x:=23;
	y:=2+3-1;
	z:=y+7;
	x:=(5-3)*(8/2);
	y:=5+3-2*4/7-9;
	z:=8/2+15*4;
	y:=14.54;
	y:=1;
	xx:=y;
	a:=24.0+4-1*3/2+34-1;
	a++;
	++x;
	--x;
	z:=x + y;
	if (2>3)then{
		    y:=a+3;
	}else{      
	     	if(4 > 2) then{
	       		b:=3.2;
       		}else{
        			b:=5.0;
      		}fi
   		y:=y+1;
	}fi
	do{
	  	y:=(y+1)*2+1;
	  	if(a=b) then{
     			a:=a+1;
  		}else{
     			b:=b+1;
  		}fi
	}until(y=5);

	while (y=0){
  		write x;
  		do{
    			write mas+5*2;
     			if(6!=a) then{
        				a:=5;
        				read x;
     			}fi
  		}until(y=5);
	}
}