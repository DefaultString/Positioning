public class Positioning 
{
	// Distance between adjacent beacons on one side(N,S)
	private double a;
	// Distance between adjacent beacons on the other side(E,W)
	private double b;
	
	double x = -1;
	
	double y = -1;
	
	// North Edge
	double[] N = new double[Definition.BEACONS_PER_EDGE];
	
	//South Edge
	double[] S = new double[Definition.BEACONS_PER_EDGE];
	
	// East Edge
	double[] E = new double[Definition.BEACONS_PER_EDGE];
	
	// West Edge
	double[] W = new double[Definition.BEACONS_PER_EDGE];
	
	public Positioning(double dist[], double width, double length)
	{
		int i;
		
		int n = 0;
		
		int s = 0;
		
		int w = 0;
		
		int e = 0;
		
		if(dist.length < Definition.NUM_OF_BEACONS)
		{
			this.a = 0;
			
			this.b = 0;
			
			for(i = 0; i < Definition.BEACONS_PER_EDGE; i++)
			{
				this.N[i] = 0;
				this.S[i] = 0;
				this.E[i] = 0;
				this.W[i] = 0;
			}
			
			System.out.println("Invalid data!");
			
			return;
		}
		
		this.a = length/(Definition.BEACONS_PER_EDGE - 1);
		
		this.b = width/(Definition.BEACONS_PER_EDGE - 1);
		
		for(i = 0; i < Definition.NUM_OF_BEACONS; i++)
		{	
			if( i == 0)
			{
				// NW Corner
				this.N[n] = dist[i];
				this.W[Definition.BEACONS_PER_EDGE - 1] = dist[i];
				
				n++;
			}
			else if( (i > 0) && ( i <  (Definition.BEACONS_PER_EDGE - 1) ))
			{
				// Record the distance to Beacons of the North Edge
				this.N[n] = dist[i];
				
				n++;
			}
			else if( i == (Definition.BEACONS_PER_EDGE - 1))
			{
				// NE Corner
				this.E[e] = dist[i];
				this.N[Definition.BEACONS_PER_EDGE - 1] = dist[i];
				
				e++;
			}
			else if( ( i > (Definition.BEACONS_PER_EDGE - 1)) && ( i < (Definition.BEACONS_PER_EDGE * 2) - 2))
			{
				// Record the distance to Beacons of the East Edge
				this.E[e] = dist[i];
				
				e++;
			}
			else if( i == (Definition.BEACONS_PER_EDGE * 2) - 2)
			{
				// SE Corner
				this.S[s] = dist[i];
				this.E[Definition.BEACONS_PER_EDGE - 1] = dist[i];
				
				s++;
			}
			else if( ( i > ((Definition.BEACONS_PER_EDGE * 2) - 2)) && ( i < ((Definition.BEACONS_PER_EDGE * 3) - 3)))
			{
				// Record the distance to Beacons of the South Edge
				this.S[s] = dist[i];
				
				s++;				
			}
			else if( i == ((Definition.BEACONS_PER_EDGE * 3) - 3))
			{
				// SW Corner
				this.W[w] = dist[i];
				this.S[Definition.BEACONS_PER_EDGE - 1] = dist[i];
				
				w++;
			}
			else
			{
				// Record the distance to Beacons of the West Edge
				this.W[w] = dist[i];
				
				w++;
			}
		}
	}
	
	// Calculate Indoor Coordinate
	public void CalcCoordinate()
	{
		int i;
		
		double x_sum = 0;
		
		double y_sum = 0;
		
		// Calculate DistN
		double[] distN = this.CalcTriangle(this.N, a);
						
		// Calculate DistS
		double[] distS = this.CalcTriangle(this.S, a);
				
		// Calculate DistE
		double[] distE = this.CalcTriangle(this.E, b);
			
		// Calculate DistW
		double[] distW = this.CalcTriangle(this.W, b);
		
		for(i = 0; i < Definition.TRIAGLES_PER_EDGE; i++)
		{
			x_sum = x_sum + ( ( this.a * ( Definition.BEACONS_PER_EDGE - 1)) - distE[i]) + distW[i];
			
			y_sum = y_sum + ( ( this.b * ( Definition.BEACONS_PER_EDGE - 1)) - distN[i]) + distS[i];
		}
		
		this.x = x_sum/( 2 * (Definition.TRIAGLES_PER_EDGE));
		
		this.y = y_sum/( 2 * (Definition.TRIAGLES_PER_EDGE));
	}
	
	private double[] CalcTriangle(double[] d, double unit)
	{
		int i, j;
		
		int n = 0;
		
		double c;
		
		double[] result = new double[Definition.TRIAGLES_PER_EDGE];
		
		for( i = 0; i < (Definition.BEACONS_PER_EDGE - 1); i++)
		{
			for(j = (i+1); j < Definition.BEACONS_PER_EDGE; j++)
			{
				c = unit * (j - i);
				
				if(d[i] + d[j] <= c)
				{
					// Cannot form an Triangle
					result[n] = Math.min(N[i], N[j]);
				}
				else
				{
					result[n] = (Math.sqrt(1 - Math.pow(this.cos(N[i], N[j], c), 2))) * N[j];
				}
				
				n++;
			}
		}
		
		for(i = 0; i < Definition.TRIAGLES_PER_EDGE; i++)
		{
			System.out.println("CT: " + result[i]);
		}
		
		return result;
	}
	
	private double cos(double a, double b, double c)
	{
		double result;
		
		if( ( (a+b) > c) && ( (a+c) > b) && ( (b+c) > a))
		{
			result = ( ( Math.pow(c, 2)) + ( Math.pow(b, 2)) - ( Math.pow(a, 2)) ) / ( 2 * b * c) ;
			
			System.out.println("COS:"+ result);
		}
		else
		{
			result = 0;
		}
				
		return result;
	}
	
	public double GetX()
	{
		return this.x;
	}
	
	public double GetY()
	{
		return this.y;
	}
	
	/* ================================= MAIN ================================== */
	
	public static void main(String[] args)
	{
		int i;
		
		double[] dist = new double[Definition.NUM_OF_BEACONS];
		
		for(i=0; i<Definition.NUM_OF_BEACONS; i++)
		{
			dist[i] = (Math.abs(Math.random()) * 10 % 8);
			
			System.out.println("dist[" + i + "] = " + dist[i]);
		}
		
		Positioning P = new Positioning(dist, 4.0, 4.0);
		
		for(i=0; i<Definition.BEACONS_PER_EDGE; i++)
		{			
			System.out.println("N[" + i + "] = " + P.N[i]);
		}
		
		for(i=0; i<Definition.BEACONS_PER_EDGE; i++)
		{			
			System.out.println("E[" + i + "] = " + P.E[i]);
		}
		
		for(i=0; i<Definition.BEACONS_PER_EDGE; i++)
		{			
			System.out.println("S[" + i + "] = " + P.S[i]);
		}
		
		for(i=0; i<Definition.BEACONS_PER_EDGE; i++)
		{			
			System.out.println("W[" + i + "] = " + P.W[i]);
		}
		
		P.CalcCoordinate();
		
		System.out.println("Coordinate:");
		System.out.println("X:" + P.x + "  Y:" + P.y);
	}
}
