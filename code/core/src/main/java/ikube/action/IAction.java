package ikube.action;

import ikube.IndexEngine;

/**
 * This is the interface to be implemented for actions see {@link Action}.
 * 
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
public interface IAction<E, F> {

	/**
	 * Executes the action on the index context. The generic parameter E is the index context and the return value is typically a boolean
	 * indicating that the action executed completely or not.
	 * 
	 * @param e
	 *            the index context
	 * @return whether the action completed the logic successfully
	 * @throws Exception
	 *             any exception during the action. This should be propagated up the stack to the caller, which is generally the
	 *             {@link IndexEngine}
	 */
	public F execute(E e) throws Exception;

}