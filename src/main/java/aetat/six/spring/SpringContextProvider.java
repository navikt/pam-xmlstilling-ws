package aetat.six.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class SpringContextProvider implements ApplicationContextAware {

   private static ApplicationContext appContext;

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      SpringContextProvider.setContext(applicationContext);
   }

   private static void setContext(ApplicationContext context){
      appContext = context;
   }

   public static ApplicationContext getContext() {
      return appContext;
   }

}
