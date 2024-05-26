package br.com.usasistemas.usaplatform.model.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectConverter {
	
	public ObjectConverter(){}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public static <T> T convert(Object source, Class<T> targetClass) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
	
		//check source and target classes
		Class<? extends Object> sourceClass = source.getClass();
		
		//create an instance of the target object
		Constructor<?> targetCt = targetClass.getConstructor();
		T retobj = (T) targetCt.newInstance();
		
		//for each target method
		Method targetMethlist[] = targetClass.getDeclaredMethods();
		Method sourceMethlist[] = sourceClass.getDeclaredMethods();
		for (Method targetMeth: targetMethlist) {
			//for set Methods
            if (targetMeth.getName().startsWith("set")) {
            	//get corresponding get method in the source object
            	for (Method sourceMeth: sourceMethlist){            		
            		if(sourceMeth.getName().startsWith("get") &&
            		   sourceMeth.getName().substring(3).equals(targetMeth.getName().substring(3))){
            			
            			Object sourceArglist[] = new Object[0];
            			Object sourceRetVal = sourceMeth.invoke(source, sourceArglist);
            			
            			Object targetArglist[] = new Object[1];
            			targetArglist[0] = sourceRetVal;
            			
            			Object targetRetVal = targetMeth.invoke(retobj, targetArglist);            			
            		}
            	}            	
            }
        }
		
		return retobj;
	}

}
