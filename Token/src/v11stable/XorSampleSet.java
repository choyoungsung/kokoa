package v11stable;

import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class XorSampleSet {
	static int dimensionInput = 2;
	static int dimensionGold = 1;
	
	INDArray inputVector;
	INDArray goldVector;
	
	public String toString(){
		return inputVector.toString() + " => " + goldVector.toString();
	}
	
	public XorSampleSet(int x1, int x2, int y1){
		inputVector = Nd4j.create(new float[]{x1, x2}, new int[]{dimensionInput,1 });
		goldVector  = Nd4j.create(new float[]{y1}, new int[]{dimensionGold,1 });
//		goldVector  = Nd4j.create(new float[]{y1, y2}, new int[]{dimensionGold,1 }); 
	}
	
	public static List<XorSampleSet> trainingList(){
		List<XorSampleSet> res = new ArrayList<XorSampleSet>();
		
		res.add(new XorSampleSet(0, 1,  0));
		res.add(new XorSampleSet(1, 0,  0));
		res.add(new XorSampleSet(0, 0,  1));
		res.add(new XorSampleSet(1, 1,  1));
		
		return res;
	}
	
	public static List<XorSampleSet> testList(){
		return trainingList();

	}
	
}
