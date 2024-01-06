package abistech.resseract.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("ALL")
@Configuration
public class AuthenticationRequestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(AuthenticationRequestInterceptor.class.getName());
    private static final String ID_TOKEN_HEADER_NAME = "Id-Token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("GET") && request.getRequestURI().contains("/Resseract/api/health"))
            return true;

        String token = request.getHeader(ID_TOKEN_HEADER_NAME);
        try {
            AuthenticationService.authenticate(token);
            return true;
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
