package entity;

public class MeanCF{
	
	private final double suma;
	private final int count;
	
	public MeanCF(double suma, int count) {
		this.suma = suma;
		this.count = count;
	}
	
	public double getSuma() {
		return suma;
	}
	public int getCount() {
		return count;
	}
	public double getMean(){
		return suma / (double) count;
	}
}
