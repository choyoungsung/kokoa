package v11stable;

import java.util.ArrayList;
import java.util.List;

import job.util.Nd4jUtil;

import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;


public class FeedForwardNetwork {
	static Logger log = Logger.getLogger("simple");
	
	List<Layer> layerList;
	
	public void addLayer(Layer layer){
		if(layerList==null){
			layerList = new ArrayList<Layer>();
		}
		layerList.add(layer);
	}
	
	public void printWeight(String title){
		log.info(title);
		for(int i=0;i<layerList.size();i++){
			layerList.get(i).print();
		}
	}
	
	private double getLearningRate(double prevLearningRate, double minus, double minLearningRate){
		double val = prevLearningRate - minus;
		return Math.max(minLearningRate, val);
	}
	
	
	public void trainByOnlineUpdate(List<XorSampleSet> samples, int iterNum, double initLearningRate, double minus, double minLearningRate) throws Exception{
		double learningRate = initLearningRate;
		for(int i=0;i<iterNum;i++){
			printWeight("before." + i );
			learningRate = getLearningRate(learningRate, minus, minLearningRate);
			trainByOnlineUpdate(samples, learningRate);
			printWeight("after." + i );
		}
	}
	
	public void trainByOnlineUpdate(List<XorSampleSet> samples, double learningRate) throws Exception{
		for(XorSampleSet sample : samples){
			trainByOnlineUpdate(sample, learningRate);
//			INDArray errorVector =  Net.eval(sample.goldVector, activationVectorL2);
		}
	}
		
	public void trainByOnlineUpdate(XorSampleSet sample, double learningRate) throws Exception{
		forward(sample.inputVector);
		backprop(sample.goldVector);
		updateWeightByOnelineUpdate(learningRate);
	}
	
	
	public void trainByBatchUpdate(List<XorSampleSet> samples, int iterNum, double initLearningRate, double minus, double minLearningRate) throws Exception{
		double learningRate = initLearningRate;
		for(int i=0;i<iterNum;i++){
			learningRate = getLearningRate(learningRate, minus, minLearningRate);
			trainByBatchUpdate(samples, learningRate);
			printWeight("after." + i );
		}
	}
	
	// 차이를 축적해야 한다. 
	public void trainByBatchUpdate(List<XorSampleSet> samples, double learningRate) throws Exception{
		for(XorSampleSet sample : samples){
			forward(sample.inputVector);
			backprop(sample.goldVector);
			sumDelta();
		}
		updateWeightByBatchUpdate(learningRate);
	}
	
	
	
	public void updateWeightByOnelineUpdate(double learningRate){
		for(int i=layerList.size()-1;i>=0;i--){
			Layer layer = layerList.get(i);

			layer.updateWeight(learningRate);
			
		}
	}
	
	public void updateWeightByBatchUpdate(double learningRate){
		for(int i=layerList.size()-1;i>=0;i--){
			Layer layer = layerList.get(i);

			layer.updateWeightFromSum(learningRate);
			
		}
	}
	
	public void sumDelta() throws Exception{
		for(int i=layerList.size()-1;i>=0;i--){
			Layer layer = layerList.get(i);
			layer.sumDeltaAndGradientWeight();
//			layer.print();
		}
	} 
	
	public void backprop(INDArray goldVector) throws Exception{
		INDArray gradientOfError = null;
		for(int i=layerList.size()-1;i>=0;i--){
			Layer layer = layerList.get(i);
			if(i== (layerList.size()-1)){
				gradientOfError = layer.backprop(null, goldVector);
			}else{
				gradientOfError = layer.backprop(gradientOfError, null);
			}
//			layer.print();
		}
	} 
	
	public INDArray forward(INDArray inputVector){
		INDArray resVector = inputVector.dup();
		for(int i=0;i<layerList.size();i++){
			Layer layer = layerList.get(i);
			resVector = layer.forward(resVector);
		}
		return resVector;
	}
	
}


//public void forwardAndBackPropOneCycle(XorSampleSet sample, double learningRate) throws Exception{
//INDArray inputVector = null;
//
////log.info("##forward##");
//for(int i=0;i<layerList.size();i++){
//	Layer layer = layerList.get(i);
//	if(i==0){
//		inputVector = sample.inputVector;
//	}
//	
//	inputVector = layer.forward(inputVector);
////	layer.print();
//}
//
////log.info("##backprop##");
//INDArray gradientOfError = null;
//for(int i=layerList.size()-1;i>=0;i--){
//	Layer layer = layerList.get(i);
//	if(i== (layerList.size()-1)){
//		gradientOfError = layer.backprop(null, sample.goldVector);
//	}else{
//		gradientOfError = layer.backprop(gradientOfError, null);
//	}
////	layer.print();
//}
//
//for(int i=layerList.size()-1;i>=0;i--){
//	Layer layer = layerList.get(i);
//	
////	Nd4jUtil.printINDArray(layer.nw.weight, "nw.weight.before.L" + i);
////	Nd4jUtil.printINDArray(layer.nw.bias, "nw.bias.before.L" + i);
//	layer.updateWeight(learningRate);
//	
////	Nd4jUtil.printINDArray(layer.nw.weight, "nw.weight.after.L" + i);
////	Nd4jUtil.printINDArray(layer.nw.bias, "nw.bias.after.L" + i);
//}
//}