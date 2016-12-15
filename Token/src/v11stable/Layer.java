package v11stable;

import job.util.Nd4jUtil;

import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;

public class Layer {
	static Logger log = Logger.getLogger("simple");
	
	String name;
	
	INDArray inputVector;
	NetWB nw;
	INDArray activationVector;
	INDArray delta;
	INDArray gradientWeight;
	INDArray sumOfgradientWeight;
	INDArray sumOfdeltaOutVector;
	
//	public INDArray backprop(INDArray nextLayersDeltaIn, INDArray goldVector) throws Exception{
//		if(nextLayersDeltaIn==null && goldVector==null){
//			throw new Exception("gradientOfError==null && goldVector==null");
//		}
//		
//		if(goldVector!=null){
//			INDArray gradientOfErrorOut = NNUtil.getGradientOfOutputError(goldVector, activationVector);
//			INDArray gradientOfActivation = NNUtil.getGradientOfLogisticActivationVector(activationVector);
//			this.deltaOut = NNUtil.getDeltaOut(gradientOfErrorOut, gradientOfActivation);
//		}else if(nextLayersDeltaIn!=null){
//			this.deltaOut = nextLayersDeltaIn;
//		}else{
//			throw new Exception("unknown case goldVector nextLayersDeltaIn ");
//		}
//		
//		this.gradientWeight = NNUtil.getGradientWeight(inputVector, deltaOut);
//
//		INDArray deltaIn = NNUtil.getDeltaIn(nw.weight, this.deltaOut);
//		return deltaIn;
//	}

//http://peterroelants.github.io/posts/neural_network_implementation_part05/ 를 참조하여 수정한 버전
/*
if(gold!=null){
	deltaOut = (out-gold) * gradientOfActivationThis
	gradientOfWeightOut = input * deltaOut
	weighedDelta = sum_by_row(weight*deltaOut)
	return weighedDelta
}else{
	 deltaIn =gradientOfActivationThis*weighedDelta    //gradientOfActivationThis * sumOF(deltaOut*weightOut)
	 gradintOfWeightIn = input*deltaIn
	 weighedDelta = sum_by_row(weight*deltaIn)
	 return weighedDelta
}
 */
	public INDArray backprop(INDArray weighedDeltaFromOuter, INDArray goldVector) throws Exception{
		if(weighedDeltaFromOuter==null && goldVector==null){
			throw new Exception("gradientOfError==null && goldVector==null");
		}
		
		if(goldVector!=null){
			INDArray gradientOfErrorOut = NNUtil.getGradientOfOutputError(goldVector, activationVector);
			INDArray gradientOfActivation = NNUtil.getGradientOfLogisticActivationVector(activationVector);
			INDArray deltaOut = NNUtil.getDeltaOut(gradientOfErrorOut, gradientOfActivation);
			this.gradientWeight = NNUtil.getGradientWeight(inputVector, deltaOut);
			this.delta = deltaOut;
			
			INDArray weighedDelta = NNUtil.getWeightedDelta(nw.weight, deltaOut); // bias에 대한 정보 전달은 안하고 있다. 
			return weighedDelta;
		}else if(weighedDeltaFromOuter!=null){
			INDArray gradientOfActivation = NNUtil.getGradientOfLogisticActivationVector(activationVector);
			INDArray deltaIn = NNUtil.getDeltaIn(gradientOfActivation, weighedDeltaFromOuter); // NNUtil.getDeltaIn(gradientOfActivation, nw.weight, deltaOut);
			this.gradientWeight = NNUtil.getGradientWeight(inputVector, deltaIn);
			this.delta = deltaIn;
			INDArray weighedDelta = NNUtil.getWeightedDelta(nw.weight, deltaIn); // bias에 대한 정보 전달은 안하고 있다.
			return weighedDelta;
		}else{
			throw new Exception("unknown case goldVector nextLayersDeltaIn ");
		}
		
	}

	
	public void print(){
		if(nw.weight!=null){
			Nd4jUtil.printINDArray2(nw.weight, name + ".weight");
			Nd4jUtil.printINDArray2(nw.bias, name + ".bias");
//			log.info(name + ".weight&bias" + " = (" + nw.weight.size(0) + "x" + nw.weight.size(1) + ") " + Nd4jUtil.vec2String(nw.weight) + " , " + Nd4jUtil.vec2String(nw.bias));
		}
//			Nd4jUtil.printINDArray(Nd4jUtil.vec2String(nw.weight) + " , " + Nd4jUtil.vec2String(nw.bias), name + ".weight&bias");
//		if(nw.bias!=null)
//			Nd4jUtil.printINDArray(nw.bias, name + ".bias");
//		if(inputVector!=null)
//			Nd4jUtil.printINDArray(inputVector, name + ".inputVector");
//		if(activationVector!=null)
//			Nd4jUtil.printINDArray(activationVector, name + ".activationVector");
//		if(gradientOfActivation!=null)
//			Nd4jUtil.printINDArray(gradientOfActivation, name + ".gradientOfActivation");
//		if(gradientOfError!=null)
//			Nd4jUtil.printINDArray(gradientOfError, name + ".gradientOfError");
//		if(gradientWeight!=null)
//			Nd4jUtil.printINDArray(gradientWeight, name + ".gradientWeight");
//		if(deltaVector!=null)
//			Nd4jUtil.printINDArray(deltaVector, name + ".deltaVector");
	}
	
