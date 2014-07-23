/**
 * 
 */
package net.blackcat.fantasy.draft.util;

/**
 * Common Java Bean utilities.
 * 
 * @author Chris
 *
 */
public class BeanUtils {

	/**
     * 
     * copyProperties - Wrapper over spring copyProperties.Copy the property values of the given source bean into the
     * target bean.
     * <p>
     * Note: It copies only if property names are same and leaves if the property names are different.
     * @see {@link org.springframework.beans.BeanUtils#copyProperties(Object, Object)}
     * @param p_fromBean - the source bean.
     * @param p_toBean - the target bean
     * @param <T> Generic Object
     * @param <U> Generic object
     */
    public static <T, U> void copyProperties(final T p_fromBean, final U p_toBean) {
        org.springframework.beans.BeanUtils.copyProperties(p_fromBean, p_toBean);
    }
}
