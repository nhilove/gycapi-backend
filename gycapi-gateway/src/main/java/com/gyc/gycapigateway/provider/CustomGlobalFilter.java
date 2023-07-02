package com.gyc.gycapigateway.provider;

import com.gyc.apicommon.model.entity.InterfaceInfo;
import com.gyc.apicommon.model.entity.User;
import com.gyc.apicommon.service.InnerInterfaceInfoService;
import com.gyc.apicommon.service.InnerUserInterfaceInfoService;
import com.gyc.apicommon.service.InnerUserService;
import com.gycapi.gycapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;


    private static final List<String> IP_WHITELIST = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1. 用户发送请求到API网关
        //2. 记录日志

        ServerHttpRequest request = exchange.getRequest();
        String url = request.getPath().value();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + url);
        log.info("请求参数：" + request.getQueryParams());
        String method = request.getMethod().toString();
        log.info("请求方法：" + method);
        log.info("请求来源地址：" + request.getRemoteAddress());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);

        ServerHttpResponse response = exchange.getResponse();
        //3. 黑白名单
        if (!IP_WHITELIST.contains(sourceAddress)) {
            handleNoAuth(response);
        }

        //4. 统一鉴权（判断用户accessKey,secretKey是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        //       String secretKey = headers.getFirst("secretKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //真实情况调用数据库查询是否分发了accessKey,把secretKey查出来
        assert accessKey != null;
        User invokerUser = null;
        try {
            invokerUser = innerUserService.getInvokerUser(accessKey);
        } catch (Exception e) {
            log.error("invoker user is null", e);
        }
        if (invokerUser == null) {
            return handleNoAuth(response);
        }

        //todo 校验nocos，随机数
        //todo 校验时间戳
        //todo 校验签名，实际情况secreKey会从数据库中查询出来
        String secretKey = invokerUser.getSecretKey();
        String sign1 = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(sign1)) {
            return handleNoAuth(response);
        }

        //  5. 判断接口是否可用 查询数据库，这个在backend项目中可以对外暴露接口直接引用
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInvokerInterfaceInfo(url, method);
        } catch (Exception e) {
            log.error("invoker interfaceInfo is null", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //todo 是否还有调用次数
        //6. 将请求路由到接口
        //7. 响应日志
//        Mono<Void> filter = chain.filter(exchange);
//        return filter;
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokerUser.getId());

    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {

                                //  调用成功，调用次数+1 这个在backend项目中可以对外暴露接口直接引用
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                //打印响应日志
                                log.info("响应结果:" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            //9. todo 调用失败，返回一个错误码给用户
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}
