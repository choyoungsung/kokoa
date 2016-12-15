package v11stable;

import java.util.List;

import job.util.Nd4jUtil;

import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;

public class TestXorSample {
	static Logger log = Logger.getLogger("simple");
	
	public static void main(String[] args) throws Exception{
		a();
	}
	
	public static void a() throws Exception{
		FeedForwardNetwork ffn = new FeedForwardNetwork();
		ffn.addLayer(Layer.newInstance(NetWB.newInstanceByRandOnlyWeight(4, 2, -2, 2), "1stL"));
		ffn.addLayer(Layer.newInstance(NetWB.newInstanceByRandOnlyWeight(1, 4, -2, 2), "2ndL"));

		int iterNum = 100;
		double learningRate = 0.5f;
		
		List<XorSampleSet> trainings = XorSampleSet.trainingList();
		for(int i=0;i<iterNum;i++){
//			learningRate -= 0.01f;
//			learningRate = Math.max(0.001f, learningRate);
//			ffn.trainByOnlineUpdate(trainings, learningRate);
			ffn.trainByBatchUpdate(trainings, learningRate);
			
		}
		
		test(ffn, trainings);
//		ffn.forwardAndBackPropOneCycle(trainings, iterNum, initLearningRate, minus, minLearningRate);
		
//		log.info("###TESTING###");
//		for(int i=0;i<iterNum;i++){
//			log.info("#iterNum " + i);
//			test(ffn, trainings);
//		}
	}
	
	
	private static void test(FeedForwardNetwork ffn, List<XorSampleSet> trainings) throws Exception{
		log.info("###TESTING###");
		ffn.printWeight("testing");
		for(XorSampleSet sample : trainings){
			
			INDArray outputVectorAfter = ffn.forward(sample.inputVector);
			Nd4jUtil.printINDArray2(sample.inputVector, "sample.inputVector");
			Nd4jUtil.printINDArray2(outputVectorAfter, "outputVector.after");
			Nd4jUtil.printINDArray2(sample.goldVector, "sample.goldVector");
		}
	}
}
