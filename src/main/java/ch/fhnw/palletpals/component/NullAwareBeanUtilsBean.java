package ch.fhnw.palletpals.component;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * Code by: Tibor Haller
 * <p>
 * Provides functionality to copy content from object a to existing object b
 * Example usage:
 * PATCH product request -> productPatch A is provided with new content. All content is copied to the existing product B.
 * <p>
 * Note that this component is NullAware, meaning that values of A that are empty will not override existing values of B
 * <p>
 * https://stackoverflow.com/a/45205844
 */
@Component
public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    /**
     * copy properties from object A to object B. Methods is NullAware, meaning that empty values will be ignored.
     *
     * @param dest
     * @param name
     * @param value
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        //value should not be copied if it is either null (strings) or 0.0 (floats)
        if (value == null || value.equals(0.0F))
            return;
        super.copyProperty(dest, name, value);
    }
}