	public static Layer newInstance(NetWB nw, String name){
		Layer layer = new Layer();
		layer.name = name;
		layer.nw = nw;
		return layer;
	}

	public INDArray forward(INDArray inputVector){
		this.inputVector = inputVector;
		INDArray netVectorL1 = NNUtil.forward(inputVector, nw);
		this.activationVector = NNUtil.activationByLogistic(netVectorL1);
		Nd4jUtil.printINDArray(this.inputVector, name +".input");
		Nd4jUtil.printINDArray(nw.weight, name +".weight");
		Nd4jUtil.printINDArray(nw.bias, name +".bias");
		Nd4jUtil.printINDArray(netVectorL1, name +".net");
		Nd4jUtil.printINDArray(this.activationVector, name +".activation");
		return this.activationVector;
	}
	
	
	static double learningRate4Bias = 0.1f;
	public void updateWeightFromSum(double learningRate){
//		Nd4jUtil.printINDArray2(sumOfgradientWeight, name + ".sumOfgradientWeight");
//		Nd4jUtil.printINDArray2(sumOfdeltaOutVector, name + ".sumOfdeltaOutVector");
		Nd4jUtil.printINDArray2(nw.weight, name + ".nw.weight");
		Nd4jUtil.printINDArray2(nw.bias, name + ".nw.bias");
		NNUtil.updateWeight(nw.weight, sumOfgradientWeight, learningRate);
		NNUtil.updateWeight(nw.bias, sumOfdeltaOutVector, learningRate4Bias);
	}
	
	public void sumDeltaAndGradientWeight(){
		if(this.sumOfdeltaOutVector==null){
			this.sumOfdeltaOutVector = this.delta;
		}else{
			this.sumOfdeltaOutVector.addi(this.delta);
		}
		
		if(this.sumOfgradientWeight==null){
			this.sumOfgradientWeight = this.gradientWeight;
		}else{
			this.sumOfgradientWeight.addi(this.gradientWeight);
		}
	}
	
	public void updateWeight(double learningRate){
		Nd4jUtil.printINDArray(nw.weight, name + ".nw.weight.before");
		Nd4jUtil.printINDArray(gradientWeight, name + ".gradientWeight");
		NNUtil.updateWeight(nw.weight, gradientWeight, learningRate);
		Nd4jUtil.printINDArray(nw.weight, name + ".nw.weight.after");
		
		Nd4jUtil.printINDArray(nw.bias, name + ".nw.bias.before");
		Nd4jUtil.printINDArray(delta, name + ".deltaOut");
		NNUtil.updateWeight(nw.bias, delta, learningRate);
		Nd4jUtil.printINDArray(nw.bias, name + ".nw.bias.after");
	}
	
}
