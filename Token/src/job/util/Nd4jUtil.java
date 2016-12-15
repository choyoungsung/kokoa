package job.util;

import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Dot;
import org.nd4j.linalg.api.ops.impl.accum.distances.CosineSimilarity;
import org.nd4j.linalg.api.rng.DefaultRandom;
import org.nd4j.linalg.factory.Nd4j;

public class Nd4jUtil {
	static Logger log =Logger.getLogger("simple");
		
	public static void main(String[] args) throws Exception{
		testbackprop();
	}
	
	public static void testbackprop() throws Exception{
		//http://neuralnetworksanddeeplearning.com/chap2.html 의 backprop를 그대로 구현
		final int V = 3;
		final int dimension = 2;
		
		INDArray errorVectorL2 = Nd4j.create(new float[]{-1,1, -1}, new int[]{V,1 });// Nd4j.(V, 1, 0, 1, new DefaultRandom());
		INDArray weightMatrixL2 = Nd4j.rand(V, dimension, 0, 1, new DefaultRandom());
		INDArray outputVectorL1 = Nd4j.create(new float[]{0.1f,0.5f, 0.3f}, new int[]{V,1 }); //Nd4j.rand(V, 1, 0, 1, new DefaultRandom());
		
		INDArray erorVectorL1 = backprop(V, dimension, errorVectorL2, weightMatrixL2, outputVectorL1);
	}
	
	public static void updateWeightByErrorVector(INDArray weightMatrix, INDArray errorVector){
		
	}
	
	public static INDArray backprop(int V, int dimension, INDArray errorVectorL2, INDArray weightMatrixL2, INDArray outputVectorL1) throws Exception{
		Nd4jUtil.printINDArray(errorVectorL2, "errorVectorL2");
		Nd4jUtil.printINDArray(weightMatrixL2, "weightMatrixL2");
		Nd4jUtil.printINDArray(outputVectorL1, "outputVectorL1");
		
		INDArray tmp = weightMatrixL2.mulColumnVector(errorVectorL2);
//		INDArray tmp = weightMatrixL2.mul(errorVectorL2.transpose());
		Nd4jUtil.printINDArray(tmp, "tmp");
		
		INDArray errorVectorL1 = Nd4j.zeros(V, 1);
		for(int i=0;i<tmp.size(0);i++){
			double val1 = tmp.getRow(i).sumNumber().doubleValue();
			val1 *= outputVectorL1.getDouble(i, 0);
			errorVectorL1.put(i, 0, val1);
		}
		Nd4jUtil.printINDArray(errorVectorL1, "errorVectorL1");
		return errorVectorL1;
	}
		// asdfasdfasdfa
	public static void move2MeanVectorSlightly(INDArray yMat, INDArray gradientVector, INDArray weightedErrorVector, double learningRate){
		assertIt(yMat, gradientVector, weightedErrorVector);
		
		for(int i=0;i<yMat.size(0);i++){
			INDArray yVector = yMat.getRow(i);
			double weightedError = weightedErrorVector.getDouble(i);
			moveVecSlightly(yVector, gradientVector, weightedError, learningRate);
		}
	}
		
		// assert yMat.size(0) == weightedErrorVector.size(1)
				// assert yMat.getRow(i).size(1) == gradientVector.size(1)
			private static void assertIt(INDArray yMat, INDArray gradientVector, INDArray weightedErrorVector){
				if(yMat.size(0) != weightedErrorVector.size(0)){
					System.err.println("NOT ASSERTED yMat.size(0) == weightedErrorVector.size(0) " + yMat.size(0) + " , " +  weightedErrorVector.size(0));
				}
				
				if(yMat.getRow(0).size(1) != gradientVector.size(1)){
					System.err.println("NOT ASSERTED yMat.getRow(i).size(1) == gradientVector.size(1) " + yMat.getRow(0).size(1) + " , " +  gradientVector.size(1));
				}
			}
				
			private static void moveVecSlightly(INDArray yVector, INDArray gradientVector, double weightedError, double learningRate){
				double similarity1 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(yVector, gradientVector)).getFinalResult().doubleValue();
				double alpha = weightedError*learningRate; //Math.max(weightedError*learningRate, 0.05f); //weightedError는 -+ 방향성이 있어 max를 단순대입하면 안된다. 
				Nd4jUtil.printINDArray(yVector, "yVector.before " + similarity1 );
	//			Nd4jUtil.printINDArray(gradientVector, "gradientVector");
	//			log.info("alpha " + alpha);
				INDArray diffVec = gradientVector.sub(yVector);
	//			Nd4jUtil.printINDArray(diffVec, "diffVec1");
				diffVec.muli(alpha);
				Nd4jUtil.printINDArray(diffVec, "diffVec2");
				yVector.addi(diffVec);
				double similarity2 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(yVector, gradientVector)).getFinalResult().doubleValue();
				
				Nd4jUtil.printINDArray(yVector, "yVector.after " + similarity2);
			}
	
	public static INDArray getDot(INDArray vec1, INDArray matrix){
		INDArray dotTable = Nd4j.zeros(matrix.size(0), 1);
//		log.info("vec1 " + vec1 + " " + vec1.size(0) + " " + vec1.size(1));
		for(int i=0;i<matrix.size(0);i++){
			INDArray vec2 = matrix.getRow(i);
//			log.info("vec2 " + vec2 + " " + vec2.size(0) + " " + vec2.size(1));
			double dot = Nd4j.getExecutioner().execAndReturn(new Dot(vec1, vec2)).getFinalResult().doubleValue();
//			log.info(dot + " " + vec1 + " " + vec2);
			dotTable.put(i, 0, dot);
		}
		return dotTable;
	}
	
	public static void printINDArray2(INDArray vec, String name){
		if(vec.size(0)>1 && vec.size(1) > 1){
			log.info(name +  " = (" + vec.size(0) + "x" + vec.size(1) + ")\n" +vec2String( vec));
		}else{
			log.info(name + " = (" + vec.size(0) + "x" + vec.size(1) + ") " + vec2String(vec));
		}
	}
	
	public static void printINDArray(INDArray vec, String name){
		if(vec.size(0)>1 && vec.size(1) > 1){
			log.info(name +  " = (" + vec.size(0) + "x" + vec.size(1) + ")\n" +vec2String( vec));
		}else{
			log.info(name + " = (" + vec.size(0) + "x" + vec.size(1) + ") " + vec2String(vec));
		}
	}
	
	public static String vec2String(INDArray vec){
		int rowSize = vec.size(0);
		int colSize = vec.size(1);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<rowSize;i++){
			sb.append("\n[");
			for(int j=0;j<colSize;j++){
				double val = vec.getDouble(i, j);
				sb.append(String.format("%.10f ", val));
			}
			sb.append("]");
		}
		return sb.toString();
	}
	
}
