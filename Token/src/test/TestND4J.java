package test;

import job.util.Nd4jUtil;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Dot;
import org.nd4j.linalg.api.ops.impl.accum.Norm1;
import org.nd4j.linalg.api.ops.impl.accum.Norm2;
import org.nd4j.linalg.api.ops.impl.accum.Sum;
import org.nd4j.linalg.api.ops.impl.accum.distances.CosineSimilarity;
import org.nd4j.linalg.api.ops.impl.indexaccum.IAMax;
import org.nd4j.linalg.api.ops.impl.transforms.SoftMaxDerivative;
import org.nd4j.linalg.api.ops.impl.transforms.Tanh;
import org.nd4j.linalg.api.rng.DefaultRandom;
import org.nd4j.linalg.factory.Nd4j;

public class TestND4J {
	
	
	public static void main(String[] args) throws Exception{
		o();
	}
	
	public static void o() throws Exception{
		INDArray outputVectorL1 = Nd4j.create(new float[]{0.9f, 0.1f}, new int[]{1,2 });

		Nd4j.getExecutioner().execAndReturn(new SoftMaxDerivative(outputVectorL1));
		System.err.println(outputVectorL1);
		
	}
	
	public static void n() throws Exception{
		INDArray v1 = Nd4j.create(new float[]{1,2, 3}, new int[]{3,1 });
		INDArray v2 = Nd4j.create(new float[]{1,2, 3}, new int[]{3,1 });
		double dot = Nd4j.getExecutioner().execAndReturn(new Dot(v1, v2)).getFinalResult().doubleValue();
		INDArray v3 = v1.mul(v2);
		System.err.println(dot);
		System.err.println(v3);
	}
	
	public static void m() throws Exception{
		INDArray v1 = Nd4j.create(new float[]{-1,1}, new int[]{2,1 });
		double rms = Nd4j.getExecutioner().execAndReturn(new Norm2(v1)).getFinalResult().doubleValue();
		System.err.println(v1);
		System.err.println(rms);
	}
	
	public static void g() throws Exception{
		INDArray mat = Nd4j.create(new float[]{0.97f, 0.14f, 0.20f, 0.10f, 0.03f}, new int[]{5,1 });
		System.err.println(mat  + "\n");
		INDArray mat2 = mat.norm2(0);
		System.err.println(mat2  + "\n");
		
		mat.divi(mat.norm2Number().doubleValue());
		System.err.println(mat  + "\n");
		
	}
	
	public static void l() throws Exception{
		INDArray mat = Nd4j.rand(10, 3);
		System.err.println(mat);
	}
	
	public static void k() throws Exception{
		// y = a*x + y
//		
		
		INDArray v1 = Nd4j.create(new float[]{1,1}, new int[]{2,1 });
		INDArray mean = Nd4j.create(new float[]{2,1}, new int[]{2,1 });
		double similarity1 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(v1, mean)).getFinalResult().doubleValue();
		System.err.println(v1 + "  " + similarity1);
		Nd4j.getBlasWrapper().level1().axpy(1, 0.1*(1-similarity1), mean, v1);
		double similarity2 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(v1, mean)).getFinalResult().doubleValue();
		System.err.println(v1 + "  " + similarity2);
	}
	
	public static void j() throws Exception{
		// y = a*x + y
//		Nd4j.getBlasWrapper().level1().axpy(syn1.length(), g, syn1, neu1e);
		
		INDArray v1 = Nd4j.create(new float[]{1,1}, new int[]{2,1 });
		INDArray mean = Nd4j.create(new float[]{2,1}, new int[]{2,1 });
		double dot = Nd4j.getBlasWrapper().dot(v1, mean);
		System.err.println(dot);
	}
	
	public static void i() throws Exception{
		INDArray v1 = Nd4j.create(new float[]{1,1}, new int[]{2,1 });
		INDArray mean = Nd4j.create(new float[]{2,1}, new int[]{2,1 });
		INDArray v2 = v1.add(mean.divi(1));
		INDArray v3 = v1.add(mean.divi(100));
		INDArray v4 = v1.add(mean.divi(10000));
		System.err.println(v1);
		System.err.println(v2);
		System.err.println(v3);
		System.err.println(v4);
		
		double similarity1 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(v1, v2)).getFinalResult().doubleValue();
		double similarity2 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(v1, v3)).getFinalResult().doubleValue();
		double similarity3 = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(v1, v4)).getFinalResult().doubleValue();
		
		System.err.println(similarity1);
		System.err.println(similarity2);
		System.err.println(similarity3);
	}
	
	public static void h() throws Exception{
		INDArray mat = Nd4j.create(new float[]{1,1,1}, new int[]{3,1 });
		INDArray mat2 = Nd4j.create(new float[]{2,2,2}, new int[]{3,1 });
		double sum = Nd4j.getExecutioner().execAndReturn(new CosineSimilarity(mat, mat2)).getFinalResult().doubleValue();
		System.err.println(sum);
	}
	
	
	
	public static void f() throws Exception{
		INDArray mat = Nd4j.rand(3, 2);
		System.err.println(mat  + "\n");
		INDArray mat2 = Nd4j.getExecutioner().execAndReturn(new Tanh(mat));
		System.err.println(mat2  + "\n");
		
		double sum = Nd4j.getExecutioner().execAndReturn(new Sum(mat)).getFinalResult().doubleValue();
		System.err.println(sum  + "\n");
		
		int idx = Nd4j.getExecutioner().execAndReturn(new IAMax(mat)).getFinalResult();
		System.err.println(idx + "\n");
	}
	
	public static void e() throws Exception{
		INDArray mat = Nd4j.rand(3, 2);
		System.err.println(mat  + "\n");
		mat.putScalar(new int[]{0,1}, 10f);
		System.err.println(mat  + "\n");
	}
	
	public static void d() throws Exception{
		String file = "mat.test2.txt";
		INDArray mat = Nd4j.rand(30, 1);
		Nd4j.writeTxt(mat, file, "\t");
		System.err.println(mat  + "\n");
		INDArray mat2 = Nd4j.readTxt(file);
		System.err.println(mat2  + "\n");
	}
	
	public static void c() throws Exception{
		INDArray mat = Nd4j.rand(3, 2);
		System.err.println(mat  + "\n");
	}
	
	public static void b() throws Exception{
		INDArray mat = Nd4j.create(new float[]{1,2,3,4,5,6}, new int[]{3,2 });
		System.err.println(mat  + "\n");
		
		mat.addi(1);
		System.err.println(mat  + "\n");
		
		mat.addi(Nd4j.create(new float[]{1,2,3,4,5, 6}, new int[]{3,2 }));
		System.err.println(mat  + "\n");
		
		mat.getRow(0).addi(Nd4j.create(new float[]{10,10}, new int[]{2,1 }));
		System.err.println(mat  + "\n");
		
		mat = mat.transpose();
		System.err.println(mat  + "\n");
	}
	
	// zero 3,5 matrix 생성하기
	public static void a() throws Exception{
		INDArray mat = Nd4j.zeros(3, 5);
		System.err.println(mat);
	}
	
}
