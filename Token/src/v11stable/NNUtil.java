package v11stable;

import job.util.Nd4jUtil;

import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Exp;
import org.nd4j.linalg.api.ops.impl.transforms.SoftMax;
import org.nd4j.linalg.api.ops.impl.transforms.SoftMaxDerivative;
import org.nd4j.linalg.factory.Nd4j;

public class NNUtil {
	static Logger log = Logger.getLogger("simple");
	
	// index 재정리
	public static INDArray getGradientOfHiddenError(INDArray gradientOfErrorL2, INDArray gradientOfActivation, INDArray weightMatrixL1){
		Nd4jUtil.printINDArray(gradientOfErrorL2, "gradientOfErrorL2"); // gradientOfErrorL2는 nextLaye이기에 index가 달라져야 한다.  
		Nd4jUtil.printINDArray(gradientOfActivation, "gradientOfActivation");
		Nd4jUtil.printINDArray(weightMatrixL1, "weightMatrixL1");
		
		INDArray res = Nd4j.zeros(weightMatrixL1.size(0), 1);
		int rowSize =weightMatrixL1.size(0);
		int colSize = weightMatrixL1.size(1);  // row , col은 weightMatrixL1을 기준으로 한다. 
		int i;
		int j;
		for(i=0;i<rowSize;i++){
			double val = 0;
//			String tmp = "";
		for(j=0;j<colSize;j++){
				double a = gradientOfErrorL2.getDouble(j, 0);
				double b = gradientOfActivation.getDouble(i, 0);
				double c = weightMatrixL1.getDouble(i, j);
				double d = a*b*c;
//				tmp += String.format(" + gradientOfErrorL2(%d) * gradientOfActivation(%d) * weightMatrixL1(%d, %d)", j, i, i,j);
//				log.info(String.format("val += [%d][%d]  <= gradientOfErrorL2(%d) * gradientOfActivation(%d) * weightMatrixL1(%d, %d)" , i, j, j, i, i,j));
				val += d;
			}
//		log.info(String.format("gradienfOfError[%d,0] <= gradientOfErrorL2[%d,0] * gradientOfActivation[%d,0] * weightMatrixL1[%d, %d]", i, j, i, i, j) );
//		log.info(String.format("gradienfOfError[%d,0]<= %s", i, tmp));
		res.put(i, 0, val);
		};
		
		return res;
	}
	
	// error * derOfActivation * weight
//	public static INDArray getGradientOfHiddenError(INDArray gradientOfErrorL2, INDArray gradientOfActivation, INDArray weightMatrixL1){
//		Nd4jUtil.printINDArray(gradientOfErrorL2, "gradientOfErrorL2"); // gradientOfErrorL2는 nextLaye이기에 index가 달라져야 한다.  
//		Nd4jUtil.printINDArray(gradientOfActivation, "gradientOfActivation");
//		Nd4jUtil.printINDArray(weightMatrixL1, "weightMatrixL1");
//		
//		INDArray res = Nd4j.zeros(weightMatrixL1.size(0), 1);
//		int colSize = weightMatrixL1.size(0); //  수정함
//		for(int j=0;j<colSize;j++){
//			double val = 0;
//		for(int i=0;i<res.size(0);i++){
//				double a = gradientOfErrorL2.getDouble(i, 0);
//				double b = gradientOfActivation.getDouble(i, 0);
//				double c = weightMatrixL1.getDouble(i, j);
//				double d = a*b*c;
//				log.info(String.format("[%d][%d] " , i, j) + d + " " + a + " " + b + " " + c);
//				val += d;
//			}
//		res.put(j, 0, val);
//		}
//		
//		return res;
//	}
	
	public static void updateWeight(INDArray weightMatrix, INDArray gradientWeight, double learningRate){
		for(int i=0;i<weightMatrix.size(0);i++){
			for(int j=0;j<weightMatrix.size(1);j++){
//				double val = weightMatrix.getDouble(i,  j) - gradientWeight.getDouble(i, j)*learningRate;
				double val = weightMatrix.getDouble(i,  j) + gradientWeight.getDouble(i, j)*learningRate;
				weightMatrix.put(i, j, val);
			}
		}
	}
	
