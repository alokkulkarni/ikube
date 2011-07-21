package ikube.web.tag.spelling;

import ikube.web.tag.ATag;

import javax.servlet.jsp.JspException;

/**
 * TODO Document me!
 * 
 * @author Michael Couck
 * @since 10.02.10
 * @version 01.00
 */
public class SpellingWriterTag extends ATag {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			String checkedSearchString = (String) pageContext.getAttribute(CHECKED_SEARCH_STRING);
			// logger.info("Checked search string : " + checkedSearchString);
			if (checkedSearchString != null) {
				pageContext.getOut().print(checkedSearchString);
			}
		} catch (Exception e) {
			logger.error("Exception writing the content out", e);
		}
		return EVAL_PAGE;
	}

}
