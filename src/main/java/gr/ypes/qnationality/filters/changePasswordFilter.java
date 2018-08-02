package gr.ypes.qnationality.filters;

import gr.ypes.qnationality.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(1)
public class changePasswordFilter implements Filter {

    @Autowired
    RoleRepository roleRepository;

    protected final String ERRORS_KEY = "errors";
    protected final String ROLE_KEY = "ROLE_EXPIRED";
    protected String changePasswordKey = "user.must.change.password";

    private String changePasswordUrl = "/resetpass";
    private String exceptionUrl = "/login";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        UserDetails userDetails = null;
        String requestURL = ((HttpServletRequest) request).getRequestURL().toString();
        try {
            Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (obj instanceof UserDetails) {
                userDetails = (UserDetails) obj;
            }

            if (userDetails != null)
            {
                Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
                boolean hasRole = false;
                for (GrantedAuthority authority : authorities) {
                    hasRole = authority.getAuthority().equals(ROLE_KEY);
                    if (hasRole) {
                        int pos = requestURL.indexOf("/resetpass");
                        if (pos == -1) {
                            saveError(((HttpServletRequest) request), changePasswordKey);
                            sendRedirect(((HttpServletRequest) request), ((HttpServletResponse) response), changePasswordUrl);
                            return;
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = request.getContextPath() + url;
        }

        response.sendRedirect(response.encodeRedirectURL(url));
    }

    public void saveError(HttpServletRequest request, String msg) {
        Set errors = (Set) request.getSession().getAttribute(ERRORS_KEY);

        if (errors == null) {
            errors = new HashSet();
        }

        errors.add(msg);

    }
}
