package com.zcbl.malaka.rpc.client.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * @author jys 2016年9月8日
 */
public class ProxyUtils {
	private final static String PROXY_CLASS_NAME_SUFFIX = "$dynamicProxy_";
	private static int proxyClassIndex = 1;
	private static Map<String, Object> map = new ConcurrentHashMap<>();
	static byte[] b = new byte[0];
	public final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProxyImpl.class);

	public static Object getProxyInstance(String interfaceClassName) {
		Object instance = map.get(interfaceClassName);
		if (instance != null) {
			return instance;
		} else {
			synchronized (b) {
				Object obj = map.get(interfaceClassName);
				if (obj != null) {
					return obj;
				}
				try {
					obj = newProxyInstance(interfaceClassName);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NotFoundException
						| CannotCompileException e) {
					logger.error(e.getMessage());
				}
				map.put(interfaceClassName, obj);
				return obj;
			}
		}
	}

	private static Object newProxyInstance(String interfaceClassName) throws InstantiationException,
			IllegalAccessException, NotFoundException, CannotCompileException, ClassNotFoundException {
		Class interfaceClass = ProxyCache.getInstance().getOraddClass(interfaceClassName);
		return dynamicImplementsInterface(interfaceClass);
	}

	private static Object dynamicImplementsInterface(Class interfaceClass)
			throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
		Object ins = map.get(interfaceClass.getName());
		if (ins != null) {
			return ins;
		}
		ClassPool cp = ClassPool.getDefault();
		cp.insertClassPath(new ClassClassPath(ProxyUtils.class));
		String interfaceName = interfaceClass.getName();
		String proxyClassName = interfaceName + PROXY_CLASS_NAME_SUFFIX + proxyClassIndex++;
		String interfaceNamePath = interfaceName;
		CtClass ctInterface = cp.getCtClass(interfaceNamePath);
		CtClass cc = cp.makeClass(proxyClassName);
		cc.addInterface(ctInterface);
		Method[] methods = interfaceClass.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			ProxyCache.getInstance().addMethod(interfaceName, method.getName(), method);
			dynamicImplementsMethodsFromInterface(interfaceNamePath, cc, method, i);
		}
		Object obj = (Object) cc.toClass().newInstance();
		return obj;
	}

	private static void dynamicImplementsMethodsFromInterface(String interfaceNamePath, CtClass implementer,
			Method methodToImpl, int methodIndex) throws CannotCompileException {
		String methodCode = generateMethodCode(interfaceNamePath, methodToImpl, methodIndex);
		CtMethod cm = CtNewMethod.make(methodCode, implementer);
		implementer.addMethod(cm);
	}

	private static String generateMethodCode(String interfaceNamePath, Method methodToImpl, int methodIndex) {
		String methodName = methodToImpl.getName();
		String methodReturnType = methodToImpl.getReturnType().getName();
		Class[] parameters = methodToImpl.getParameterTypes();
		Class[] exceptionTypes = methodToImpl.getExceptionTypes();
		StringBuffer exceptionBuffer = new StringBuffer();
		if (exceptionTypes.length > 0)
			exceptionBuffer.append(" throws ");
		for (int i = 0; i < exceptionTypes.length; i++) {
			if (i != exceptionTypes.length - 1)
				exceptionBuffer.append(exceptionTypes[i].getName()).append(",");
			else
				exceptionBuffer.append(exceptionTypes[i].getName());
		}
		StringBuffer parameterBuffer = new StringBuffer();
		for (int i = 0; i < parameters.length; i++) {
			Class parameter = parameters[i];
			String parameterType = parameter.getName();
			String refName = "a" + i;
			if (i != parameters.length - 1)
				parameterBuffer.append(parameterType).append(" " + refName).append(",");
			else
				parameterBuffer.append(parameterType).append(" " + refName);
		}
		StringBuffer methodDeclare = new StringBuffer();
		methodDeclare.append("public ").append(methodReturnType).append(" ").append(methodName).append("(")
				.append(parameterBuffer).append(")").append(exceptionBuffer).append(" {\n");
		methodDeclare.append("Object returnObj =com.zcbl.malaka.rpc.client.proxy.ProxyImpl.invoker(\""
				+ interfaceNamePath + "\",\"" + methodName + "\",");
		if (parameters.length > 0)
			methodDeclare.append("new Object[]{");
		for (int i = 0; i < parameters.length; i++) {
			if (i != parameters.length - 1)
				methodDeclare.append("($w)a" + i + ",");
			else
				methodDeclare.append("($w)a" + i);
		}
		if (parameters.length > 0)
			methodDeclare.append("});\n");
		else
			methodDeclare.append("null);\n");
		if (methodToImpl.getReturnType().isPrimitive()) {
			if (methodToImpl.getReturnType().equals(Boolean.TYPE))
				methodDeclare.append("return ((Boolean)returnObj).booleanValue();\n");
			else if (methodToImpl.getReturnType().equals(Integer.TYPE))
				methodDeclare.append("return ((Integer)returnObj).intValue();\n");
			else if (methodToImpl.getReturnType().equals(Long.TYPE))
				methodDeclare.append("return ((Long)returnObj).longValue();\n");
			else if (methodToImpl.getReturnType().equals(Float.TYPE))
				methodDeclare.append("return ((Float)returnObj).floatValue();\n");
			else if (methodToImpl.getReturnType().equals(Double.TYPE))
				methodDeclare.append("return ((Double)returnObj).doubleValue();\n");
			else if (methodToImpl.getReturnType().equals(Character.TYPE))
				methodDeclare.append("return ((Character)returnObj).charValue();\n");
			else if (methodToImpl.getReturnType().equals(Byte.TYPE))
				methodDeclare.append("return ((Byte)returnObj).byteValue();\n");
			else if (methodToImpl.getReturnType().equals(Short.TYPE))
				methodDeclare.append("return ((Short)returnObj).shortValue();\n");
		} else {
			methodDeclare.append("return (" + methodReturnType + ")returnObj;\n");
		}
		methodDeclare.append("}");
		return methodDeclare.toString();
	}

	public static void main(String[] args) {
		System.out.println(ProxyUtils.getProxyInstance("com.zcbl.cloud.rpc.client.invoker.Invoker"));
	}
}
