package com.resolute;

/**
 * This is a marker interface attached to a specific JUnit test method using the {@literal @}Category annotation and used to denote that the method
 * in question represents a slow-running test, as shown in the following example:
 * 
 * <pre>
 * {@literal @}Category(SlowTests.class)
 * {@literal @}Test
 * public void b() {
 * }
 * </pre>
 * 
 * 
 * @author cdevoto
 *
 */
public interface SlowTest {

}