	//- deltaIn[col, 1] = weight[row,col] * deltaOut[row, 1]
	public static INDArray getWeightedDelta(INDArray weight, INDArray deltaOut) {
		Nd4jUtil.printINDArray(weight, "weight");
		Nd4jUtil.printINDArray(deltaOut, "deltaOut");
		int rowSize = weight.size(0);
		int colSize = weight.size(1);
		INDArray res = Nd4j.zeros(colSize, 1);
		
		for(int colIdx = 0; colIdx<colSize; colIdx++){
			double delta = 0;
//			String tmp = "";
			for(int rowIdx=0;rowIdx<rowSize;rowIdx++){
				double b = weight.getDouble(rowIdx, colIdx);
				double a = deltaOut.getDouble(rowIdx, 0);
				delta += a*b;
//				tmp += String.format("weight[%d][%d] * deltaOut[%d][%d] + ", rowIdx, colIdx, rowIdx, 0);
//				log.info("[" + rowIdx + "," + colIdx + "] " +  b + " " + c + " " + a + " => " + val);
//			log.info(String.format("delta[%d][0] <= gradientOfErrorOut(%d, 0)  * gradientOfActivation (%d, 0) ", colIdx, rowIdx, rowIdx));
			}
//			log.info(String.format("deltaIn[%d][0] = ", colIdx) + tmp) ;
			res.put(colIdx, 0, delta);
		}
		Nd4jUtil.printINDArray(res, "deltaIn");
		return res;
	}
	
	public static INDArray getDeltaIn(INDArray gradientOfActivation, INDArray weighedDelta) {
		INDArray res = Nd4j.zeros(gradientOfActivation.size(0), 1);
		res = gradientOfActivation.muliRowVector(weighedDelta);
		return res;
//		Nd4jUtil.printINDArray(gradientOfActivation, "gradientOfActivation");
//		Nd4jUtil.printINDArray(deltaOut, "deltaOut");
//		int rowSize = gradientOfActivation.size(0);
//		INDArray res = Nd4j.zeros(colSize, 1);
//		
//		for(int colIdx = 0; colIdx<colSize; colIdx++){
//			double delta = 0;
//			double c = gradientOfActivation.getDouble(colIdx, 0);
//			String tmp = "";
//			for(int rowIdx=0;rowIdx<rowSize;rowIdx++){
//				double b = weight.getDouble(rowIdx, colIdx);
//				double a = deltaOut.getDouble(rowIdx, 0);
//				delta += a*b;
//				tmp += String.format("weight[%d][%d] * deltaOut[%d][%d] + ", rowIdx, colIdx, rowIdx, 0);
////				log.info("[" + rowIdx + "," + colIdx + "] " +  b + " " + c + " " + a + " => " + val);
////			log.info(String.format("delta[%d][0] <= gradientOfErrorOut(%d, 0)  * gradientOfActivation (%d, 0) ", colIdx, rowIdx, rowIdx));
//			}
//			log.info(String.format("deltaIn[%d][0] = ", colIdx) + String.format("gradientOfActivation[%d][0] + " , colIdx ) + tmp) ;
//			res.put(colIdx, 0, delta*c);
//		}
//		Nd4jUtil.printINDArray(res, "deltaIn");
//		return res;
	}
	
//	public static INDArray getDeltaIn(INDArray gradientOfActivation, INDArray weight, INDArray deltaOut) {
//		Nd4jUtil.printINDArray(gradientOfActivation, "gradientOfActivation");
//		Nd4jUtil.printINDArray(weight, "weight");
//		Nd4jUtil.printINDArray(deltaOut, "deltaOut");
//		int rowSize = weight.size(0);
//		int colSize = weight.size(1);
//		INDArray res = Nd4j.zeros(colSize, 1);
//		
//		for(int colIdx = 0; colIdx<colSize; colIdx++){
//			double delta = 0;
//			double c = gradientOfActivation.getDouble(colIdx, 0);
//			String tmp = "";
//			for(int rowIdx=0;rowIdx<rowSize;rowIdx++){
//				double b = weight.getDouble(rowIdx, colIdx);
//				double a = deltaOut.getDouble(rowIdx, 0);
//				delta += a*b;
//				tmp += String.format("weight[%d][%d] * deltaOut[%d][%d] + ", rowIdx, colIdx, rowIdx, 0);
////				log.info("[" + rowIdx + "," + colIdx + "] " +  b + " " + c + " " + a + " => " + val);
////			log.info(String.format("delta[%d][0] <= gradientOfErrorOut(%d, 0)  * gradientOfActivation (%d, 0) ", colIdx, rowIdx, rowIdx));
//			}
//			log.info(String.format("deltaIn[%d][0] = ", colIdx) + String.format("gradientOfActivation[%d][0] + " , colIdx ) + tmp) ;
//			res.put(colIdx, 0, delta*c);
//		}
//		Nd4jUtil.printINDArray(res, "deltaIn");
//		return res;
//	}
	
