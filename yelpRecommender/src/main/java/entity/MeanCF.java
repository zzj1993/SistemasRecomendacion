package entity;

public class MeanCF{
	
	private double suma;
	private int count;
	
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
	
	public void removeMean(int value){
		suma -= value;
		count--;
	}
}
