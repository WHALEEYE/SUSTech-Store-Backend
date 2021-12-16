package tech.whaleeye.backcontroller.config.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * Let Spring to automatically control the lifecycle of Shiro Beans
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * Enable the support of Shiro Annotations for Spring
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator app = new DefaultAdvisorAutoProxyCreator();
        app.setProxyTargetClass(true);
        return app;
    }

    /**
     * Do not register JWTFilter Bean into Spring in case JWTFilter is registered as a global filter.
     * Global filter will filter all the requests, while there we only need to filter requests except /login
     */
    @Bean
    public FilterRegistrationBean<Filter> registration(JWTFilter filter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter();
    }

    /**
     * Set the authorization of different resources
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/authorized");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");


        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", jwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/v2/api-docs-ext", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");

        // This line should be removed in release version
        filterChainDefinitionMap.put("/test/**", "anon");

        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/vCode/login/**", "anon");
        filterChainDefinitionMap.put("/**", "jwtFilter");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * Set strategy for multiple realms to FirstSuccessfulStrategy
     */
    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new MultiRealmAuthenticator();
        AuthenticationStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    /**
     * Disable session so that every time the user logins, shiro will authenticate again.
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean
    JWTRealm jwtRealm() {
        JWTRealm jwtRealm = new JWTRealm();
        CredentialsMatcher credentialsMatcher = new JWTCredentialsMatcher();
        jwtRealm.setCredentialsMatcher(credentialsMatcher);
        return jwtRealm;
    }

    @Bean
    CardNumberPasswordRealm cardNumberPasswordRealm() {
        CardNumberPasswordRealm cardNumberPasswordRealm = new CardNumberPasswordRealm();
        // Set Hash Algorithm Name
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("MD5");
        // Set Iteration Times
        credentialsMatcher.setHashIterations(1024);
        cardNumberPasswordRealm.setCredentialsMatcher(credentialsMatcher);
        return cardNumberPasswordRealm;
    }

    /**
     * 配置 SecurityManager
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Autowired JWTRealm jwtRealm,
                                                               @Autowired CardNumberPasswordRealm cardNumberPasswordRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 1. Authenticator
        securityManager.setAuthenticator(authenticator());

        // 2. Realm
        List<Realm> realmList = new ArrayList<>();
        realmList.add(jwtRealm);
        realmList.add(cardNumberPasswordRealm);
        securityManager.setRealms(realmList);

        // 3. Turn off the session of Shiro
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }
}