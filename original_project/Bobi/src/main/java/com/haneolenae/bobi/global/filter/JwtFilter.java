package com.haneolenae.bobi.global.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haneolenae.bobi.domain.auth.util.JwtTokenProvider;
import com.haneolenae.bobi.global.dto.ApiResponse;
import com.haneolenae.bobi.global.dto.ApiType;
import com.haneolenae.bobi.global.exception.ApiException;
import com.haneolenae.bobi.global.util.RedisUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter implements Filter {

	private final Set<RequestMatcher> excludedPaths = new HashSet<>();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisUtil redisUtil;

	public JwtFilter(JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.redisUtil = redisUtil;
		// 제외하고 싶은 URL과 메소드 조합을 추가
		excludedPaths.add(new RequestMatcher("/member", HttpMethod.POST));
		excludedPaths.add(new RequestMatcher("/auth/login", HttpMethod.POST));
		excludedPaths.add(new RequestMatcher("/auth/refresh", HttpMethod.POST));
		excludedPaths.add(new RequestMatcher("/general/**", HttpMethod.GET));
		excludedPaths.add(new RequestMatcher("/message/sendtest", HttpMethod.GET)); // test for coolsms
		excludedPaths.add(new RequestMatcher("/swagger-ui/**", HttpMethod.GET));
		excludedPaths.add(new RequestMatcher("/swagger-resources/**", HttpMethod.GET));
		excludedPaths.add(new RequestMatcher("/v3/api-docs/**", HttpMethod.GET));
		excludedPaths.add(new RequestMatcher("/swagger-ui.html", HttpMethod.GET));
		excludedPaths.add(new RequestMatcher("/**", HttpMethod.OPTIONS));
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("jwt filter init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		log.info("jwt filter filter in");
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String origin = httpRequest.getHeader("Origin");
		log.info("origin: {}", origin);

		if (shouldExclude(httpRequest)) {
			log.info("exclude");
			chain.doFilter(request, response);
			return;
		}

		try {
			String accessHeader = httpRequest.getHeader("Authorization");

			log.info("accessHeader: " + accessHeader);

			if (accessHeader == null) {
				throw new ApiException(ApiType.ACCESS_TOKEN_INVALID);
			}

			String accessToken = jwtTokenProvider.getTokenFromHeader(accessHeader);

			log.info("accessToken: " + accessToken);

			if (!jwtTokenProvider.validateToken(accessToken)) {
				log.info("invalid access token");
				throw new ApiException(ApiType.ACCESS_TOKEN_INVALID);
			}

			if (redisUtil.isBlackList(accessToken)) {
				log.info("블랙리스트 감지");
				throw new ApiException(ApiType.ACCESS_TOKEN_INVALID);
			}

			chain.doFilter(request, response);
		} catch (ApiException e) {
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			log.info("value: {}", e.getApiType().getStatus().value());
			httpResponse.setStatus(e.getApiType().getStatus().value());
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpResponse.setCharacterEncoding("UTF-8");

			ApiResponse<Object> errorResponse = new ApiResponse<Object>(e.getApiType(), e.getResult());

			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			log.info("response: {}", jsonResponse);

			httpResponse.getWriter().write(jsonResponse);
			httpResponse.flushBuffer();
			httpResponse.getWriter().close();
		}

		log.info("jwt filter filter out");
	}

	private boolean shouldExclude(HttpServletRequest request) {
		String requestPath = request.getRequestURI();
		String method = request.getMethod();

		log.info("requestPath = {}", requestPath);
		log.info("method = {}", method);

		return excludedPaths.stream()
			.anyMatch(matcher -> matcher.matches(requestPath, HttpMethod.valueOf(method)));
	}
}
