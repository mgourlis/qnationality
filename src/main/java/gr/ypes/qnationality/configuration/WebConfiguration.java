package gr.ypes.qnationality.configuration;

import gr.ypes.qnationality.filters.changePasswordFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.io.File;
import java.util.List;
import java.util.Locale;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new PageRequest(0, 10));
        argumentResolvers.add(resolver);
        super.addArgumentResolvers(argumentResolvers);
    }

    @Bean
    public FilterRegistrationBean resetPasswordBean() {
        final FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new changePasswordFilter());
        filterRegBean.addUrlPatterns("/**");
        filterRegBean.setEnabled(Boolean.TRUE);
        filterRegBean.setName("Reset Password");
        filterRegBean.setAsyncSupported(Boolean.TRUE);
        return filterRegBean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/upload-dir/**").addResourceLocations("classpath:static/upload-dir");
    }

    @Override
    public void addInterceptors(InterceptorRegistry reg) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("locale");
        reg.addInterceptor(interceptor);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
        msgSrc.setBasename("classpath:i18n/messages");
        msgSrc.setDefaultEncoding("UTF-8");
        return msgSrc;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("GR"));
        resolver.setCookieName("nqI18N_language_cookie");
        return resolver;
    }

    @Bean
    JasperReport examQuestionsReport() throws JRException {
        JasperReport jr = null;
        File f = new File("examQuestionsReport.jasper");
        if (f.exists()) {
            jr = (JasperReport) JRLoader.loadObject(f);
        } else {
            jr = JasperCompileManager.compileReport("src/main/resources/jasperreports/ithageneia_01.jrxml");
            JRSaver.saveObject(jr, "examQuestionsReport.jasper");
        }
        return jr;
    }

    @Bean
    JRFileVirtualizer fileVirtualizer() {
        return new JRFileVirtualizer(100);
    }


}
