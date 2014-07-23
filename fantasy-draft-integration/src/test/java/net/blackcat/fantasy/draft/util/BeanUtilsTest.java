/**
 * 
 */
package net.blackcat.fantasy.draft.util;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Unit tests for {@link BeanUtils}.
 * 
 * @author Chris
 *
 */
public class BeanUtilsTest {

	private static final String LAST_NAME_TEST = "_lastName";
    private static final String DUMMY = "_dummy";
	
	@Test
    public void testCopyProperties() throws Exception {
		// arrange
        final TestProduct fromProduct = new TestProduct();
        fromProduct.setFirstName(DUMMY);
        fromProduct.setDummyName(LAST_NAME_TEST);
        
        final TestProduct toProduct = new TestProduct();
        
        // act
        BeanUtils.copyProperties(fromProduct, toProduct);
        
        // assert
        assertThat(toProduct.getFirstName()).isEqualTo(DUMMY);
        assertThat(toProduct.getDummyName()).isEqualTo(LAST_NAME_TEST);
        assertThat(toProduct.getName()).isNull();
    }

}

/**
 * 
 * CommonInfraUtilsTest.java - Helper class for testing.
 * 
 * @author Chris
 */
class TestProduct {

    private String firstName;
    private String name;
    private String dummyName;

    /**
     * Getter for firstName.
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for dummyName.
     * @return the dummyName
     */
    public String getDummyName() {
        return dummyName;
    }

    /**
     * Setter for dummyName.
     * @param p_dummyName the dummyName to set
     */
    public void setDummyName(final String p_dummyName) {
        dummyName = p_dummyName;
    }

    /**
     * Setter for firstName.
     * @param p_firstName the firstName to set
     */
    public void setFirstName(final String p_firstName) {
        firstName = p_firstName;
    }

    /**
     * Getter for name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * @param p_name the name to set
     */
    public void setName(final String p_name) {
        name = p_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TestProduct [firstName=" + firstName + ", name=" + name + ", dummyName=" + dummyName + "]";
    }

}