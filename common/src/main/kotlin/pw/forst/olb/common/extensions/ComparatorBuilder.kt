package pw.forst.olb.common.extensions


/**
 * Implementation from - https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/CompareToBuilder.html
 *
 * Assists in implementing [java.lang.Comparable.compareTo] methods.
 *
 **
 *
 * Two Objects that compare equal using `equals(Object)` should normally
 * also compare equal using `compareTo(Object)`.
 *
 *
 * All relevant fields should be included in the calculation of the
 * comparison. Derived fields may be ignored. The same fields, in the same
 * order, should be used in both `compareTo(Object)` and
 * `equals(Object)`.
 *
 *
 * To use this class write code as follows:
 *
 * <pre>
 * public class MyClass {
 * String field1;
 * int field2;
 * boolean field3;
 *
 * ...
 *
 * public int compareTo(Object o) {
 * MyClass myClass = (MyClass) o;
 * return new CompareToBuilder()
 * .appendSuper(super.compareTo(o)
 * .append(this.field1, myClass.field1)
 * .append(this.field2, myClass.field2)
 * .append(this.field3, myClass.field3)
 * .toComparison();
 * }
 * }
</pre> *
 *
 *
 * Values are compared in the order they are appended to the builder. If any comparison returns
 * a non-zero result, then that value will be the result returned by `toComparison()` and all
 * subsequent comparisons are skipped.
 *
 *
 * Alternatively, there are [reflectionCompare][.reflectionCompare] methods that use
 * reflection to determine the fields to append. Because fields can be private,
 * `reflectionCompare` uses [java.lang.reflect.AccessibleObject.setAccessible] to
 * bypass normal access control checks. This will fail under a security manager,
 * unless the appropriate permissions are set up correctly. It is also
 * slower than appending explicitly.
 *
 *
 * A typical implementation of `compareTo(Object)` using
 * `reflectionCompare` looks like:
 *
 * <pre>
 * public int compareTo(Object o) {
 * return CompareToBuilder.reflectionCompare(this, o);
 * }
</pre> *
 *
 *
 * The reflective methods compare object fields in the order returned by
 * [Class.getDeclaredFields]. The fields of the class are compared first, followed by those
 * of its parent classes (in order from the bottom to the top of the class hierarchy).
 *
 * @see java.lang.Comparable
 *
 * @see java.lang.Object.equals
 * @see java.lang.Object.hashCode*
 * @since 1.0
 */
class CompareToBuilder {

    /**
     * Current state of the comparison as appended fields are checked.
     */
    private var comparison: Int = 0

    /**
     *
     * Constructor for CompareToBuilder.
     *
     *
     * Starts off assuming that the objects are equal. Multiple calls are
     * then made to the various append methods, followed by a call to
     * [.toComparison] to get the result.
     */
    init {
        comparison = 0
    }


    //-------------------------------------------------------------------------
    /**
     * Appends to the `builder` the comparison of
     * two `long`s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Long, rhs: Long): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = java.lang.Long.compare(lhs, rhs)
        return this
    }

    /**
     * Appends to the `builder` the comparison of
     * two `int`s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Int, rhs: Int): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = Integer.compare(lhs, rhs)
        return this
    }

    /**
     * Appends to the `builder` the comparison of
     * two `short`s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Short, rhs: Short): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = java.lang.Short.compare(lhs, rhs)
        return this
    }

    /**
     * Appends to the `builder` the comparison of
     * two `char`s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Char, rhs: Char): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = Character.compare(lhs, rhs)
        return this
    }

    /**
     * Appends to the `builder` the comparison of
     * two `byte`s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Byte, rhs: Byte): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = java.lang.Byte.compare(lhs, rhs)
        return this
    }

    /**
     *
     * Appends to the `builder` the comparison of
     * two `double`s.
     *
     *
     * This handles NaNs, Infinities, and `-0.0`.
     *
     *
     * It is compatible with the hash code generated by
     * `HashCodeBuilder`.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Double, rhs: Double): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = java.lang.Double.compare(lhs, rhs)
        return this
    }

    /**
     *
     * Appends to the `builder` the comparison of
     * two `float`s.
     *
     *
     * This handles NaNs, Infinities, and `-0.0`.
     *
     *
     * It is compatible with the hash code generated by
     * `HashCodeBuilder`.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Float, rhs: Float): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        comparison = java.lang.Float.compare(lhs, rhs)
        return this
    }

    /**
     * Appends to the `builder` the comparison of
     * two `booleans`s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    fun append(lhs: Boolean, rhs: Boolean): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        if (lhs == rhs) {
            return this
        }
        if (lhs) {
            comparison = 1
        } else {
            comparison = -1
        }
        return this
    }

    /**
     *
     * Appends to the `builder` the deep comparison of
     * two `long` arrays.
     *
     *
     *  1. Check if arrays are the same using `==`
     *  1. Check if for `null`, `null` is less than non-`null`
     *  1. Check array length, a shorter length array is less than a longer length array
     *  1. Check array contents element by element using [.append]
     *
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    fun append(lhs: LongArray, rhs: LongArray): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        if (lhs.contentEquals(rhs)) {
            return this
        }
        if (lhs.size != rhs.size) {
            comparison = if (lhs.size < rhs.size) -1 else 1
            return this
        }
        var i = 0
        while (i < lhs.size && comparison == 0) {
            append(lhs[i], rhs[i])
            i++
        }
        return this
    }

    /**
     *
     * Appends to the `builder` the deep comparison of
     * two `int` arrays.
     *
     *
     *  1. Check if arrays are the same using `==`
     *  1. Check if for `null`, `null` is less than non-`null`
     *  1. Check array length, a shorter length array is less than a longer length array
     *  1. Check array contents element by element using [.append]
     *
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    fun append(lhs: IntArray, rhs: IntArray): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        if (lhs.contentEquals(rhs)) {
            return this
        }
        if (lhs.size != rhs.size) {
            comparison = if (lhs.size < rhs.size) -1 else 1
            return this
        }
        var i = 0
        while (i < lhs.size && comparison == 0) {
            append(lhs[i], rhs[i])
            i++
        }
        return this
    }


    /**
     *
     * Appends to the `builder` the deep comparison of
     * two `double` arrays.
     *
     *
     *  1. Check if arrays are the same using `==`
     *  1. Check if for `null`, `null` is less than non-`null`
     *  1. Check array length, a shorter length array is less than a longer length array
     *  1. Check array contents element by element using [.append]
     *
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    fun append(lhs: DoubleArray, rhs: DoubleArray): CompareToBuilder {
        if (comparison != 0) {
            return this
        }
        if (lhs.contentEquals(rhs)) {
            return this
        }

        if (lhs.size != rhs.size) {
            comparison = if (lhs.size < rhs.size) -1 else 1
            return this
        }
        var i = 0
        while (i < lhs.size && comparison == 0) {
            append(lhs[i], rhs[i])
            i++
        }
        return this
    }


    //-----------------------------------------------------------------------
    /**
     * Returns a negative integer, a positive integer, or zero as
     * the `builder` has judged the "left-hand" side
     * as less than, greater than, or equal to the "right-hand"
     * side.
     *
     * @return final comparison result
     * @see .build
     */
    fun toComparison(): Int {
        return comparison
    }

    /**
     * Returns a negative Integer, a positive Integer, or zero as
     * the `builder` has judged the "left-hand" side
     * as less than, greater than, or equal to the "right-hand"
     * side.
     *
     * @return final comparison result as an Integer
     * @see .toComparison
     * @since 3.0
     */
    fun build(): Int {
        return Integer.valueOf(toComparison())
    }

}