	// 단순곱이다. Nd4j의 함수로 치환할 것 
	public static INDArray getDeltaOut(INDArray gradientOfErrorOut, INDArray gradientOfActivation) {
		Nd4jUtil.printINDArray(gradientOfErrorOut, "gradientOfErrorOut");
		Nd4jUtil.printINDArray(gradientOfActivation, "gradientOfActivation");
		int rowSize = gradientOfActivation.size(0);
		INDArray res = Nd4j.zeros(rowSize, 1);
		
		for(int rowIdx = 0; rowIdx<rowSize; rowIdx++){
			double b = gradientOfActivation.getDouble(rowIdx, 0);
			double a = gradientOfErrorOut.getDouble(rowIdx, 0);
			double delta = a*b;

//			log.info(String.format("delta[%d][0] <= gradientOfErrorOut(%d, 0)  * gradientOfActivation (%d, 0) ", rowIdx, rowIdx, rowIdx));
				res.put(rowIdx, 0, delta);
//			}
		}
		Nd4jUtil.printINDArray(res, "delta");
		return res;
	}
	
	public static INDArray getGradientWeight(INDArray inputVector, INDArray deltaVector) {
//		Nd4jUtil.printINDArray(deltaVector, "deltaVector");
//		Nd4jUtil.printINDArray(inputVector, "inputVector");
		int rowSize = deltaVector.size(0);
		int colSize = inputVector.size(0);
		INDArray res = Nd4j.zeros(rowSize, colSize);
		
		for(int rowIdx = 0; rowIdx<rowSize; rowIdx++){
			double delta = deltaVector.getDouble(rowIdx, 0);
			for(int colIdx = 0; colIdx<colSize; colIdx++){
				double c = inputVector.getDouble(colIdx, 0);
				
				double val = delta*c; //a*b*c;
//				log.info("[" + rowIdx + "," + colIdx + "] " +  b + " " + c + " " + a + " => " + val);
//				log.info(String.format("gradientWeight[%d][%d] <= delta(%d,0) * input(%d,0)", rowIdx, colIdx , rowIdx, colIdx));
				res.put(rowIdx, colIdx, val);
			}
		}
//		Nd4jUtil.printINDArray(res, "gradientWeight");
		return res;
	}
	
	public static INDArray getGradientWeight(INDArray inputVector, INDArray gradientOfError, INDArray gradientOfActivationL2) {
		int rowSize = gradientOfActivationL2.size(0);
		int colSize = inputVector.size(0);
		INDArray res = Nd4j.zeros(rowSize, colSize);
		
		for(int rowIdx = 0; rowIdx<rowSize; rowIdx++){
			double b = gradientOfActivationL2.getDouble(rowIdx, 0);
			double a = gradientOfError.getDouble(rowIdx, 0);
			double tmp = a*b;
			for(int colIdx = 0; colIdx<colSize; colIdx++){
				double c = inputVector.getDouble(colIdx, 0);
				
				double val = tmp*c; //a*b*c;
//				log.info("[" + rowIdx + "," + colIdx + "] " +  b + " " + c + " " + a + " => " + val);
				res.put(rowIdx, colIdx, val);
			}
		}
		return res;
	}
	
	public static INDArray getGradientOfOutputError(INDArray goldAnswerVector, INDArray outputVector) {
//		Nd4jUtil.printINDArray(goldAnswerVector, "goldAnswerVector");
//		Nd4jUtil.printINDArray(outputVector, "outputVector");
		INDArray diffVectorFromGoldAnswer = Nd4j.zeros(goldAnswerVector.size(0), 1);
//		INDArray diffVectorFromGoldAnswer = outputVector.sub(goldAnswerVector);
		for(int i=0;i<goldAnswerVector.size(0);i++){
			double val = goldAnswerVector.getDouble(i) - outputVector.getDouble(i) ;
//			double val = outputVector.getDouble(i) - goldAnswerVector.getDouble(i) ;
			
			diffVectorFromGoldAnswer.put(i, 0, val);
		}
		Nd4jUtil.printINDArray(diffVectorFromGoldAnswer, "diffVectorFromGoldAnswer");
		return diffVectorFromGoldAnswer;
	}
	
	public static INDArray getGradientOfLogisticActivationVector(INDArray activationVectorL1){
		INDArray res = Nd4j.zeros(activationVectorL1.size(0), 1);
		for(int i=0;i<activationVectorL1.size(0);i++){
			double val = activationVectorL1.getDouble(i);
			val = val * (1 - val);
			res.put(i, 0, val);
		}
		return res;
//			return Nd4j.getExecutioner().execAndReturn(new SoftMaxDerivative(outputVectorL1));
	}
	
