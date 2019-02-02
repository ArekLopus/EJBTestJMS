package mdb.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class LifecycleInterceptors {
	
	@AroundInvoke
	private Object testAroundInvoke(InvocationContext ctx) throws Exception {
		System.out.println("@AroundInvoke");
		return ctx.proceed();
	}
	
	@AroundConstruct
	private Object testAroundConstruct(InvocationContext ctx) throws Exception {
		System.out.println("@AroundConstruct");
		return ctx.proceed();
	}
	
	@PostConstruct
	private Object init(InvocationContext ctx) throws Exception {
		System.out.println("@PostConstruct");
		return ctx.proceed();
	}
	@PreDestroy
	private Object destroy(InvocationContext ctx) throws Exception {
		System.out.println("@PreDestroy");
		return ctx.proceed();
	}
	
}
