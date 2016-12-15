package v11stable;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.DefaultRandom;
import org.nd4j.linalg.factory.Nd4j;

public class NetWB {
	public INDArray weight;
	public INDArray bias;
	
	public static NetWB newInstanceByRandOnlyWeight(int rowSize, int colSize, double min, double max){
		NetWB nw = new NetWB();
		nw.weight = Nd4j.rand(rowSize, colSize, min, max, new DefaultRandom());
		nw.bias = Nd4j.zeros(rowSize, 1);//Nd4j.rand(rowSize, 1, min, max, new DefaultRandom());
		return nw;
	}
	
	public static NetWB newInstanceByRand(int rowSize, int colSize, double min, double max){
		NetWB nw = new NetWB();
		nw.weight = Nd4j.rand(rowSize, colSize, min, max, new DefaultRandom());
		nw.bias = Nd4j.rand(rowSize, 1, min, max, new DefaultRandom());
		return nw;
	}
	
	public static NetWB newInstanceByZero(int rowSize, int colSize){
		NetWB nw = new NetWB();
		nw.weight = Nd4j.zeros(rowSize, colSize);
		nw.bias = Nd4j.zeros(rowSize, 1);
		return nw;
	}
	
	
	public static NetWB newInstanceTestN1(){
		NetWB n1 = new NetWB();
		
		n1.weight = Nd4j.zeros(2, 2);
		n1.weight.putRow(0, Nd4j.create(new double[]{0.15f, 0.20f}));
		n1.weight.putRow(1, Nd4j.create(new double[]{0.25f, 0.30f}));
		n1.bias = Nd4j.zeros(2, 1);
		n1.bias.putRow(0, Nd4j.create(new double[]{0.35f})); //0.35f;
		n1.bias.putRow(1, Nd4j.create(new double[]{0.35f}));
		return n1;
	}
	
	public static NetWB newInstanceTestN2(){
		NetWB n2 = new NetWB();
		n2.weight = Nd4j.zeros(2, 2);
		n2.weight.putRow(0, Nd4j.create(new double[]{0.40f, 0.45f}));
		n2.weight.putRow(1, Nd4j.create(new double[]{0.50f, 0.55f}));
		n2.bias = Nd4j.zeros(2, 1);
		n2.bias.putRow(0, Nd4j.create(new double[]{0.60f})); //= 0.60f;
		n2.bias.putRow(1, Nd4j.create(new double[]{0.60f}));
		return n2;
	}
	
	public String toString(){
		return "w=" + weight.toString() + " , b=" + bias.toString();
	}
}
