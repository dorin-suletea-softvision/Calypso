package engine.geometry;

import engine.exceptions.IntersectionAlgorithmInvalidId;

public class IntersectionAlgorithmFactory {
	public static IntersectionAlgorithmFamily createAlgorithm(int id) throws IntersectionAlgorithmInvalidId{
		switch (id){
			case 1:
				return new IntersectionAlgorithm1();
			default :
				throw new IntersectionAlgorithmInvalidId(id);
		}
	}
}
