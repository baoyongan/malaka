package com.zcbl.malaka.rpc.common.annation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Malaka
{
	public String inter() default "";

	public int port() default 0;

	public String value() default "";

	public String version() default "";
}