	public static INDArray forward(INDArray inputVector, NetWB net){
//		Nd4jUtil.printINDArray(inputVector, "inputVector");
//		Nd4jUtil.printINDArray(net.weight, "weight");
		INDArray outputVectorL1 = Nd4jUtil.getDot(inputVector, net.weight);
//		Nd4jUtil.printINDArray(outputVectorL1, "outputVectorL1");
		outputVectorL1.addi(net.bias);
//		Nd4jUtil.printINDArray(outputVectorL1, "outputVectorL1.plusBias");
		return outputVectorL1;
	}
	
	
	public static INDArray activationByLogistic(INDArray outputVectorBeforeSoftMax){
		INDArray res = outputVectorBeforeSoftMax.dup();
//		Nd4j.getExecutioner().execAndReturn(new Exp(res));
		for(int i=0;i<res.size(0);i++){
			double val1 = res.getDouble(i);
			double val = 1 / (Math.exp(-val1) + 1);
//			double val = 1/(res.getDouble(i)+1);
			res.put(i, 0, val);
		}
//		Nd4j.getExecutioner().execAndReturn(new SoftMax(res));
		return res;
	}
	
	public static INDArray eval(INDArray goldAnswerVector, INDArray outputVector) {
		INDArray diffVectorFromGoldAnswer = Nd4j.zeros(goldAnswerVector.size(0), 1);
//		INDArray diffVectorFromGoldAnswer = outputVector.sub(goldAnswerVector);
		for(int i=0;i<goldAnswerVector.size(0);i++){
			double val = goldAnswerVector.getDouble(i) - outputVector.getDouble(i);
			
			val = (val*val)/2.0f;
			
			diffVectorFromGoldAnswer.put(i, 0, val);
		}
		return diffVectorFromGoldAnswer;
	}
	
	
	
	
//	diff에서  weightMatrix가 참여해야 할 것~~~~~
//	public static void updateWeightByErrorVector(INDArray weightMatrix, INDArray gradientOfActivationVector, INDArray weightedErrorVector, double learningRate){// gradient가 필요하다. 
//		// weightMatrix -=  gradientOfActivationVector*weightedErrorVector*learningRate ; // weightedErrorVector가 tutorial과 다르게
//		INDArray diff = gradientOfActivationVector.mul(weightedErrorVector);
//		diff.muli(learningRate);
//		Nd4jUtil.printINDArray(weightMatrix, "weightMatrix.before");
//		Nd4jUtil.printINDArray(diff, "diff");
//		weightMatrix.subiColumnVector(diff);
//		Nd4jUtil.printINDArray(weightMatrix, "weightMatrix.after");
//	}
		
	
	public static INDArray activationBySoftMax(INDArray outputVectorBeforeSoftMax){
		INDArray res = outputVectorBeforeSoftMax.dup();
		Nd4j.getExecutioner().execAndReturn(new SoftMax(res));
		return res;
	}
	
	//outputVectorL1는 SoftMax 의 결과가 있음을 가정, outputVectorL1내 원소 합은 1임을 가정, outputVectorL1원소는 0~1사이의 확률값을 가짐을 가정
		// -2*(x-0.5)^2 + 0.5 를 임의로 정의하여 사용하자. 
		// SoftMax OutputLayer에서만 작동될 것 
	public static INDArray getGradientOfSoftMaxActivationVector(INDArray outputVectorL1){
		INDArray res = Nd4j.zeros(outputVectorL1.size(0), 1);
		for(int i=0;i<outputVectorL1.size(0);i++){
			double val = outputVectorL1.getDouble(i);
			val = -2*(val-0.5)*(val-0.5)+0.5;
			res.put(i, 0, val);
		}
		return res;
//			return Nd4j.getExecutioner().execAndReturn(new SoftMaxDerivative(outputVectorL1));
	}
	
	
//	public static INDArray backprop(int V, int dimension, INDArray errorVectorL2, INDArray weightMatrixL2, INDArray outputVectorL1) throws Exception{
//		Nd4jUtil.printINDArray(errorVectorL2, "errorVectorL2");
//		Nd4jUtil.printINDArray(weightMatrixL2, "weightMatrixL2");
//		Nd4jUtil.printINDArray(outputVectorL1, "outputVectorL1");
//		
//		INDArray tmp = weightMatrixL2.mulColumnVector(errorVectorL2);
////		INDArray tmp = weightMatrixL2.mul(errorVectorL2.transpose());
//		Nd4jUtil.printINDArray(tmp, "tmp");
//		
//		INDArray errorVectorL1 = Nd4j.zeros(V, 1);
//		for(int i=0;i<tmp.size(0);i++){
//			double val1 = tmp.getRow(i).sumNumber().doubleValue();
//			val1 *= outputVectorL1.getDouble(i, 0);
//			errorVectorL1.put(i, 0, val1);
//		}
//		Nd4jUtil.printINDArray(errorVectorL1, "errorVectorL1");
//		return errorVectorL1;
//	}
}
