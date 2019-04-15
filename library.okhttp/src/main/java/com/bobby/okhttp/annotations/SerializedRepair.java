package com.bobby.okhttp.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于修复数据实体对象中的各个成员变量，当变量的值可能会出现未知预订结果时，可使用该注解对其进行标准化修复。
 * <p>
 * 作者 Bobby on 2017/10/27.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializedRepair
{
    /**
     * 声明其目标的值。要注意当条件<code>{@link #condition()}</code>不为<code>{@link Condition#EMPTY}</code>时，
     * 当前value的值同样以字符串的形式表现，但请务必写成目标类型的标准值格式。<br>
     * 比如当判断条件为<code>{@link Condition#NUMBER}</code>时，其声明的值格式应该遵循正则表达：<code>\d+</code>。
     * 否则，在解析过程中会出现类型错误异常
     *
     * @return 返回已声明的目标值。默认返回空的字符串形式
     * @see #condition()
     */
    String targetValue() default "";

    /**
     * 目标对象的类表达式。当条件<code>{@link #isObject()}</code>成立时，当前宿主会被该方法的返回值重新修改。
     *
     * @return 当前宿主新的对象。默认返回Object
     * @see #isObject()
     */
    Class<?> targetObj() default Object.class;

    /**
     * 判断是否需要对当前宿主对象进行重新赋值的条件
     *
     * @return 返回声明需要对当前宿主对象进行何种条件的判断
     * @see <code>{@link Condition}</code>
     */
    Condition condition();

    /**
     * 为解析器指定当前的宿主对象是否为自定义类对象
     *
     * @return 若是自定义的类对象则返回<code>true</code>，否则返回<code>false</code>
     */
    boolean isObject() default false;
}
