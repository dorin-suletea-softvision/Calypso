package engine.exceptions;

public class IntersectionAlgorithmInvalidId extends Throwable{
	private static final long	serialVersionUID	= 1L;

	public IntersectionAlgorithmInvalidId(int id) {
		super ("The intersectionAlgorithmFactory can't find the algorithm specified by "+id);
	}

}
