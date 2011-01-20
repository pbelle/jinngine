/**
 * Copyright (c) 2010-2011 Morten Silcowitz
 *
 * This file is part of jinngine.
 *
 * jinngine is free software: you can redistribute it and/or modify it
 * under the terms of its license which may be found in the accompanying
 * LICENSE file or at <http://code.google.com/p/jinngine/>.
 */
package jinngine.physics.solver.experimental;

import java.util.List;
import jinngine.math.Vector3;
import jinngine.physics.Body;
import jinngine.physics.solver.Solver;

/**
 * This is not a real solver, it can simply project the given constraints into their proper limits,
 * which is equivalent to PGS using only the projection part of it. 
 */
public class Projection implements Solver {
	@Override
	public final double solve(List<NCPConstraint> constraints, List<Body> bodies, double epsilon) {
		boolean projected = false;
		
		for (NCPConstraint ci: constraints) {
			double deltaLambda = 0;
			double lambda0 = ci.lambda;

			//do projection
			ci.lambda =
				Math.max(ci.lower, Math.min(lambda0 + deltaLambda,ci.upper ));
			
			//update the V vector
			deltaLambda = ci.lambda - lambda0;
			
			if (deltaLambda!=0)
				projected = true;

			//Apply to delta velocities
			Vector3.add( ci.body1.deltavelocity,     ci.b1.multiply(deltaLambda) );
			Vector3.add( ci.body1.deltaomega, ci.b2.multiply(deltaLambda) );
			Vector3.add( ci.body2.deltavelocity,     ci.b3.multiply(deltaLambda));
			Vector3.add( ci.body2.deltaomega, ci.b4.multiply(deltaLambda));
		} //for constraints
		return projected?1:0;
	}

	@Override
	public void setMaximumIterations(int n) {
	}
}
