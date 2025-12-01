package com.tienda.tiendaelectronica.configuracion;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext contexto;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        contexto = applicationContext;
    }

    public static <T> T getBean(Class<T> tipo) {
        return contexto.getBean(tipo);
    }
}
