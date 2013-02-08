/**
 * 
 */
package org.dihedron.ogtl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author d093154
 *
 */
public class ExpressionCompilerTest {
	
	@Test
	public void test() {
		ExpressionCompiler compiler = new ExpressionCompiler();
		try {
			Expression expression = compiler.compile("field1.field2.field3['field4'][\"field5\"].array[1].2.method()");
			assertEquals("<object>.field1.field2.field3.field4.field5.array[1][2].method();", expression.toString());			
		} catch (LexerException e) {
			e.printStackTrace();
			fail("error parsing input expression: " + e.getInvalidToken());			
		}		
	}

}
